package com.netshell.apps.dbstore.web.rest;

import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    // Base URI the Grizzly HTTP server will listen on
    private static final String BASE_URI = "/store";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this
     * application.
     *
     * @return Grizzly HTTP server.
     */
    private static HttpServer createServer() {
        final ResourceConfig rc = new ResourceConfig().packages("com.netshell.apps.dbstore.web.rest");

        String hostName = null;
        int port = -1;
        try {
            hostName = InetAddress.getLocalHost().getHostName();
            final String portStr = System.getProperty("dbstore.http.port", "8080");
            port = Integer.parseInt(portStr);
        } catch (final UnknownHostException e) {
            logger.warn("Using localhost as could not find machine name due to reason: " + e.getMessage());
            hostName = "localhost";
        }

        final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc, false);
        server.addListener(new NetworkListener("dbstore-http-listener", hostName, port));
        server.getServerConfiguration().addHttpHandler(new CLStaticHttpHandler(Main.class.getClassLoader(), "content/"), "/");

        return server;
    }

    /**
     * Main method.
     *
     * @param args
     * @throws IOException
     */
    public static void main(final String[] args) throws IOException, InterruptedException {

        final HttpServer server = createServer();
        final NetworkListener listener = server.getListener("dbstore-http-listener");
        logger.info
                (String.format("Application started. Please access application using following URL: " +
                        "http://%s:%d/%s", listener.getHost(), listener.getPort(), BASE_URI));
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdownNow));
        Thread.currentThread().join();
    }
}
