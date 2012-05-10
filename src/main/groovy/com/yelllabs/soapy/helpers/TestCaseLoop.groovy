package com.yelllabs.soapy.helpers;

import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestStepResult
import com.eviware.soapui.model.testsuite.TestStepResult.TestStepStatus

import com.yelllabs.soapy.enums.Delay;

/**
* @brief 
* @author Janusz Kowalczyk
* @updated 2011-12-23 38115ja - updated run() method and added runTestStep() to simplyfiy the code
* @updated 2012-05-10 38115ja - added import com.yelllabs.soapy.enums.Delay to /
* differentiate it from com.yelllabs.soapy.helpers.Delay
*/
class TestCaseLoop {

  private def context = null;
  private def testRunner = null;
  private def log = null;
  private Random r = new Random();
  private List cfgSteps = [];
  private List testSteps = [];
  private String testCaseName = "";


  /**
  * @brief Constructor
  *
  * @param context
  * @param testRunner
  * @param log
  */
  TestCaseLoop ( def context, def testRunner, def log)
  {
    this.context = context;
    this.testRunner = testRunner;
    this.log = log;
    this.testCaseName = "'" + this.testRunner.testCase.getName() + "' "; 
  }

  public void addCfgSteps( String... names ) {
    add( this.cfgSteps, names );
  }

  public void addTestSteps( String... names ) {
    add( this.testSteps, names );
  }

  private void add( List list, String[] names ) {
    if ( list != null && names.size() > 0 )
    {
      for( name in names ) {
        list.push( ["name":name,"errors": 0] );
      }
    }
  }


  private TestStepStatus runTestStep( String testStepName ) {
    try {
          log.info "Now executing test step: " + testStepName;
          def ts =  this.testRunner.getTestCase().getTestStepByName( testStepName );
          WsdlTestStepResult tsr = this.testRunner.runTestStep( ts, false, false );
          
          return tsr.getStatus();
    } catch ( Exception e ) {
      this.log.info "TestRunner failed to run a test step '"+testStepName+"', because: " + e.printStackTrace();
    }
  }
 

  public void run( int numberOfLoops ) {
    this.run( numberOfLoops, Delay.SEC.set( 3 ) );
  }

  public void run( int numberOfLoops, Delay delay ) {

    for(int i=1; i <= numberOfLoops; i++) {
      
      log.info "Loop no.: " + i;

      for ( step in cfgSteps ) {

          if ( runTestStep( step.name ) != TestStepStatus.OK ) {
            step.errors += 1;
            this.log.error "Config test step: " + step.name + " failed!!!";
          } else {
            this.log.info "Config test step: " + step.name + " passed!!!";
          }
      }
  
      for ( step in testSteps ) {
          if ( runTestStep( step.name ) != TestStepStatus.OK ) {
            step.errors += 1;
            this.log.error "Test step: " + step.name + " failed!!!";
          } else {
            this.log.info "Test step: " + step.name + " passed!!!";
          }

          // sleep for given time
          this.log.info "Waiting " + delay.getValue() + "ms to execute next test step!!";
          sleep( delay.getValue() ); 
      }
      

    } // end for

    assert !isFailed( cfgSteps ), "There were errors when running cfg steps";
    assert !isFailed( testSteps ), "There were errors when running test steps";
  } // end run()


  private boolean isFailed( List steps ) {
    boolean failed = false;
    for ( step in steps )
    {
        if ( step.errors > 0 ) {
          this.log.error "There was: " + step.errors + " errors in step: " + step.name; 
          failed = true;
        }
    }
    return failed;
  }

}
