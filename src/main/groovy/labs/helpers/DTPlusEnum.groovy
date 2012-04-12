package labs.helpers;


import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormat;


/**
* @brief An ENUM helper type that can plus/substract certain number of time units to a DateTime object;
* @author Janusz Kowalczyk
*/
enum DTPlusEnum {

  MILLIS (),
  SECS (),
  MINS (),
  HOURS (),
  DAYS (),
  WEEKS (),
  MONTHS (),
  YEARS ();

  private DateTime dt = null; 
  private DateTimeFormatter dtf = null;
  private String fp = ""; 


  /**
  * @brief Default constructor, it will use default values if no custom DateTime and formatPattern is set
  */
  DTPlusEnum() {
    this.dt = ( this.dt == null) ? new DateTime() : this.dt; // Will use current DateTime if no custom is provided;
    this.fp = ( this.fp.equals("") ) ? this.fp = "yyyy-MM-dd'T'hh:mm:ss.SSZZ" : this.fp; // Will set format patter to a default one if no custom is provided
    this.dtf = ( this.dtf == null ) ? this.dtf = DateTimeFormat.forPattern( "yyyy-MM-dd'T'hh:mm:ss.SSZZ" ) : this.dtf; // will initialize DateTimeFormatter with current format pattern
  }

  /**
  * @brief Setter for custom format pattern.
  * For more details please refer to: 
  * http://joda-time.sourceforge.net/apidocs/org/joda/time/format/DateTimeFormat.html
  *
  * @param fp - A String representation of format pattern
  */
  public void setFormatPattern( String fp ) {
    this.fp = fp; 
    this.dtf = DateTimeFormat.forPattern( fp );
  }


  /**
  * @brief Setter for custom Joda's DateTime value
  * For more details please refer to: 
  * http://joda-time.sourceforge.net/apidocs/org/joda/time/DateTime.html
  *
  * @param dt - A Joda's DateTime object
  */
  public void setDateTime( DateTime dt ) {
    this.dt = dt;
  }
  
  /**
  * @brief Will return current DateTime formatted using current format pattern as String.
  *
  * @return A String representation of current DateTime.
  */
  public String now() {
    return this.dtf.print( this.dt );
  }


  /**
  * @brief A simple function that will add certain number of time units (millisecond, seconds, days, etc.) depending of current ENUM type
  *
  * @param i A number of time units that will be added to current datetime depending od ENUM type
  *
  * @return A String representation of current datetime with number of time units added.
  */
  public String plus( int i ) {
    switch( this ) {
      case MILLIS : return this.dtf.print( this.dt.plusMillis( i ) );
      case SECS   : return this.dtf.print( this.dt.plusSeconds( i ) );
      case MINS   : return this.dtf.print( this.dt.plusMinutes( i ) );
      case HOURS  : return this.dtf.print( this.dt.plusHours( i ) );
      case DAYS   : return this.dtf.print( this.dt.plusDays( i ) );
      case WEEKS  : return this.dtf.print( this.dt.plusWeeks( i ) );
      case MONTHS : return this.dtf.print( this.dt.plusMonths( i ) );
      case YEARS  : return this.dtf.print( this.dt.plusYears( i ) );
    }
  }

  /**
  * @brief A simple function that will substract certain number of time units (millisecond, seconds, days, etc.) depending of current ENUM type
  *
  * @param i A number of time units that will be substracted from to current datetime depending od ENUM type
  *
  * @return A String representation of current datetime with number of time units substracted.
  */
  public String minus( int i ) {
    switch( this ) {
      case MILLIS : return this.dtf.print( this.dt.minusMillis( i ) );
      case SECS   : return this.dtf.print( this.dt.minusSeconds( i ) );
      case MINS   : return this.dtf.print( this.dt.minusMinutes( i ) );
      case HOURS  : return this.dtf.print( this.dt.minusHours( i ) );
      case DAYS   : return this.dtf.print( this.dt.minusDays( i ) );
      case WEEKS  : return this.dtf.print( this.dt.minusWeeks( i ) );
      case MONTHS : return this.dtf.print( this.dt.minusMonths( i ) );
      case YEARS  : return this.dtf.print( this.dt.minusYears( i ) );
    }
  }

}
