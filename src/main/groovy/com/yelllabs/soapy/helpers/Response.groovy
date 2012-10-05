package com.yelllabs.soapy.helpers;


import com.eviware.soapui.impl.wsdl.panels.support.MockTestRunContext
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestRunContext
import com.eviware.soapui.impl.wsdl.teststeps.RestResponseMessageExchange
import org.apache.log4j.Logger

class Response
{
  private def context = null;
  //private def testRunner = null;
  private def request = null;
  private def response = null;
  private String rawResponse = "";
  private def headers = "";
  private def log = null;
  private String testCaseName = "";
  private def gut = null;
  private def holder = null;
  private Random r = new Random();



  // ***********************************************************
  // default constructors used inside of a typical "Groovy Script" test step
  // and when test is run from a test suite or manually
  Response( def context, def testRunner, def log, def responseStepName, String namespace ) {
    
    this( context, testRunner, log, responseStepName );
    this.holder.namespaces["ns"] = this.holder.getNodeValue( namespace ); // overwrite namespace automatically set by default constructor
  }

  Response( def context, def testRunner, def log, def responseStepName) {
    this.context = context;
    //this.testRunner = testRunner;
    this.log = log;
    this.testCaseName = "'" + testRunner.testCase.getName() + "' "; 

    // initialization
    this.request = testRunner.testCase.testSteps[ responseStepName ].testRequest
    this.response = this.request.response.getContentAsXml();
    this.rawResponse = this.request.response.getContentAsString();
    //this.headers = this.request.getResponseHeaders();
    //log.info this.response;
    this.gut = new com.eviware.soapui.support.GroovyUtils( this.context )
    this.holder = gut.getXmlHolder( this.response );
    this.holder.namespaces["ns"] = this.holder.getNodeValue("//namespace::*[2]")
  }
  // ***********************************************************



  // ***********************************************************
  // Those are two constructor used inside assertion scripts added to a specific test step
  Response( WsdlTestRunContext context, RestResponseMessageExchange messageExchange, Logger log ) {
    this( context, messageExchange, log, "//namespace::*[2]" );
  }

  Response( WsdlTestRunContext context, RestResponseMessageExchange messageExchange, Logger log, String namespace ) {
      this.context = context;
      this.log = log;

      this.request = messageExchange.getRestRequest();
      this.response = messageExchange.getResponseContentAsXml();
      this.rawResponse = messageExchange.getResponseContent();
      this.headers = messageExchange.getResponseHeaders();
      this.gut = new com.eviware.soapui.support.GroovyUtils( this.context );
      this.holder = gut.getXmlHolder( this.response );
      this.holder.namespaces["ns"] = this.holder.getNodeValue( namespace );
  }
  // ***********************************************************



  // ***********************************************************
  // Those two constructors are used when you run test manually from a Groovy Script test step
  // that has to loop something and you have an assertion script added to one of the steps
  // manually invoked by the parent script
  Response( MockTestRunContext context, RestResponseMessageExchange messageExchange, Logger log ) {
    this( context, messageExchange, log, "//namespace::*[2]" );
  }

  Response( MockTestRunContext context, RestResponseMessageExchange messageExchange, Logger log, String namespace ) {
      this.context = context;
      this.log = log;

      this.request = messageExchange.getRestRequest();
      this.response = messageExchange.getResponseContentAsXml();
      this.rawResponse = messageExchange.getResponseContent();
      this.headers = messageExchange.getResponseHeaders();
      this.gut = new com.eviware.soapui.support.GroovyUtils( this.context );
      this.holder = gut.getXmlHolder( this.response );
      this.holder.namespaces["ns"] = this.holder.getNodeValue( namespace );
  }
  // ***********************************************************




  // ***********************************************************
  public def getNodeValues(String xpath) {
   return this.holder.getNodeValues( xpath ); 
  }

  public def getNodeValue(String xpath) {
   return this.holder.getNodeValue( xpath ); 
  }
  // ***********************************************************

  public String getTCname() {
    return testCaseName;
  }

  public def getRequest() {
    return request;
  }

  public def getResponseAsXML() {
    return this.response;
  }

  public String getRawResponse() {
    return this.rawResponse;
  }

  public def getHeaders() {
    return this.headers;
  }

  public def getReqProp(String propertyName ) {
    return request.getPropertyValue( propertyName );
  }

  public int findIndexOf( List list, String value) {
    return list.findIndexOf( { it.value.toString() == value} )
  }



  public Boolean hasNoDuplicates( List list, String fail_msg, String success_msg ) {
    fail_msg = ( fail_msg != null ) ? fail_msg : "List contains more that one occurences of the same value !!!"
    success_msg  = ( success_msg  != null ) ? success_msg  : "List has no duplicates!!!!!"
    def uniques = [] as Set, dups= [] as Set
        
    list.each{ uniques.add(it) || dups.add(it) }
    uniques.removeAll(dups)

    if ( list.size() == uniques.size() ) {
      log.info success_msg
      return true;
    } 
    else {
      log.info fail_msg + dups
      return false;
    }
    return null;
  }
  
}
