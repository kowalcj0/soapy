package com.yelllabs.soapy.helpers;

import groovy.io.FileType;
import com.yelllabs.soapy.json.JSONHandler;

/**
 * @brief A helper class to quickly create simple REST Mock APIs
 * @author Janusz Kowalczyk (jk)
 * @created 2012-11-28 (jk)
 * @modified 2012-11-28 (jk)
 * @SeeAlso
 *   http://www.soapui.org/apidocs/com/eviware/soapui/impl/wsdl/mock/WsdlMockRequest.html
 *   http://www.soapui.org/apidocs/com/eviware/soapui/impl/wsdl/mock/WsdlMockRunner.html
 * @ToDo:
 *  - send pretty response
 *  - fails to calculate response content length when response body it too large
 *  - clean-up the code
 *  - in-memory DB to store a filename to request triggers mapping: http://hsqldb.org/ or http://www.h2database.com/html/features.html#in_memory_databases
 * @example
 *   curl -ki  http://localhost:8088/deals/deal/1 -H "Accept:application/vnd.soapui.deals.deal-v2+json" -H "ENV:DEV" -XGET
 *   curl -ki  http://localhost:8088/deals/deal/2 -H "Accept:application/vnd.soapui.deals.deal-v2+json" -H "ENV:DEV"  -XGET
 *   curl -ki  http://localhost:8088/deals/deal/878 -H "Accept:application/xml" -H "ENV:DEV"  -XGET
 *   curl -ki  http://localhost:8088/deals/deal/5461 -H "Accept:application/vnd.soapui.deals.deal-v2+json" -H "ENV:DEV"  -XGET
 *   curl -ki  http://localhost:8088/deals -H "Accept:application/vnd.soapui.deals.deal-v2+json" -H "ENV:DEV"  -XGET
 * 
 */
class Ultim8Responder{
    private def log;
    private def context;
    private def req;
    private def runner;
    private def result;
    private String CFG_RESP_DIR;
    private String CFG_PROJ_DIR;
    private String CFG_NO_MATCHING_RESPONSE;
    private String CFG_MORE_THAN_ONE_MATCHING_RESPONSE;
    private Map reqTriggers;


    /*
     * @brief Default constructor
     *      Remember to provide the response dir as a relative path to the project directory
     * 
     */
    Ultim8Responder(def log,def context, def mockRequest, def mockRunner, String CFG_RESP_DIR, String CFG_PROJ_DIR, String CFG_NO_MATCHING_RESPONSE, String CFG_MORE_THAN_ONE_MATCHING_RESPONSE){
        this.log = log;
        this.context = context;
        this.req = mockRequest;
        this.runner = mockRunner;
        this.result = new com.eviware.soapui.impl.wsdl.mock.WsdlMockResult( this.req );
        this.CFG_RESP_DIR = CFG_RESP_DIR;
        this.CFG_PROJ_DIR = CFG_PROJ_DIR;
        this.reqTriggers = this.getRequestTriggers(this.req);
        this.CFG_NO_MATCHING_RESPONSE = CFG_NO_MATCHING_RESPONSE;
        this.CFG_MORE_THAN_ONE_MATCHING_RESPONSE = CFG_MORE_THAN_ONE_MATCHING_RESPONSE;
    }


    /*
     * @brief Overloaded simplified constructor that uses all default settings
     * 
     */
    Ultim8Responder(def log,def context, def mockRequest, def mockRunner){
        this(log,
            context, 
            mockRequest, 
            mockRunner, 
            "/responses/", 
            context.expand('${projectDir}'), 
            context.expand('${projectDir}')+File.separator+"cfg"+File.separator+"NO_MATCHING_RESPONSE.json",
            context.expand('${projectDir}')+File.separator+"cfg"+File.separator+"MORE_THAN_ONE_MATCHING_RESPONSE.json"
            );
    }

    /*
     * @brief Overloaded simplified constructor that uses default settings for the response directory. 
     *      Remember to provide the response dir as a relative path to the project directory
     * 
     */
    Ultim8Responder(def log,def context, def mockRequest, def mockRunner, String CFG_RESP_DIR ){
        this(log,
            context, 
            mockRequest, 
            mockRunner, 
            CFG_RESP_DIR, 
            context.expand('${projectDir}'), 
            context.expand('${projectDir}')+File.separator+"cfg"+File.separator+"NO_MATCHING_RESPONSE.json",
            context.expand('${projectDir}')+File.separator+"cfg"+File.separator+"MORE_THAN_ONE_MATCHING_RESPONSE.json"
            );
    }

    /*
     * @brief Simple helper that can do an automatching using configured settings
     *      User can do everything manually using methods given in this class
     * 
     */
    com.eviware.soapui.impl.wsdl.mock.WsdlMockResult autoMatch(){
        def matchingResponses = this.findMatchingResponses();
        this.printReqTriggers();
        if (matchingResponses.isEmpty()){
            this.log.error "No matching response file was found! Please check them or your request!"
            return this.respondWithFile( CFG_NO_MATCHING_RESPONSE );
        } else if (matchingResponses.size() > 1 ) {
            this.log.error "There's more than one matching response file! Please check them";
            matchingResponses.each{ this.log.error "Conflicting file: " + it};
            return this.respondWithFile( CFG_MORE_THAN_ONE_MATCHING_RESPONSE );
        } else {
            this.log.info "Found one file that matches all the request criteria: " + matchingResponses[0]
            return this.respondWithFile(matchingResponses[0]); // respond with the only one file that matched the request
        }
    }

    def printReqTriggers(){     
        this.log.info "req path: " + this.reqTriggers.PATH;
        this.log.info "req method: " + this.reqTriggers.METHOD;
        this.log.info "req context: " + this.reqTriggers.CONTEXT;
        this.log.info "req headers: " + this.reqTriggers.HEADERS;
        this.log.info "req resource: " + this.reqTriggers.RESOURCE;
        this.log.info "req isBodyPresent : " + this.reqTriggers.ISBODYPRESENT;      
    }
    
    
    boolean isBodyPresent(){
        return (this.req.getRequestContent() == null) ? false : true;
    }
    
    String extractMethod(){
        return this.req.method;
    }
    
    String extractContext(){
        String path = this.req.getPath();
        int beg = 1;
        int end = path.lastIndexOf("/");
        return (end > 1) ? path.substring(beg, end) : path.substring(beg);
    }
    
    String extractResource(){
        String resource = ""
        String path = this.req.getPath();
        char  lastChar = path[path.length()-1]
    
        if (lastChar == '/') {
            resource = "";
        } else {
            resource = path.substring( path.lastIndexOf("/")+1 )
        }
    
        return resource
    }
    
    Map<String,List<String>> extractHeaders(){
        return this.req.getRequestHeaders()
    }
    
    List findMatchingResponses() {
        JSONHandler jh = new JSONHandler( log );
        List matchRespFiles = [];
        List<java.io.File> files = this.getAllResponseFiles();
        
        files.each{
            f ->
                // parse the JSON response file
                Object json = jh.parse( f.getText() );
                // check if it is a valid one
                assert jh.isValid(json), "Response is not an VALID JSON!!!";
                assert jh.isObject(json), "Response is not an JSON OBJECT!!!";
                // check if it matches the request triggers
                if ( this.isMatching(json, this.reqTriggers) ) matchRespFiles.push(f);
        }
        return matchRespFiles;
    }
    
    
    Map getRequestTriggers(def req){
        Map m = [:];
        m.put( "PATH"           , this.req.getPath() ); // currently not used in matching
        m.put( "METHOD"         , extractMethod() );
        m.put( "CONTEXT"        , extractContext() );
        m.put( "RESOURCE"       , extractResource() );
        m.put( "ISBODYPRESENT"  , isBodyPresent() );
        m.put( "HEADERS"        , extractHeaders() );
        return m;
    }
    
    boolean isMatching(Object jsonResp, Map reqTrig){
        boolean METH_MATCH =        ( reqTrig.METHOD.toString().toLowerCase().equals( jsonResp.REQUEST.METHOD.toString().toLowerCase()) ) ? true : false;
        boolean CONTEXT_MATCH =         ( reqTrig.CONTEXT.toString().toLowerCase().equals( jsonResp.REQUEST.CONTEXT.toString().toLowerCase()) ) ? true : false;
        boolean RESOURCE_MATCH =    ( reqTrig.RESOURCE.toString().toLowerCase().equals( jsonResp.REQUEST.RESOURCE.toString().toLowerCase()) ) ? true : false;
        boolean ISBODYPRESENT_MATCH = ( reqTrig.ISBODYPRESENT == jsonResp.REQUEST.ISBODYPRESENT ) ? true : false;
        boolean HEADERS_MATCH =         false;
        
        // do a reverse check
        // so check if all headers defined in the response file
        // are present in the actual request
        List hdrsCheck = [];
        jsonResp.REQUEST.HEADERS.each{
            key, val ->
                if (reqTrig.HEADERS[key]) {
                    if (reqTrig.HEADERS[key][0] == val ) hdrsCheck.push(true) else hdrsCheck.push(false);
                }
        }
        HEADERS_MATCH = hdrsCheck.every{ it == true}; // check if all found headers match the expected ones
    
        return (METH_MATCH && CONTEXT_MATCH && RESOURCE_MATCH && ISBODYPRESENT_MATCH && HEADERS_MATCH) ? true : false;
    }
    
    List<java.io.File> getAllResponseFiles() {
        def list = []
        def dir = new File( this.CFG_PROJ_DIR + this.CFG_RESP_DIR )
        dir.eachFileRecurse (FileType.FILES) { list << it   } // add all found files to list
        return list;
    }

    /*
     * This will always respond with 200 OK and the message in the file provided
     */
    def respondWithFile(String filename) {
        try {
            this.runner.returnFile((this.req.httpResponse), new File( filename ) );
            return this.result;
        } catch (IOException e) {
            this.log.error("Exception when responding with a file: " + e );
        }
        
    }
    
    def respondWithFile(File file) {
        log.info "responding with file"
        JSONHandler jh = new JSONHandler( log );
        Object json = jh.parse( file.getText() );

        // set the response
        this.req.httpResponse.status = (json.RESPONSE.CODE) ? json.RESPONSE.CODE : 200;
        json.RESPONSE.HEADERS.each{
            key, val ->
                this.req.httpResponse.addHeader(key, val)
        }

        // write the response body if needed
        if (json.RESPONSE.BODY) 
            this.req.httpResponse.getWriter().write(json.RESPONSE.BODY.toString());
        
        // set the content length
        // fails to calculate response content length when response body it too large
        this.req.httpResponse.setContentLength( json.RESPONSE.BODY.toString().length() )

        // return the result
        return this.result;
    }
    
}
