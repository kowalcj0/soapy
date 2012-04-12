package labs.helpers;

//import labs.helpers.Delay;
import labs.helpers.Loops;
import labs.enums.Delay;

import com.eviware.soapui.model.testsuite.TestStepResult.TestStepStatus;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCaseRunner;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestRunContext;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestStepResult;
import com.eviware.soapui.impl.wsdl.panels.support.MockTestRunContext;
import com.eviware.soapui.impl.wsdl.panels.support.MockTestRunner;

import org.apache.log4j.Logger;


/**
* @brief A helper class to repeat steps until all of them pass
* @author Janusz Kowalczyk 38115ja
* @created 2011-12-14
* @updated 2011-12-14
*/
class TestStepLoop {

  private WsdlTestRunContext context;
  private WsdlTestCaseRunner testRunner;
  private Logger log;
  private def tr;
  private def c;

  TestStepLoop( WsdlTestRunContext context, WsdlTestCaseRunner testRunner, Logger log ) {
    this.context = context;
    this.testRunner = testRunner;
    this.log = log;
  }


  TestStepLoop( MockTestRunContext context, MockTestRunner testRunner, Logger log ) {
    this.c = context;
    this.tr = testRunner;
    this.log = log;

  }


  /**
  * @brief Run step once 
  *
  * @param testStepName Name of the test step
  *
  * @return TestStepStatus object
  */
  private TestStepStatus run( String testStepName ) {

    TestStepStatus tss = TestStepStatus.FAILED;
    WsdlTestStepResult tsr; 

    try {
      def ts;
      //def tsr;
      if ( this.testRunner == null ) {
        log.info "Using MockTestRunner!!"
          ts = this.tr.getTestCase().getTestStepByName( testStepName );
        tsr = this.tr.runTestStep( ts );
      } else {
        log.info "Using WsdlTestCaseRunner!!"
          ts =  this.testRunner.getTestCase().getTestStepByName( testStepName );
          tsr = this.testRunner.runTestStep( ts, false, false );
      }

      if (tsr != null ) {
        tss = tsr.getStatus();
        //this.log.info "TSR is NOT null!! " + tss.toString();
        //return tss;
      } else {
        this.log.info "TSR is null!!";
      }
    } catch ( Exception e ) {
      this.log.info "TestRunner failed to run a test step '"+testStepName+"', because: " + e.printStackTrace();
    } finally {
      // ???
    }

    return tss; 
  }


  /**
  * @brief Runs test step once with specific delay
  *
  * @param testStepName Name of the test step to run
  * @param delay int value of delay in milliseconds
  *
  * @return TestStepStatus object;
  */
  public TestStepStatus runStep( String testStepName, Delay delay ) {
    TestStepStatus tss = this.run( testStepName );
    if ( tss == TestStepStatus.FAILED )  sleep( delay.getValue() ); 
    return tss;
  }



  /**
  * @brief Will run specific test step till it pass in a loop
  *
  * @param testStepName Test step name
  * @param delay Delay in milliseconds that will be applied after each run
  * @param loops number of loops
  *
  * @return TestStepStatus object 
  */
  public TestStepStatus runStepTillPass( String testStepName, Delay delay, Loops loops ) {
    int counter = 1;
    TestStepStatus tss = TestStepStatus.FAILED;

    while ( ( tss != TestStepStatus.OK ) && ( counter <= loops.getValue() ) ) {
      
      this.log.info "Iteration no.: " + counter + " for test step: '" + testStepName + "'. Executed with delay=" + delay.getValue() + "ms";

      tss = this.runStep( testStepName, delay );

      if ( tss.equals( TestStepStatus.OK ) ) {
        this.log.info "Test step '"+testStepName+"' passed! Iteration no.: " + counter;
        break;
      } else {
        this.log.info "Test step '"+testStepName+"' failed! Iteration no.: " + counter;
        counter++;
        continue;
      } // end if;
    } // end while;

    return tss;
  }


  /**
  * @brief Will run specific test step till it fails in a loop
  *
  * @param testStepName Test step name
  * @param delay Delay in milliseconds that will be applied after each run
  * @param loops number of loops
  *
  * @return TestStepStatus object 
  */
  public TestStepStatus runStepTillFail( String testStepName, Delay delay, Loops loops ) {
    int counter = 1;
    TestStepStatus tss = TestStepStatus.OK;

    while ( tss.equals( TestStepStatus.OK) && ( counter <= loops.getValue() ) ) {
      
      this.log.info "Iteration no.: " + counter + " for test step: '" + testStepName + "'. Executed with delay=" + delay.getValue() + "ms";

      tss = this.runStep( testStepName, delay );

      if ( tss.equals( TestStepStatus.FAILED ) ) {
        this.log.info "Test step '"+testStepName+"' PASSED but by FAILING the assertions! Iteration no.: " + counter;
        break;
      } else {
        this.log.info "Test step '"+testStepName+"' FAILED by PASSING all assertions! Iteration no.: " + counter;
        counter++;
        continue;
      } // end if;
    } // end while;

    return tss;
  }


}
