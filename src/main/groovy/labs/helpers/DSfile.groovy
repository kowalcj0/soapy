package labs.helpers;



/**
 * A helper class to manage data source file stored as regular java property file
 * @author - Janusz Kowalczyk
 */
class DSfile
{
  
  private FileInputStream fis = null;
  private def props = new java.util.Properties();
  private def log = null;
  private String filePath = "";

 /**
  * Default constructor : open a file and reads all the properties
  * @param log - default soapui logger
  */
  DSfile( String filePath, def log )
  {
    try
    {
      this.filePath = filePath;
      this.fis = new FileInputStream( filePath );
      this.log = log;
      this.read(); // read all values 
    }
    catch(IOException e)
    {
      System.err.println("Caught IOException while reading the file, exception: " + e.getMessage());
    }
  }



// ***************************** Default methods ****************

 /**
  * Reads the property file and returns a Properties object 
  */
  public Properties read()
  {
      this.props.load( this.fis )
      return this.props;
  }

  /**
  * @brief Saves property file
  *
  */
  public void write()
  {
    // Write properties file.
    try
     {
        this.props.store( new FileOutputStream( this.filePath ) , "Merchant API user content holder" );
     } 
    catch (IOException e) 
     {
        System.err.println("Caught IOException when writing to file, exception: " + e.getMessage());
     } 
  }

// ***************************** Default methods ****************




// ***************************** Getters ****************

 /**
  * Returns value of selected key
  */
  public String get( String key )
  {
     return this.props.getProperty( key )
  }
  
 /**
  * Return all found propeties
  */
  public Properties getAll( )
  {
     return this.props;
  }

  /**
  * @brief Return properties key as a List. If List is empty then null is returned.
  *
  * @param key - a name of a key
  * @param splitter - Splitter that separates key values
  *
  * @return A List of all key values
  */
  public List get( String key, Splitter splitter ) {
    List tmp = get( key ).split( splitter.value() ).toList();
    //log.info "tmp=" + tmp;
    if ( tmp.size() == 1 ) {
      if ( tmp.getAt( 0 ) == "" || tmp.getAt( 0 ) == [] ) {
        return null;
      } else {
        return tmp;
      }
    } else if (tmp.size() > 1 ) return tmp;
  }



  public int getKeySize( String key, Splitter splitter ) {
    return get( key ).split( splitter.value() ).size();
  }


 /**
  * Returns particular value of selected key
  */
  public String getKeyValAt( String key, int index, Splitter valueSplitter )
  {
    String val = this.props.getProperty( key ).split( valueSplitter.value() ).getAt( index )
    valueSplitter.charsToReplace().each
    {
      chr ->
        val = val.replaceAll( chr, "" )
    }
    return val;
  }
 
 /**
  * Return all found propeties
  */
  public int getRandomValueIndex( String key, Splitter valueSplitter )
  {
    return new Random().nextInt( get( key ).split( valueSplitter.value() ).size() )
  }

// ***************************** Getters ****************






// ***************************** Setters ****************

 /**
  * Sets value for a specific property.
  * In case when property doesn't exist, it will create it
  */
  public void set( String key, String val)
  {
      if( key != null && val != "" && val != null ) 
      {
            this.props.setProperty( key , val )
            //log.info( "Property: " + key + " value was set to: [" + val + "]" )
      }
  }
  
  
  /**
  * Sets value for a specific property.
  * In case when property doesn't exist, it will create it
  */
  public void append( String key, Splitter splitter, String valueToAppend )
  {
      if( key != null && valueToAppend != "" && valueToAppend != null && splitter != null ) 
      {
            String newVal = this.get( key ) + splitter.value() + valueToAppend;
            this.props.setProperty( key , newVal )
            //log.info( "Property: " + key + " value was set to: [" + newVal + "]" )
      }
      else {
        log.info "Something went wrong!!! : key=" + key + " valueToAppend=" + valueToAppend + " splitter='" + splitter.value() + "'";
      }
  }

// ***************************** Setters ****************



}
