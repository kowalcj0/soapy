package labs.helpers;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormat;

import org.apache.log4j.Logger;



class DateTimeHelper
{

	private Map datetime = [:]; // an empty Map to store all the datetimes
  private DateTimeFormatter fmt = null; 
  private DateTime dt = null; 
  private int plus = 3;
  private Logger log = null;

  /**
  * @brief An empty constructor;
  */
  DateTimeHelper( Logger log ) {
    this.dt = new DateTime();
    this.log = log;
  }


  public void update() {
    this.dt = new DateTime();
  }


  /**
  * @brief Return a datetime Map with string represantations of a dateTime in format 2010-05-19T13:18:00.249875+01:00 
  * Uses specific DateTime to generate all the needed dates;
  *
  * @param date - A DateTime object. If null the current DateTime is used;
  *
  * @return A Map with fields:  now,plusMinute,minusMinute,plusHour,minusHour,plusDay,minusDay,plusWeek,minusWeek,plusMonth,minusMonth
  */
  public Map getDateTimeWTimeZone( DateTime date ) {
	  this.parse( date, "yyyy-MM-dd'T'HH:mm:ss.SSSZZ" ); 
    return datetime;
  }


  /**
  * @brief Return a datetime Map with string represantations of a dateTime in format 2010-05-19T13:18:00.249875+01:00 
  * Uses current DateTime to generate all the needed dates;
  * 
  * @return  A Map with fields:  now,plusMinute,minusMinute,plusHour,minusHour,plusDay,minusDay,plusWeek,minusWeek,plusMonth,minusMonth
  */
  public Map getDateTimeWTimeZone() {
    return getDateTimeWTimeZone( (this.dt == null) ? new DateTime() : this.dt );
  }


  /**
  * Returns string represantation of currrent dateTime in format 2011-01-25T18:24:04.26537Z 
  *
  */
  public Map getZuluTime( DateTime date ) {
    this.parse( date, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" ); // parses the date
    return datetime;
  }

  /**
  * @brief Return datetime Map using current DateTime
  *
  * @return A Map with fields: now,plusMinute,minusMinute,plusHour,minusHour,plusDay,minusDay,plusWeek,minusWeek,plusMonth,minusMonth
  */
  public Map getZuluTime() {
    return getZuluTime( (this.dt == null) ? new DateTime() : this.dt );
  }


  public Map getWCustomFormat( String customFormatPattern ) {
    this.parse( this.dt, customFormatPattern );
    return datetime;
  }


  /**
  * @brief 
  *
  * @param dt - A DateTime object
  * @param formatPattern - JODA's specific time formatter. For more info.: http://joda-time.sourceforge.net/api-release/org/joda/time/format/DateTimeFormat.html
  *
  */
  private void parse( DateTime tmpdt, String formatPattern ) {

     this.fmt = DateTimeFormat.forPattern( formatPattern );
  	 tmpdt = ( tmpdt == null ) ? new DateTime() : tmpdt; // get current date or set it to specific one

     this.datetime.now = fmt.print( tmpdt );
     this.datetime.plusMilli = fmt.print( tmpdt.plusMillis(1) );
     this.datetime.minusMilli = fmt.print( tmpdt.minusMillis(1) );
     this.datetime.plusSecond = fmt.print( tmpdt.plusSeconds(1) );
     this.datetime.minusSecond = fmt.print( tmpdt.minusSeconds(1) );
     this.datetime.plusMinute = fmt.print( tmpdt.plusMinutes(1) );
     this.datetime.minusMinute = fmt.print( tmpdt.minusMinutes(1) );
     this.datetime.plusHour = fmt.print( tmpdt.plusHours(1) );
     this.datetime.minusHour = fmt.print( tmpdt.minusHours(1) );
     this.datetime.plusDay = fmt.print( tmpdt.plusDays(1) );
     this.datetime.minusDay = fmt.print( tmpdt.minusDays(1) );
     this.datetime.plusWeek = fmt.print( tmpdt.plusWeeks(1) );
     this.datetime.minusWeek = fmt.print( tmpdt.minusWeeks(1) );
     this.datetime.plusMonth = fmt.print( tmpdt.plusMonths(1) );
     this.datetime.minusMonth = fmt.print( tmpdt.minusMonths(1) );
     this.datetime.plusYear = fmt.print( tmpdt.plusYears(1) );
     this.datetime.minusYear = fmt.print( tmpdt.minusYears(1) );
      
     if ( this.plus > 1 ) {
       this.datetime.plusMillis = this.getPlusDates( tmpdt, DTPlusEnum.MILLIS, formatPattern, this.plus );
       this.datetime.minusMillis = this.getMinusDates( tmpdt, DTPlusEnum.MILLIS, formatPattern, this.plus );
       this.datetime.plusSeconds = this.getPlusDates( tmpdt, DTPlusEnum.SECS, formatPattern, this.plus );
       this.datetime.minusSeconds = this.getMinusDates( tmpdt, DTPlusEnum.SECS, formatPattern, this.plus );
       this.datetime.plusMinutes = this.getPlusDates( tmpdt, DTPlusEnum.MINS, formatPattern, this.plus );
       this.datetime.minusMinutes = this.getMinusDates( tmpdt, DTPlusEnum.MINS, formatPattern, this.plus );
       this.datetime.plusHours = this.getPlusDates( tmpdt, DTPlusEnum.HOURS, formatPattern, this.plus );
       this.datetime.minusHours = this.getMinusDates( tmpdt, DTPlusEnum.HOURS, formatPattern, this.plus );
       this.datetime.plusDays = this.getPlusDates( tmpdt, DTPlusEnum.DAYS, formatPattern, this.plus );
       this.datetime.minusDays = this.getMinusDates( tmpdt, DTPlusEnum.DAYS, formatPattern, this.plus );
       this.datetime.plusWeeks = this.getPlusDates( tmpdt, DTPlusEnum.WEEKS, formatPattern, this.plus );
       this.datetime.minusWeeks = this.getMinusDates( tmpdt, DTPlusEnum.WEEKS, formatPattern, this.plus );
       this.datetime.plusMonths = this.getPlusDates( tmpdt, DTPlusEnum.MONTHS, formatPattern, this.plus );
       this.datetime.minusMonths = this.getMinusDates( tmpdt, DTPlusEnum.MONTHS, formatPattern, this.plus );
       this.datetime.plusYears = this.getPlusDates( tmpdt, DTPlusEnum.YEARS, formatPattern, this.plus );
       this.datetime.minusYears = this.getMinusDates( tmpdt, DTPlusEnum.YEARS, formatPattern, this.plus );

     } else {
        // then return 
       this.datetime.plusMillis = [[ 1 : fmt.print( tmpdt.plusMillis(1) ) ]];
       this.datetime.minusMillis = [[ 1 : fmt.print( tmpdt.minusMillis(1) ) ]];
       this.datetime.plusSeconds = [[ 1 : fmt.print( tmpdt.plusSeconds(1) ) ]];
       this.datetime.minusSeconds = [[ 1 : fmt.print( tmpdt.minusSeconds(1) ) ]];
       this.datetime.plusMinutes = [[ 1 : fmt.print( tmpdt.plusMinutes(1) ) ]];
       this.datetime.minusMinutes = [[ 1 : fmt.print( tmpdt.minusMinutes(1) ) ]];
       this.datetime.plusHours = [[ 1 : fmt.print( tmpdt.plusHours(1) ) ]]; 
       this.datetime.minusHours = [[ 1 : fmt.print( tmpdt.minusHours(1) ) ]]; 
       this.datetime.plusDays = [[ 1 : fmt.print( tmpdt.plusDays(1) ) ]]; 
       this.datetime.minusDays = [[ 1 : fmt.print( tmpdt.minusDays(1) ) ]]; 
       this.datetime.plusWeeks = [[ 1 : fmt.print( tmpdt.plusWeeks(1) ) ]]; 
       this.datetime.minusWeeks = [[ 1 : fmt.print( tmpdt.minusWeeks(1) ) ]]; 
       this.datetime.plusMonths = [[ 1 : fmt.print( tmpdt.plusMonths(1) ) ]]; 
       this.datetime.minusMonths = [[ 1 : fmt.print( tmpdt.minusMonths(1) ) ]]; 
       this.datetime.plusYears = [[ 1 : fmt.print( tmpdt.plusYears(1) ) ]]; 
       this.datetime.minusYears = [[ 1 : fmt.print( tmpdt.minusYears(1) ) ]]; 
   
     }

    }


  public void setPlusValue( int plus ) {
    this.plus = ( plus >= 1 && plus <= 50 ) ? plus : Randomizer.randRange( 1, 50 );
  }


  private List getPlusDates( DateTime dt, DTPlusEnum dtpe, String formatPattern, int times ) {
    List tmp = [];
    dtpe.setDateTime( dt );
    dtpe.setFormatPattern( formatPattern );
    for( int i = 1; i <= times; i++ ) {
      tmp.add( ""+dtpe.plus( i )+"" );
    }
    return tmp;
  }

  private List getMinusDates( DateTime dt, DTPlusEnum dtpe, String formatPattern, int times ) {
    List tmp = [];
    dtpe.setDateTime( dt );
    dtpe.setFormatPattern( formatPattern );
    for( int i = 1; i <= times; i++ ) {
      tmp.add( ""+dtpe.minus( i )+"" );
    }
    return tmp;
  }

}
