package poi.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import poi.core.PointOfInterest;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;


//Tested using curl commands in terminal and in web browser
public class PoiHandler implements HttpHandler {

    private PoiManager manager;

    /**
     * Handles requests given the id number of the POI to search for
     * @param manager the PoiManager
     */
    public PoiHandler(PoiManager manager) {
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

        String response;
        List<PointOfInterest> results;
        int idNum = parsePath(path);
        int responseCode;
        if (manager.isIndexValid(idNum)) {
            responseCode = 200;
            results = manager.search(idNum);
            response = manager.resultsString(results);
        }
        else {
            responseCode = 404;
            response = "Error 404: Place not found\n";
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
     * parses and returns the id contained in the HttpExchange path
     * @param path the HttpExchange path
     * @return the id of the POI to request
     */
    public int parsePath(String path) {
        return Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
    }

}
