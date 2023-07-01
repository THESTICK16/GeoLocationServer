package poi.server;

import poi.core.GeoLocation;
import poi.core.PointOfInterest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The class that stores and manages the POIs
 */
public class PoiManager {

    List<PointOfInterest> pois;

    /**
     * Constructs the PoiManager object
     * @param placesFilePath the String containing the file path that contains the places data
     */
    public PoiManager(String placesFilePath) {
        pois = loadPoiList(placesFilePath);
    }

    /**
     * loads the POIs from a file to a list
     * @param placesFilePath the String path of the file to load the places from
     * @return a list containing the places data
     */
    private List<PointOfInterest> loadPoiList(String placesFilePath) {
        ArrayList places = new ArrayList<>();
        try {
            Scanner placesScan = new Scanner( new File(placesFilePath));
            while (placesScan.hasNextLine()) {
                String place = placesScan.nextLine();
                String[] placeData = place.split("\t");
                PointOfInterest poi = new PointOfInterest(placeData[0], placeData[1],
                        placeData[2], Double.parseDouble(placeData[3]), Double.parseDouble(placeData[4]));
                places.add(poi);
            }
            placesScan.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return places;
    }

    /**
     * searches pois for the POI at the given ID number
     * @param id the id of the PointOfInterest to be searched for
     * @return a list containing the POI that matches the search parameters
     */
    public List<PointOfInterest> search(int id) {

        ArrayList results = new ArrayList<PointOfInterest>();

        if (id >= pois.size()) {
            return results; //make it so returning an empty list will throw the proper error code
        }

        results.add(pois.get(id));
        return results;
    }

    /**
     * Searches pois for PointOfInterests within the given radius of the given point that match the type description
     * @param lat the latitude of the point to search from
     * @param lon the longitude of the point to search from
     * @param radius the radius to search within from the given point (the maximum distance away)
     * @return a list containing all PointOfInterests matching the search criteria
     */
    public List<PointOfInterest> search(double lat, double lon, double radius) {
        ArrayList<PointOfInterest> results = new ArrayList<>();
        GeoLocation searchLocation = new GeoLocation(lat, lon);

        for (PointOfInterest poi: pois) {
            if (poi.distanceTo(searchLocation) <= radius) {
                results.add(poi);
            }
        }

        return results;
    }

    /**
     * Searches pois for PointOfInterests within the given radius of the given point that match the type description
     * @param lat the latitude of the point to search from
     * @param lon the longitude of the point to search from
     * @param radius the radius to search within from the given point (the maximum distance away)
     * @param q the typee parameter to match in the search
     * @return a list containing all PointOfInterests matching the search criteria
     */
    public List<PointOfInterest> search(double lat, double lon, double radius, String q) {
        ArrayList<PointOfInterest> results = new ArrayList<>();
        GeoLocation searchLocation = new GeoLocation(lat, lon);

        for (PointOfInterest poi: pois) {
            if (poi.getType().contains(q) && poi.distanceTo(searchLocation) <= radius) {
                results.add(poi);
            }
        }

        return results;
    }

    /**
     * For testing purposes only
     * @return a string representation of pois
     */
    @Override
    public String toString() {
        return "PoiManager{" +
                "pois=" + pois.toString() +
                '}';
    }

    /**
     * Returns the list of results in the format provided by https://cs.plu.edu/courses/protected/cs390/2022s/pa/pa03_geolocation.html
     * @param resultsList the list to be formatted as a String
     * @return the String representation of the given list
     */
    public String resultsString(List<PointOfInterest> resultsList) {
        StringBuilder sb = new StringBuilder();

        for (PointOfInterest point : resultsList) {
            sb.append("(" + (pois.indexOf(point) + 1) + ") name=\"" + point.getName() + "\" addr=\"" + point.getAddress()
                + "\" tags=\"" + point.getType() + "\" location=\"(" + point.getLatitude() + "," + point.getLongitude()
                + ")\"");
            sb.append("\n");
        }

        return sb.toString();
    }

    public List<PointOfInterest> getPois() { //FIXME for testing purposes only!
        return pois;
    }

    /**
     * Used to determine if an int is a valid index for the list of PointOfInterests
     * @param index the index to test validity for
     * @return true if the index will return a valid entry in pois, false if less than zero or greater the last valid index for pois
     */
    public boolean isIndexValid(int index) {
        if (index < pois.size() && index >= 0)
            return true;
        return false;
    }
}
