package labs.helpers;

import java.lang.Math;



class Distance
{

   
  /**
  * @brief Calculate new random location within a given radius (in km )
  *
  * @param lat - reference lat point
  * @param lon - reference lon point
  * @param radius - radius in km
  *
  * @return A Map of values: lat, lon, formLat, formLon, where form* are formatted represantations of first values
  */
  public static Map getLocInRadius( double lat, double lon, double radius )
  {
    Random r = new Random();
    double dtd = 0.009009009; // 1km ~ 0.009009009 degree -> check http://en.wikipedia.org/wiki/Decimal_degrees
    
    double q = r.nextDouble() * (Math.PI * 2)
    double lat1 = lat + ( ( radius * dtd ) * Math.cos( q ) )
    double lon1 = lon + ( ( radius * dtd ) * Math.sin( q ) )
    String formLat = String.format("%.8g", lat1);
    String formLon = String.format("%.6g", lon1);

    return ["lat":lat1,"lon":lon1,"formLat":formLat,"formLon":formLon];
  }


  /**
  * @brief Calculate new random location within a given bounded distance (in km )
  *
  * @param lat - reference lat point
  * @param lon - reference lon point
  * @param minDistance - min distance in which new random location will be genarated
  * @param maxDistance - max distance in which new random location will be genarated
  *
  * @return A Map of values: lat, lon, formLat, formLon, distance. 
  * Where formX are formatted represantations of first values 
  * and distance is randomly selected distance from given range
  */
  public static Map getLocInRadius( double lat, double lon, double minDistance, double maxDistance )
  {
    Random r = new Random();
    double dtd = 0.009009009; // 1km ~ 0.009009009 degree -> check http://en.wikipedia.org/wiki/Decimal_degrees
    
    Double radius = Randomizer.randRange( minDistance, maxDistance );
    if ( radius == null ) return null; // check wheter there was no problem with generatign random radius

    double q = r.nextDouble() * (Math.PI * 2)
    double lat1 = lat + ( ( radius * dtd ) * Math.cos( q ) )
    double lon1 = lon + ( ( radius * dtd ) * Math.sin( q ) )
    String formLat = String.format("%.8g", lat1);
    String formLon = String.format("%.6g", lon1);

    return ["lat":lat1,"lon":lon1,"formLat":formLat,"formLon":formLon,"distance":radius];
  }




}
