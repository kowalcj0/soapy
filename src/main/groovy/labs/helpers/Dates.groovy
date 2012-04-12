package labs.helpers;

import java.util.Calendar;

enum Dates
{
  TODAY ( Calendar.DAY_OF_YEAR ),
  TOMMOROW (  ),
  YESTERDAY (  ),
  HOUR_AGO (  ),
  HOUR_LATER (  ),
  TWO_DAYS_AGO (  ),
  TWO_DAYS_LATER (  ),
  WEEK_AGO (  ),
  WEEK_LATER (  ),
  MONTH_AGO (  ),
  MONTH_LATER (  ),
  YEAR_AGO (  ),
  YEAR_LATER (  );

  private 

  Dates( int value )
  {
    this.splitter = splitter;
    this.additionalCharsToReplace = additionalCharsToReplace;
  }
  
  public String value()
  {
    return this.splitter;
  }

  public String[] charsToReplace()
  {
    return this.additionalCharsToReplace;
  }
}
