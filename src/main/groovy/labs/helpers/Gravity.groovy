package labs.helpers;


import labs.helpers.Randomizer;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormat;


/**
* @brief Simple class to generate gravity triplet
* @author Janusz Kowalczyk 38115ja
* @created 2011-11-23
* @lastUpdate 2011-11-23
*/
public class Gravity {
  
  private Double x = 0;
  private Double y = 0;
  private Double z = 0;
  private Float threshold = new Float(3);
 
  /**
  * @brief Default contstructor
  */
  public Gravity() {
    this.shuffle();
  }

  /**
  * @brief Setter for threshold used by shuffleSafe() method.
  *
  * @param threshold a Float
  */
  public void setThreshold( Float threshold) {
    this.threshold = threshold;
  }

  /**
  * @brief Return gravity triplet as Double values;
  *
  * @return A Map with fields: x,y,z
  */
  public Map get() {
    return [ "x":x,"y":y,"z":z ];
  }

  /**
  * @brief Shuffles the values until Math.sqrt(x * x + y * y + z * z) < threshold
  */
  public void shuffleSafe() {
   while ( Math.sqrt(x * x + y * y + z * z) > threshold ) {
    this.shuffle();
   }
  }

  /**
  * @brief Shuffles the values until Math.sqrt(x * x + y * y + z * z) > threshold
  */
  public void shuffleNotSafe() {
   while ( Math.sqrt(x * x + y * y + z * z) < threshold ) {
    this.shuffle();
   }
  }


  /**
  * @brief Shuffles the values to get values: -3.0 > x,y,z < 3.0 
  */
  public void shuffle() {
    this.x = Randomizer.randRange( -3.0, 3.0 );
    this.y= Randomizer.randRange( -3.0, 3.0 );
    this.z= Randomizer.randRange( -3.0, 3.0 );
  }

}
