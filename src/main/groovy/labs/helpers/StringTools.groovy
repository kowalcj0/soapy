package labs.helpers;

import java.util.regex.Pattern;

class StringTools
{

	static String cleanTestCaseName(String testCaseName)
	{
		testCaseName = testCaseName.replaceAll("/","")
		testCaseName = testCaseName.replaceAll("\\\\","")
		testCaseName = testCaseName.replaceAll("\\{","")
		testCaseName = testCaseName.replaceAll("\\}","")
		testCaseName = testCaseName.replaceAll(":","")
		testCaseName = testCaseName.replaceAll(";","")
		testCaseName = testCaseName.replaceAll("!","")
		testCaseName = testCaseName.replaceAll(" ","-")
		testCaseName = testCaseName.toLowerCase();
		testCaseName = testCaseName.trim();
		return testCaseName;
	}
	
	static String getDBGfilename(String testCaseName)
	{
		return "DBG-RES-" + cleanTestCaseName(testCaseName) + ".txt"
	}
	
	static String getDBGfilePath(def context, String testCaseName)
	{
		return context.expand( '${projectDir}') + File.separator + getDBGfilename(testCaseName)
	}
}