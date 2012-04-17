package com.yelllabs.soapy.helpers;


public class CSVFileReader {

	private String fileName = null;
	private def log = null;
	private boolean isVerboseMsg = false;
	private ArrayList <ArrayList<Double>>vals = new ArrayList<ArrayList<Double>>();
	
	
	public CSVFileReader(def log, String FileName)
	{
		this.log = log;
		this.fileName=FileName;
	}
	
	
	public verboseMsgs(boolean b)
	{
		this.isVerboseMsg = b;
	}
	
	
	public void ReadFile()
	{
		try {
			//storeValues.clear();//just in case this is the second call of the ReadFile Method./
			BufferedReader br = new BufferedReader( new FileReader(fileName));
		
			StringTokenizer st = null;
			int lineNumber = 0, tokenNumber = 0;
 
			while( (fileName = br.readLine()) != null)
			{
				lineNumber++;
				//storeValues.add(fileName);
				if (this.isVerboseMsg) log.info(fileName)
				
				//break comma separated line using ";"
				st = new StringTokenizer(fileName, ";");

				ArrayList <Double>rowVals = new ArrayList<Double>();
				while(st.hasMoreTokens())
				{
					//log.info Float.parseFloat( st.nextToken() )
					rowVals.add( Double.parseDouble( st.nextToken() ) )
					if (this.isVerboseMsg)
					{
						log.info("Line # "
							+ lineNumber
							+ ", Token # " + tokenNumber
							+ ", Token : "+ st.nextToken());
					}
				}
 
				
				this.vals.add(rowVals)
				
				//reset token number
				tokenNumber = 0;
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	/java.math.BigDecimal
	
	//mutators and accesors
	public void setFileName(String newFileName)
	{
		this.fileName=newFileName;
	}
	
	public String getFileName()
	{
		return fileName;
	}
	
	public ArrayList <ArrayList<Double>> getVals()
	{
		return this.vals;
	}
	
	
	public void displayArrayList()
	{
		if (this.isVerboseMsg)
		{
			for(int x=0;x<this.storeValues.size();x++)
			{
				log.info(storeValues.get(x));
			}
		}
	}
	
}