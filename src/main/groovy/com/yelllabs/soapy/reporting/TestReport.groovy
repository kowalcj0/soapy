package com.yelllabs.soapy.reporting;
import com.yelllabs.soapy.helpers.Various;
import com.yelllabs.soapy.helpers.Splitter;

import com.eviware.soapui.model.testsuite.TestStepResult.TestStepStatus
import com.eviware.soapui.model.testsuite.TestRunner
import com.eviware.soapui.model.testsuite.TestCase
import java.text.SimpleDateFormat
import org.apache.log4j.Logger

/**
* @brief A custom reporting class. Creates a simple html report, that covers all enabled tests in specific Test Suite
* @author Janusz Kowalczyk
* @updated 2011-12-20 Modified the output so only FAILDED testSteps are reported
* @example Add those lines to Test Suite TearDown Script:
*  import labs.reporting.*;
*
*  def outputDir = context.expand( '${projectDir}') + File.separator + "reports";
*  SuiteReport sr = new SuiteReport(runner, outputDir ,reportFileName, reportSummaryPrefix);
*  sr.createReport();
*/
class TestReport
{
    private def TS = null;
	private def runner = null;
	private def reportFileName = null;
	private def reportSummaryPrefix = null;
	private def outputDir = null;
	private def log = null;
		
	// an empty collection to store all test resutls
	// used later on to create report
	private def res = []
	private def temp = []
	private def sortRes = []
	//private def stepMessage = []

	private int counter = 0;
	
	private int count = 0;
	private int passedTotal = 0;
	private int failedTotal = 0;

	private float passper = 0;
	private float failper = 0;

	private def projectName = runner.getTestSuite().getProject().name;
	private def env = runner.getTestSuite().getProject().getPropertyValue('MAIN-REST-ENDPOINT');
	

	TestReport(def testSuite, def runner, def outputDir, def reportFileName, def reportSummaryPrefix, def log)
	{
        this.TS = testSuite;
		this.runner = runner;
		this.outputDir = outputDir;
		this.reportFileName = reportFileName;
		this.reportSummaryPrefix = reportSummaryPrefix;
		this.log = log;
	}
    
    private List<String> getTCNames() {
       List<String> tcNames = [];
       this.TS.getTestCaseList().each{
          tc ->
            // get all enabled test cases
            if(tc.isDisabled()==false) {
                tcNames.push(tc.name);
            }
       }
        return tcNames;
    }
	
	def createReport()
	{
		log.info("Built in reporting is enabled!")
		getResultData(this.runner);
        mapData(getTCNames());
		saveToFile(this.sortRes)
	}


	private def getResultData(runner)
	{
		// iterate through test suite results
		for( testCaseResult in runner.results )
		{
		   def testCaseName = testCaseResult.getTestCase().name;
       def testCaseDescription = testCaseResult.getTestCase().description;
		   //log.info "TEST CASE NAME : " + testCaseName
		   counter++;
		   def stepsResults = []
		   int stepCounter = 0;
			  for( testStepResult in testCaseResult.getResults() )
			  {
				testStepResult.each() 
				{
					ts ->
	          			 // add msg only for faile test steps
			          // if ( testStepResult.getStatus() == TestStepStatus.FAILED ) 
			           //{
			           	stepCounter++;
			           	String msg = ""
			           	testStepResult.messages.each{
			           		m ->
			           			msg += m+"<br>\n"
			           	}
								    stepsResults.add(['stepIndex':stepCounter,'stepName':testStepResult.getTestStep().getLabel(),'stepResult':testStepResult.getStatus(),'msg':msg,'responseTime':testStepResult.timeTaken])
			          //log.info "s " + testStepResult.messages
			          
				}
				//log.info runner.getTestSuite().getProject().name;
				//log.info runner.getTestSuite().getProject().getPropertyValue('MAIN-REST-ENDPOINT')
				/*testStepResult.messages.each() 
				{
					msg ->
	          			 // add msg only for faile test steps
			          // if ( testStepResult.getStatus() == TestStepStatus.FAILED ) 
			           //{
								    stepsResults.add(['msg':msg])
			          // }
			          log.info "s " + stepsResults
				}*/
			  }
		   if( testCaseResult.getStatus().toString() == 'FAILED' )
		   {
			   failedTotal++;
			   
			 this.res.add(['testCase':testCaseName,'result':'FAILED','steps':stepsResults,'index':counter,'testCaseDescription':testCaseDescription,'testCaseResponseTime':testCaseResult.timeTaken])
		   }
		   
		   if( testCaseResult.getStatus().toString() == 'FINISHED' ) 
		   {
				   passedTotal++;
				this.res.add(['testCase':testCaseName,'result':'PASSED','steps':stepsResults,'index':counter,'testCaseDescription':testCaseDescription,'testCaseResponseTime':testCaseResult.timeTaken])
		   }
		}
	}
	
	private def mapData(temp)
	{
		temp.each {
			t ->
                //log.info "TC " + t

					for(r in res) {
						if(t == r.testCase) {
							count++;
							sortRes.add(['testCase':r.testCase,'result':r.result,'steps':r.steps,'index':count,'testCaseDescription':r.testCaseDescription,'TestCaseResponseTime':r.testCaseResponseTime]);
						}
					}
		}
        //log.info "s " + sortRes
		passper = (counter == 0) ? passedTotal : (passedTotal/counter)*100;
		failper = (counter == 0) ? failedTotal : (failedTotal/counter)*100;
	}

	private def saveToFile(sortRes)
	{
		// generate random user name using current time and random number
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-d_-_HH-mm-");
		def date = new Date();
		def sDate = formatter.format(date);
		
		// create and flush report file
        //File f = new File(outputDir + '/' + sDate + "test" + '.html')
        File f = new File(outputDir + '/' + reportFileName + '.html')

		f.write("")
	
		f.append('<HTML>\n<HEAD>\n<LINK REL="StyleSheet" TYPE="text/css" HREF="teststylesheet.css" TITLE="Style">\n</HEAD>\n<BODY>\n\n')
	
		f.append('<script language="javascript">\n')
    	f.append('function hide(elem) {\n')
    	f.append('if(elem.style.visibility ==\'hidden\'){\n')
    		f.append('elem.style.visibility=\'visible\';\n')
    		f.append('elem.style.display=\'table-row\';\n')
    	f.append('} else {\n')
    		f.append('elem.style.visibility=\'hidden\';\n')
    		f.append('elem.style.display=\'none\';\n')
    	f.append('}}</script>')
    	f.append('<h2 align="center">'+projectName+'</h1>')
    	f.append('<table id="headTable">')
    	f.append('<tr><td class="noborder"><b><font color="blue">ENV : '+env+'</b></font></td>')
    	f.append('<td class="noborder"><b><font color="blue">DATE : '+date+'</b></font></td></tr>')
    	f.append('</table><br>')

    	f.append('<table id="headTable">')
    	f.append('<tr><td class="noborder">')
    	f.append('<table id="resultTable" align="center">')
		f.append('<tr><td class="report results">Total</td>')
		f.append('<td class="report results">'+counter+'</td></tr>')
		f.append('<tr><td class="testCaseResultPASSED results">Passed</td>')
		f.append('<td class="report results">'+passedTotal+'</td></tr>')
		f.append('<tr><td class="testCaseResultFAILED results">Failed</td>')
		f.append('<td class="report results">'+failedTotal+'</td></tr></table></td><td class="noborder" align="center">')

		f.append('<table id="progressTable" align="center">')
		f.append('<tr><td width='+passper+'% bgcolor=green height=20px> </td>')
		f.append('<td width='+failper+'% bgcolor=red height=20px> </td></tr>')
		f.append('</table></td></tr></table><br>')

		//f.append('<div class="results">\n')
		 f.append('<table id="maintable"> <tr class="headerRow">\n')
		 f.append('<th width="3%">No.</th>\n')
		 f.append('<th width="28%">Name</th>\n')
		 f.append('<th width="6%">Result</th>\n')  
		 f.append('<th width="28%">Message</th>\n')
		 f.append('<th width="7%">Duration (ms)</th>\n') 
		 f.append('<th width="28%">Description</th> </tr>\n')             
		
		sortRes.each{
			TC ->

		f.append('<tr>')
		f.append('<td class="noborder" colspan="6">')
		f.append('<table class="subtable">')
		f.append('<tr class="row'+TC.result+'" onClick="hide(document.getElementById(\'tc'+TC.index+'\'));">')
        f.append('\n')
        f.append('<td width="3%"> '+TC.index+'</td>\n')
        f.append('<td width="28%"> <a href ="javascript:;">'+TC.testCase+'</a> </td>\n')
        f.append('<td width="6%">'+TC.result+'</td>\n')
        f.append('<td width="28%"> Message</td>')
        f.append('<td width="7%">'+TC.TestCaseResponseTime+'</td>\n')
        f.append('<td width="28%">'+TC.testCaseDescription+'</td> </tr> \n')

        f.append('<tr id="tc'+TC.index+'" class="hiderow">')
        f.append('<td class="noborder" colspan="6">')
        f.append('<table class="subtable">')
		        TC.steps.each{
       		step->
        f.append('<tr class="step'+step.stepResult+'">')
        f.append('<td width="3%" class="tdbottom">('+step.stepIndex+')</td>')
        f.append('<td width="28%" class="tdbottom">'+step.stepName+'</td>')
        f.append('<td class="tdbottom" width="6%">'+step.stepResult+'</td>')
        f.append('<td width="28%" class="tdbottom">'+step.msg+'</td>')
        f.append('<td class="tdbottom" width="7%">'+step.responseTime+'</td>')
        f.append('<td width="28%" class="tdbottom"> </td>')
        f.append('</tr>')

   		}	
   		f.append('</table> </td> </tr>')
   		f.append('</table> </td> </tr>')

		}

		f.append('</table>')
	
		f.append('\n</BODY>\n</HTML>')
		
	}
}
