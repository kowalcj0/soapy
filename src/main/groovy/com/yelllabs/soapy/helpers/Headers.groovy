package com.yelllabs.soapy.helpers;

import com.eviware.soapui.support.types.StringToStringMap;


/**
* @brief Rest Request Header manager. Use to add or remove headers from a given request
* @author 38115ja Janusz Kowalczyk
* @created 2012-07-04
* @updated 2012-07-04 38115ja
*/
class Headers {

    private def context = null;
    private def testRunner = null;
    private def headers = "";
    private def log = null;
    private String stepName = "";
  

    /**
    * @brief Default constructor used inside of a typical "Groovy Script" test step
    * and when test is run from a test suite or manually
    * @param context
    * @param testRunner
    * @param log
    * @param stepName
    */
    Headers(def context, def testRunner, def log, String stepName){
        this.context = context;
        this.testRunner = testRunner;
        this.log = log;
        this.stepName = stepName; 
    }


    /**
    * @brief gets the request object for a given test step
    *
    * @return com.eviware.soapui.impl.wsdl.teststeps.RestTestRequest;
    */
    private def getRequest(){
        return this.testRunner.testCase.testSteps[ stepName ].testRequest;
    }



    /**
    * @brief Injects HTTP headers into a REST test step
    *
    * @param headers A map of headers to inject
    */
    public void injectHeaders(HashMap headers){
        assert headers.isEmpty() == false, "Please provide at least one header to inject!!!";
        
        StringToStringMap sm = new StringToStringMap( headers );

        this.getRequest().setRequestHeaders( sm );
    }


    /**
    * @brief This method removes the HTTP header(s) from a REST test request
    *
    * @param headers a String VarArgs defining Headers to remove.
    */
    public void removeHeaders(String... headers){
        assert headers.size() > 0, "Please provide at least one header to remove!!!";
       
        Map tmp = this.getRequest().requestHeaders;

        headers.each{                                                               
            val ->                                                             
                tmp.remove(val);                                                    
        }                                                                           
                                                                                    
        this.getRequest().requestHeaders = tmp;   
    }


    /**
    * @brief This method removes the HTTP header(s) from a REST test request
    *
    * @param headers a Map of Headers to remove. Will use map keys to remove headers.
    */
    public void removeHeaders(Map headers){
        assert headers.isEmpty() == false, "Please provide at least one header to remove!!!";
 
        Map tmp = this.getRequest().requestHeaders;

        headers.each{                                                               
            key, val ->                                                             
                tmp.remove(key);
        }                                                                           
                                                                                    
        this.getRequest().requestHeaders = tmp;   
    }


}// end
