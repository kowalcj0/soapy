package com.yelllabs.soapy.helpers;


/**
* A helper class to:<br>
*       <ul>
*       <li>get random location within given radius from a place
*       <li>calculate distance between two points in various ways
*       </ul>
*
* @author Janusz Kowalczyk (jk)
* @created 2012-12-13 jk
* @updated 2012-12-13 jk
*/
class Distance
{

   
  /**
  * Calculate new random location within a given radius (in km )
  *
  * @SOURCE <a href="http://en.wikipedia.org/wiki/Decimal_degrees">http://en.wikipedia.org/wiki/Decimal_degrees</a>
  *
  * @param double lat - reference lat point
  * @param double lon - reference lon point
  * @param double radius - radius in km
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
  * Calculate new random location within a given bounded distance (in km )
  *
  * @SOURCE <a href="http://en.wikipedia.org/wiki/Decimal_degrees">http://en.wikipedia.org/wiki/Decimal_degrees</a>
  *
  * @param double lat - reference lat point
  * @param double lon - reference lon point
  * @param double minDistance - min distance in which new random location will be genarated
  * @param double maxDistance - max distance in which new random location will be genarated
  *
  * @return A Map of values: lat, lon, formLat, formLon, distance. 
  * Where formLat/formLon are formatted represantations of the first values 
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



    /**
    * This function computes the distance in km, miles, or nautical miles 
    * between two points on map.
    * 
    * @SOURCE <a href="http://www.dzone.com/snippets/distance-calculation-using-3">http://www.dzone.com/snippets/distance-calculation-using-3</a>
    * @author ZIPCodeWorld.com
    * 
    * @param Double Latitude of the first place
    * @param Double Longitude of the first place
    * @param Double Latitude of the second place
    * @param Double Longitude of the second place
    * @param String unit distance unit: K = Km, M = miles, N = nautical miles
    *
    * @return Distance between two places in desired distance unit
    */
    public static double computeDistanceBetween(Double lat1, Double lon1, Double lat2, Double lon2, String unit) {
      double theta = lon1 - lon2;
      double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
      dist = Math.acos(dist);
      dist = rad2deg(dist);
      dist = dist * 60 * 1.1515;
      if (unit == "K") {
        dist = dist * 1.609344;
      } else if (unit == "N") {
        dist = dist * 0.8684;
        }
      return (dist);
    }

    /**
    * This function converts decimal degrees to radians 
    *
    * @SOURCE <a href="http://www.dzone.com/snippets/distance-calculation-using-3">http://www.dzone.com/snippets/distance-calculation-using-3</a>
    * @author ZIPCodeWorld.com
    *
    * @param deg Decimal degrees that will be converted to radians
    *
    * @return Decimal degrees converted to radians
    */
    public static double deg2rad(double deg) {
      return (deg * Math.PI / 180.0);
    }

    /**
    * This function converts radians to decimal degrees 
    *
    * @SOURCE <a href="http://www.dzone.com/snippets/distance-calculation-using-3">http://www.dzone.com/snippets/distance-calculation-using-3</a>
    * @author ZIPCodeWorld.com
    *
    * @param rad Radinas that 
    *
    * @return Radians converted to Decimal degrees
    */
    public static double rad2deg(double rad) {
      return (rad * 180.0 / Math.PI);
    }


    /**
    * This uses the 'haversine' formula to calculate the great-circle 
    * distance between two points - that is, the shortest distance over the 
    * earth's surface - giving an 'as-the-crow-flies' distance between the 
    * points (ignoring any hills, of course!).
    * 
    * @author Chris Veness 2002-2012
    * @URL <a href="http://www.movable-type.co.uk/scripts/latlong.html">http://www.movable-type.co.uk/scripts/latlong.html</a>
    * 
    * @param double lat1 Latitude of the first place
    * @param double Longitude of the first place
    * @param double Latitude of the second place
    * @param double Longitude of the second place
    *
    * @return Disantance between two places in Km
    */
    public static double computeHaversineDistanceBetween(double lat1, double lon1, double lat2, double lon2){
        def R = 6371; // avarage Earth's radius in km
        def dLat = Math.toRadians(lat2-lat1);
        def dLon = Math.toRadians(lon2-lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        
        def a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * 
                Math.cos(lat1) * Math.cos(lat2); 
        def c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
        def d = R * c;
        return d;
    }

}
