package poi.core;

/**
 * This class stores information about a location on Earth.  Locations are
 * specified using latitude and longitude.  The class includes a method for
 * computing the distance between two locations.
 * 
 * @author Stuart Reges, David Wolff
 */
public class GeoLocation {
	
    public static final double RADIUS = 3963.1676;  // Earth radius in miles

    private double latitude;
    private double longitude;

    /**
     * Constructs a GeoLocation object with given latitude and longitude
     * 
     * @param theLatitude initial latitude
     * @param theLongitude initial longitude
     */
    public GeoLocation(double theLatitude, double theLongitude) {
        latitude = theLatitude;
        longitude = theLongitude;
    }

    /**
     * Returns the latitude of this GeoLocation
     * @return the latitude of this GeoLocation
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Returns the longitude of this GeoLocation
     * @return the longitude of this GeoLocation
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Returns a String representation of this GeoLocation
     * @return a String representation of this GeoLocation
     */
    public String toString() {
        return "latitude: " + latitude + ", longitude: " + longitude;
    }

    /**
     * Returns the distance in miles between this GeoLocation and the given
     * other GeoLocation.
     * 
     * @param other other location to measure distance
     * @return the (spherical) distance in miles between this and other
     */
    public double distanceFrom(GeoLocation other) {
        double lat1 = Math.toRadians(latitude);
        double long1 = Math.toRadians(longitude);
        double lat2 = Math.toRadians(other.latitude);
        double long2 = Math.toRadians(other.longitude);

        double havSinLat = Math.sin( (lat1 - lat2) * 0.5 );
        double havSinLong = Math.sin( (long1 - long2) * 0.5 );
        double theSqrt = havSinLat * havSinLat + Math.cos(lat1) * Math.cos(lat2) * havSinLong * havSinLong;
        return 2.0 * RADIUS * Math.asin( Math.sqrt(theSqrt) );
    }
}
