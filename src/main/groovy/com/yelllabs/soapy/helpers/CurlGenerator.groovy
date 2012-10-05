package com.yelllabs.soapy.helpers;

import com.eviware.soapui.impl.rest.support.RestRequestParamsPropertyHolder;
import com.eviware.soapui.impl.wsdl.teststeps.RestTestRequest;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestRunContext;
import com.eviware.soapui.support.types.StringToStringsMap;
import org.apache.log4j.Logger;


/**
* @brief can create cURL command out of REST request.
* @author 38115ja Janusz Kowalczyk
* @created 2012-07-04
*/
class CurlGenerator{

    private WsdlTestRunContext context;
    private RestTestRequest request;
    private Logger log;
    private String testCaseName;
    private String reqHttpMethod;
    private String reqContent;
    private String reqContext;
    private String reqEndpoint;
    private byte[] reqRawData;
    private StringToStringsMap reqHeaders;
    private RestRequestParamsPropertyHolder reqParams;


    CurlGenerator( WsdlTestRunContext context, RestTestRequest request, Logger log, String curlParams){
        this.context = context;
        this.request = request;
        this.log = log;

        if ( this.context.getCurrentStep().isDisabled() == false ) {
            init();
            log.info getCurlCmd(curlParams);
        }
    }


    CurlGenerator( WsdlTestRunContext context, RestTestRequest request, Logger log) {
        this(context, request, log, "ki");
    }


    private void init() {
        this.testCaseName = context.getCurrentStep().getLabel();
        this.reqHttpMethod = request.getMethod();
        
        // gets the req body if there's one
        // if not the an empty List is returned
        this.reqContent = (request.hasRequestBody()) ? request.getRequestContent() : ""; 
        this.reqParams = request.params;

        // alt. way to get the URI:
        // context.getProperty("httpMethod").URI.toString();
        //this.reqUri = context.getProperty("httpMethod").URI.toString();
        this.reqContext = context.getProperties().requestUri;
        this.reqEndpoint = context.expand(request.getEndpoint());
        
        log.info "REQUEST HEADERS: " + this.request.getRequestHeaders(); // WHY THERE ARE NO HEADERS?????

        // get request headers
        // works only when request is made manually for the second time, in other case it will be always [:]
        this.reqHeaders = (request.getResponse() != null) ? request.getResponse().getRequestHeaders() : [:];
        
        this.reqRawData = (request.getResponse() != null) ? request.getResponse().getRawRequestData() : [];
    }


    /**
     * @brief Will use "-ki" as default curl parameters
     * 
     * @return a cURL command with "-ki" set as default parameters
     */
    public String getCurlCmd() {
        return getCurl("ki");
    }


    /**
     * @brief 
     * 
     * @param String curlParams - Pass the curl parameters. If "" is used then no params will be added to the command line
     *
     * @return A cURL command with custom parameters
     */
    public String getCurlCmd(String curlParams) {
        return getCurl(curlParams);
    }


    /**
    * @brief Will create a cURL command with provided params
    *
    * @param curlParams custom cURL params
    *
    * @return a cURL command with custom params
    */
    private String getCurl(String curlParams)
    {
        String params = ( curlParams.isEmpty() ) ? "" : "-" + curlParams;
        String cmd = "curl %s %s \"%s\" %s %s";
        return String.format(cmd, params, getMethod(), getUri(), getHeaders(), getContent());
    }


    public byte[] getRawRequestData(){
        return this.reqRawData;
    }


    private String getMethod(){
        return "-X" + reqHttpMethod;
    }

    private String getUri() {
        if ( this.reqContext.toString().toLowerCase().contains( this.reqEndpoint.toString().toLowerCase() ) ){
            return this.reqContext;
        } else {
            return this.reqEndpoint + reqContext;
        }
    }

    private String getHeaders() {
        if ( this.reqHeaders.isEmpty()) {
            return "";
        } else {
            String H = "";
            reqHeaders.each{
                key, val ->
                    H += ' -H "' +key + ':' + val[0] +'"'
            }
            return H;
        }
    }

    private String getContent() {
        if ( this.reqContent.isEmpty() ) {
            return "";
        } else {
            return "-d '" + this.reqContent+"'";
        }
    } 

}//end
