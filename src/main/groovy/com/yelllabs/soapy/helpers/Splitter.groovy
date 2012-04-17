package com.yelllabs.soapy.helpers;

enum Splitter
{
  SPACE ( " ", " " ),
  COMA ( ",", "" ),
  COLON ( ":", "" ),
  SEMICOLON ( ";", "" ),
  DOT ( ".", "" ),
  COMA_IN_DOUBLE_QUOTES(",",",","\""),
  SPACE_IN_QOUTES ( "\' \'" , "\'" ),
  SPACE_IN_DOUBLE_QOUTES ( "\" \"", "\"" ),
  SPACE_IN_ROUND_BRACKETS ( ") (", ")", "(" ),
  SPACE_IN_SQUARE_BRACKETS ( "] [", "]", "[" ),
  SPACE_IN_CURLY_BRACKETS ( "} {" , "}" , "{" ),
  SPACE_IN_BRACKETS ( "> <", ">" , "<" );

  private String splitter = ""; // 
  private String[] additionalCharsToReplace = null; // when for example values are separeted with double quotes, ie.: "abc" "def" ...
                                                 // then this variable is a character that is placed at the begining and end of string values 
                                                 // that has to be replaced to get clean results; 
  Splitter( String splitter, String... additionalCharsToReplace )
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
