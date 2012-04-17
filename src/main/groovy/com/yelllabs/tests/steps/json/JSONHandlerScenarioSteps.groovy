package com.yelllabs.tests.steps.json

import org.jbehave.core.steps.Steps
import org.junit.Assert
import org.jbehave.core.annotations.*
import com.yelllabs.soapy.json.JSONHandler

/**
 * Created with IntelliJ IDEA.
 * User: jk
 * Date: 11/04/12
 * Time: 10:48
 * To change this template use File | Settings | File Templates.
 */
class JSONHandlerScenarioSteps extends Steps {

    private JSONHandler jsonHandler;
    private Object parsedJSON;
    //private Logger logger;


    @BeforeScenario
    public void createJsonHandler() throws Exception {
        jsonHandler = new JSONHandler();
    }

    @AfterScenario
    public void cleanObjects() throws Exception {
        jsonHandler = null;
        parsedJSON = null;
    }

    @Given("a JSON array: <jsonStringArray>")
    public void parserArray(@Named("jsonStringArray") String jsonStringArray) {
        System.out.print("Parsing string: '"+jsonStringArray+"'\n");
        parsedJSON = jsonHandler.parse(jsonStringArray);
    }

    @Then("I should get an JSON array object")
    public void isJsonArray() {
        Assert.assertTrue("Parsed string is not an JSON Array. It was parsed as: " + jsonHandler.getType(parsedJSON) + ". String passed to parsing was: " + parsedJSON, jsonHandler.isArray(parsedJSON));
    }

}
