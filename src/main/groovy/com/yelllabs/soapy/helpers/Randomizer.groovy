package com.yelllabs.soapy.helpers;


import nl.flotsam.xeger.Xeger

/**
* @brief A helper class that consist of various methods that generate random values (int, double, string etc);
* @author Janusz Kowalczyk (38115ja)
* @lastUpdate 2011-12-11 
* @changeLog 2011-12-11 randString(X) methods added
*/
class Randomizer {

  /**
  * @brief Randomly selects int value from specific range
  *
  * @param Min - min range value
  * @param Max - max range value
  *
  * @return A random int from specific a range. Return NULL when Min < Max
  */
  public static Integer randRange(int Min, int Max)
  {
    return ( Min <= Max ) ? ( Min + (int)(Math.random() * ((Max - Min) + 1)) ) : null;
  }

  /**
  * @brief Randomly selects double value from a specific range
  *
  * @param Min - min range value
  * @param Max - max range value
  *
  * @return A random double from specific a range. Return NULL when: Min<Max && Min>=0 && Min<1 && Max>0 && Max<=1 
  */
  public static Double randRange( double Min, double Max )
  {
    return ( Min<=Max ) ? Min + (double)(Math.random() * ((Max - Min) )) : null;
    //return ( Min<Max && Min>=0 && Min<1 && Max>0 && Max<=1 ) ? ( Min + (double)(Math.random() * ((Max - Min) )) ) : null;
  }



  /**
  * @brief Returns randomly generated alphanumeric string with given lenght
  * @return A random String;
  */
  public static String randString( int minLength, int maxLength ) {
    String regex = "";
    if ( minLength > 1 && minLength < maxLength && maxLength < 255 ) {
      regex = "[a-zA-Z0-9]{"+minLength+","+maxLength+"}c";
    } else {
      regex = "[a-zA-Z0-9]{4,10}c";
    }
    return this.randString( regex );
  }


  /**
  * @brief Generates random string with variable length between 4 and 16 chars.
  * @return A random String;
  */
  public static String randString() {
    Xeger generator = new Xeger( "[a-zA-Z0-9]{4,16}c" );
    return generator.generate();
  }



  /**
  * @brief Return randomly generated string using a Xeger library, that works
  * as a reverse RegExp;
  * @return A random String;
  */
  public static String randString( String regex ) {
    Xeger generator = new Xeger( regex );
    return generator.generate();
  }


 }
