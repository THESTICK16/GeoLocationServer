package poi.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import poi.core.PointOfInterest;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


//Tested using curl commands in terminal and in web browser
public class PoiSearchHandler implements HttpHandler {

    private PoiManager manager;
    private double lat;
    private double lon;
    private double radius;
    private String q = "no parameter given";

    /**
     * Handles requests containing search parameters
     * @param manager
     */
    public PoiSearchHandler(PoiManager manager) {
        this.manager = manager;
    }

    /**
     * Handle the given request and generate an appropriate response.
     * See {@link HttpExchange} for a description of the steps
     * involved in handling an exchange.
     *
     * @param exchange the exchange containing the request from the
     *                 client and used to send the response
     * @throws NullPointerException if exchange is <code>null</code>
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            handleExchange(exchange);
        } catch (Exception e) {
            System.err.println("Unexpected exception in PoiHandler: " + e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Handle the given request and generate an appropriate response.
     * See {@link HttpExchange} for a description of the steps
     * involved in handling an exchange.
     *
     * @param exchange the exchange containing the request from the
     *                 client and used to send the response
     * @throws NullPointerException if exchange is <code>null</code>
     */
    private void handleExchange(HttpExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        String queryString = exchange.getRequestURI().getQuery();
        String qDecoded = URLDecoder.decode(queryString, StandardCharsets.UTF_8);

        boolean validQuery = parseQuery(qDecoded);

        List<PointOfInterest> results;
        if (validQuery) {
            if (!q.equals("no parameter given")) {
                results = manager.search(lat, lon, radius, q);
            } else {
                results = manager.search(lat, lon, radius);
            }
        }
        else {
            results = new ArrayList<>();
        }

        int responseCode;
        String response;
        if (!validQuery) {
            responseCode = 400;
            response = "Error 400: Bad Request\n";
        }

        else {
            responseCode = 200;
            if (results.size() <= 0) {
                response = "No results found\n";
            }
            else {
                response = manager.resultsString(results);
            }
        }

        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.add("Content-Type", "text/plain");

        byte[] responseBytes = response.getBytes();
        exchange.sendResponseHeaders(responseCode, responseBytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(responseBytes);
        os.close();
    }

    /**
     * parses the search portion of the query and assigns the fields with their proper values
     * @param query the query of the HTTPExchange
     * @return true if the assignment was successful, false if the query format was invalid
     */
    public boolean parseQuery(String query) {
        lat = -1;
        lon = -1;
        radius = -1;

        String[] fields = query.split("&");

        for (int i = 0; i < fields.length; i++) {
            String current = fields[i];

            if (current.contains("lat")) {
//                lat = current.substring(current.indexOf("="));
                lat = Double.parseDouble(current.substring(current.indexOf("=") + 1));
            }

            else if (current.contains("long")) {
                lon = Double.parseDouble(current.substring(current.indexOf("=") + 1));
            }

            else if (current.contains("radius")) {
                radius = Double.parseDouble(current.substring(current.indexOf("=") + 1));
            }

            else if (current.contains("q")) {
                q = current.substring(current.indexOf("=") + 1);
            }
        }

        if (lat == -1 || lon == -1 || radius == -1)
            return false;

        return true;
    }

}
