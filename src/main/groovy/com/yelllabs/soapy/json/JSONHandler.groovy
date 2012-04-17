package com.yelllabs.soapy.json;

import com.yelllabs.soapy.enums.*;
import org.apache.log4j.Logger
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.json.simple.parser.ParseException

/**
* @brief A helper class to parse JSON responses. JSON-simple lib is user to parse the actual JSON text
* @author Janusz Kowalczyk
* @created 2011-12-22
*/
class JSONHandler {

    private JSONParser parser;
    private static Logger log;

    JSONHandler( Logger log ) {
    this.parser = new JSONParser();
    this.log = log;
    }

    JSONHandler() {
        this.parser = new JSONParser();
        this.log = Logger.getLogger(JSONHandler.class);
    }


    /**
    * @brief a JSON-simple parser's wrapper.
    *
    * @param jsonText any JSON string.
    *
    * @return If exception was found then JSON.INVALID enum is returned;
    */
    public Object parse( String jsonText ) {
    try {
        return this.parser.parse( jsonText );
    } catch( ParseException pe ) {
        log.error " While parsing the text at position: " + pe.getPosition();
        log.info pe;
        return JSON.INVALID;
    }
    }



  /**
  * @brief A helper method to parse the response for a certain test step
  *
  * @param testRunner SoapUI's testRunner object;
  * @param testStepName test step name
  *
  * @return JSON parsed object. Can return JSON.INVALID enum if parsing failed!!!
  */
  public Object parseTestStepResponse( def testRunner, String testStepName ) {
    return this.parse( testRunner.testCase.testSteps[ testStepName ].testRequest.response.getResponseContent() );
  }



  /**
  * @brief Checks the object's type by simply using instanceof
  *
  * @param jsonObject any parsed jsonObject
  *
  * @return A JSON enum type;
  */
  public <T> T getType( T jsonObject ) {
     if ( jsonObject instanceof JSONObject ) {
       return JSON.OBJECT;
     } else if ( jsonObject instanceof JSONArray ) {
       return JSON.ARRAY;
     } else if ( !(jsonObject instanceof JSONArray) || !(jsonObject instanceof JSONObject) ) {
       // for more details refer to :
       // http://code.google.com/p/tests.json-simple/source/browse/branches/mavenization/src/main/java/org/tests.json/simple/parser/JSONParser.java
       // Generally, JSONParse method return Instance of the following:
       //   org.tests.json.simple.JSONObject,
       //   org.tests.json.simple.JSONArray,
       //   java.lang.String,
       //   java.lang.Number,
       //   java.lang.Boolean,
       //   null
       // so anything apart from JSONObject and JSONArray is an JSONValue
       return JSON.VALUE;
     } else {
       return JSON.INVALID;
     }
  }


  public <T> boolean isObject( T jsonObject ) {
    return ( this.getType( jsonObject ) == JSON.OBJECT ) ? true : false;
  }

  public <T> boolean isArray( T jsonObject ) {
    return ( this.getType( jsonObject ) == JSON.ARRAY ) ? true : false;
  }

  public <T> boolean isValue( T jsonObject ) {
    return ( this.getType( jsonObject ) == JSON.VALUE ) ? true : false;
  }

  public <T> boolean isValid( T jsonObject ) {
    return ( this.getType( jsonObject ) != JSON.INVALID ) ? true : false;
  }

  /**
  * @brief Checks if given JSON object (that consists of values, arrays or other objects) have an empty values
  *
  * @param jsonObject
  *
  * @return 
  */
  public <T> boolean hasEmptyValues( T jsonObject ){
    boolean hasEmptyValue;

    jsonObject.each{
      key, val ->
        hasEmptyValue = this.hasEmptyValues(key,val);
    }
    return hasEmptyValue;
  }

  /**
  * @brief Iterates through the given JSON object and checks if any of the values are empty
  *
  * @param key value, object or array name
  * @param val any JSON value, that can be represented as value, object or array. For more info go to: http://tests.json.org/
  *
  * @return true when the is at least one empty value, false when no empty value was found
  */
  public boolean hasEmptyValues(String key, Object val) {

    if ( this.isValue( val ) ) {
      if ( val.toString().isEmpty() ) {
        log.error("Key: '" +key+ "' is an empty JSON value!!!");
        return true;
      }
      log.info "key: '" + key + "' value: " + val
    }
    
    if ( this.isArray( val )) {
      log.info "Analyzing array: '" + key + "' holding: " + val.size() + " element(s)."
      val.each{ v -> hasEmptyValues(key, v) }
    }
    
    if ( this.isObject( val )) {
      log.info "Analyzing object: '" + key + "' holding: " + val.size() + " element(s)."
      val.each{ k, v -> hasEmptyValues(k, v) }
    }
    return false;
  }
}
