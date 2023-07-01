package poi.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Main {

    private static final int PORT = 8000;

    public static void main(String[] args) throws IOException {
        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
        } catch(IOException e ) {
            System.err.println("Unable to start web server on port " + PORT);
            System.err.println(e.getMessage());
            return;
        }
        server.setExecutor(Executors.newCachedThreadPool());

        PoiManager manager = new PoiManager("places.txt");
        server.createContext("/poi", new PoiHandler(manager));
        server.createContext("/poi/search", new PoiSearchHandler(manager));

        System.out.println("Starting web server on port: " + PORT);
        server.start();
    }

}
