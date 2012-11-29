package com.yelllabs.soapy.helpers;

import com.eviware.soapui.impl.wsdl.teststeps.RestResponseMessageExchange;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestRunContext;
import com.eviware.soapui.support.types.StringToStringsMap;
import org.apache.log4j.Logger;


/**
* @brief can create cURL command out of REST request.
* @author 38115ja Janusz Kowalczyk
* @created 2012-07-04
* @updated 2012-11-14 Janusz Kowalczyk
*/
class CurlGenerator{

    private def context;
    private def messageExchange;
    private def request;
    private Logger log;
    private String testCaseName;
    private String reqHttpMethod;
    private String reqContent;
    private String reqContext;
    private String reqEndpoint;
    private byte[] reqRawData;
    private StringToStringsMap reqHeaders;
    private def reqParams;

    /**
    * @brief Default constructor
    *
    * @param context - test run context
    * @param messageExchange - message exchange availabe in a assertion script
    * @param log - Logger
    * @param curlParams - cURL params that will be placed at the begining of cmd
    */
    CurlGenerator( WsdlTestRunContext context, RestResponseMessageExchange messageExchange, Logger log, String curlParams ) {
        this.context = context;
        this.messageExchange = messageExchange;
        this.log = log;

        if ( this.context.getCurrentStep().isDisabled() == false ) {
            init();
            log.info getCurlCmd(curlParams);
        }
    }


    /**
    * @brief Another constructor, this one will use "-ki" as default cURL params
    *
    */
    CurlGenerator( WsdlTestRunContext context, RestResponseMessageExchange messageExchange, Logger log) {
        this(context, messageExchange, log, "ki");
    }


    /**
    * @brief Init method that gets all the needed data from context and msgExchng
    *
    */
    private void init() {
        this.request = messageExchange.getRestRequest();
        this.testCaseName = this.context.getCurrentStep().getLabel();
        this.reqHttpMethod = this.request.getMethod();
        
        // gets the req body if there's one
        // if not the an empty List is returned
        this.reqContent = (this.request.hasRequestBody()) ? this.context.expand(this.request.getRequestContent()) : ""; 

        this.reqParams = this.request.getProperties();

        this.reqContext = this.context.expand(this.request.getResource().getFullPath());
        this.reqEndpoint = this.context.expand(this.request.getEndpoint());

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
        String cmd = "curl %s %s \"%s%s\" %s %s";
        return String.format(cmd, params, getMethod(), getUri(), getParams(), getHeaders(), getContent());
    }


    public byte[] getRawRequestData(){
        return this.reqRawData;
    }

    /**
    * @brief return a list of maps of sent non-empty query parameters
    *
    * @return return a list of maps of sent non-empty query parameters
    */
    private List getSentParams(){
        List params = [];
        this.reqParams.each{
            k,v ->
                if ( !v.getValue().isEmpty() )  {
                    def param = [ "name": v.getName(), "val" :  context.expand( v.getValue() ) ]
                    params.push( param )
            }
        }
        return params;
    }

    /**
    * @brief Converts list of query params into a nicely formatted string
    *
    * @return A string representing all the query params
    */
    private String getParams(){
        String qp = "";
        if ( false == getSentParams().isEmpty() ) {
            getSentParams().each{
                p ->
                // insert ? when processing first param, else insert & 
                    qp += ( qp  == "" ) ? "?" + p.name + "=" + p.val : "&" + p.name + "=" + p.val;
            }
        }
        return qp;
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
