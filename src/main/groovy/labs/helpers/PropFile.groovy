package labs.helpers;

class PropFile
{
  
  private File file = null;
  private def propHolder = null;
  private def props = new java.util.Properties();
  private def log = null;

  PropFile( String filePath, def destinationPropertiesHolder, def log )
  {
    try
    {
      this.propHolder = destinationPropertiesHolder;
      this.file = new java.io.File( filePath )
      this.log = log;
    }
    catch(IOException e)
    {
      System.err.println("Caught IOException: " + e.getMessage());
    }
  }

  public def readAllValues()
  {
      this.props.load( new java.io.FileReader( this.file ))
      return this.props;
  }

  /**
  * Saves all found property values into a destination properties holder
  */
  public void saveToHolder()
  {
    if ( this.props != null && props.size() > 0 )
    {
      this.props.each
      {
        key, val ->
            this.setProperty( key, val ) 
      }
    }
  }


  private void setProperty( String key, String val)
  {
      if( key != null && val != "" && val.size() > 0 ) 
      {
            //log.info key + " / " + val
            this.propHolder.setPropertyValue( key , val )
            log.info( "Changed " + key + " property value to [" + val + "]" )
      }
  }
}
