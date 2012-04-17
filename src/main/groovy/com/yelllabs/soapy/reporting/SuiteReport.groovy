package com.yelllabs.soapy.reporting;


import com.eviware.soapui.model.testsuite.TestStepResult.TestStepStatus

import java.text.SimpleDateFormat

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
class SuiteReport
{
	private def runner = null;
	private def reportFileName = null;
	private def reportSummaryPrefix = null;
	private def outputDir = null;
		
	// an empty collection to store all test resutls
	// used later on to create report
	private def res = []

	private int counter = 0;
	private int passedTotal = 0;
	private int failedTotal = 0;
	
	
	SuiteReport(def runner, def outputDir, def reportFileName, def reportSummaryPrefix)
	{
		this.runner = runner;
		this.outputDir = outputDir;
		this.reportFileName = reportFileName;
		this.reportSummaryPrefix = reportSummaryPrefix;
	}
	
	
	def createReport()
	{
		//log.info("Built in reporting is enabled!")
		//log.info("Start to prepare the test suite report")
		getResultData(this.runner);
		saveToFile(this.res)
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
		   
		   if( testCaseResult.getStatus().toString() == 'FAILED' )
		   {
			   failedTotal++;
			   def stepsResults = []
			  
			  for( testStepResult in testCaseResult.getResults() )
			  {
				testStepResult.messages.each() {
				msg ->
           // add msg only for faile test steps
           if ( testStepResult.getStatus() == TestStepStatus.FAILED ) {
					    stepsResults.add(['stepName':testStepResult.getTestStep().getLabel(),'msg':msg])
           }
				 }
			  }
			 this.res.add(['testCase':testCaseName,'result':'FAILED','steps':stepsResults,'index':counter,'testCaseDescription':testCaseDescription])
		   }
		   
		   if( testCaseResult.getStatus().toString() == 'FINISHED' )
		   {
				   passedTotal++;
				this.res.add(['testCase':testCaseName,'result':'PASSED','index':counter,'testCaseDescription':testCaseDescription])
		   }
		}
	}
	
	private def saveToFile(res)
	{
		// generate random user name using current time and random number
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-d_-_HH-mm-");
		def date = new Date();
		def sDate = formatter.format(date);
		
		// create and flush report file
		File f = new File(outputDir + '/' + sDate + reportFileName + '.html')
		f.write("")
	
		f.append('<HTML>\n<HEAD>\n<LINK REL="stylesheet" TYPE="text/css" HREF="stylesheet.css" TITLE="Style">\n</HEAD>\n<BODY>\n\n')
	
		f.append('<div class="results">\n')
		res.each{
			TC ->


        f.append('\t<div class="testCase">\n')
        f.append('\t\t<div class="testCaseNo">No.: '+TC.index+'</div>\n')
        f.append('\t\t<div class="testCaseResult'+TC.result+'">Result: '+TC.result+'</div>\n')
        f.append('\t\t<div class="testCaseName">Name: <b>'+TC.testCase+'</b></div>\n')
        f.append('\t\t<div class="testCaseDescription">Description:'+TC.testCaseDescription+'</div>\n')
    
				//log.info TC.testCase
				if (TC.result == "PASSED")
				{
				  f.append('\t</div>\n')
				}
			
				if (TC.result == "FAILED")
				{
					f.append('\t\t<div class="testSteps">\n')
					TC.steps.each{
						step ->
						f.append('\t\t\t<div class="step">\n')
						f.append('\t\t\t\t<div class="stepName">Test Step Name: <b>'+step.stepName+'</b></div>\n')
						f.append('\t\t\t\t<div class="errorMsg"><b>Error msg:</b> '+step.msg+'</div>\n')
						f.append('\t\t\t</div>\n')
					}
					f.append('\t\t</div>\n')
					f.append('\t</div>\n')
				}

		}
		f.append('</div>\n')

		f.append('<div class="suiteSummary">\n')
		f.append('\t<div class="total">'+reportSummaryPrefix+' Total: '+counter+' / Passed: '+passedTotal+' / Failed: '+failedTotal+'</div>\n')
		f.append('</div>\n')
		f.append('\n</BODY>\n</HTML>')
		
	}

}
