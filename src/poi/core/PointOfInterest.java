package poi.core;

public class PointOfInterest {

    private String name;
    private String address;
    private String type;
    private  GeoLocation geoLocation;
    private double latitude;
    private double longitude;

    /**
     * contains the data for a single point of interest
     * @param name the name of the POI
     * @param address the address of the POI
     * @param type the type(s) of the POI
     * @param latitude the latitude of the POI
     * @param longitude the longitude of the POI
     */
    public PointOfInterest(String name, String address, String type, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.type = type;
        geoLocation = new GeoLocation(latitude, longitude);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getType() {
        return type;
    }

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public double getLatitude() {
//        return latitude;
        return geoLocation.getLatitude();
    }

    public double getLongitude() {
//        return longitude;
        return geoLocation.getLongitude();
    }

    public double distanceTo(GeoLocation to) {
        return geoLocation.distanceFrom(to);
    }

    @Override
    public String toString() {
        return "PointOfInterest{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", type='" + type + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
