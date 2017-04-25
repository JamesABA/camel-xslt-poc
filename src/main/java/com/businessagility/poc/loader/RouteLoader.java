package com.businessagility.poc.loader;

import org.apache.camel.model.RoutesDefinition;
import org.apache.camel.spring.SpringCamelContext;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

/**
 * Created by JamesAyling on 10/03/2017.
 *
 * Iterates around a specified folder and updates the camel context with any routes found.
 *  Looks for files with an extension of .xml or .route
 *
 *  Update - if the path points to a file, just load that file (handy for testing)
 */
public class RouteLoader {

    private SpringCamelContext context;
    private Logger logger;

    public RouteLoader(SpringCamelContext context, Logger logger, String routeFolder) {
        this.context = context;
        this.logger = logger;

        loadRoutes(routeFolder);
    }

    public void loadRoutes(String routeFolder) {
        Path routePath = Paths.get(routeFolder);

        if (!Files.exists(routePath)) {
            logger.error("Failed to read from route configuration location <"
                    + routeFolder +"> at ["+routePath.toAbsolutePath()+"]. It doesn't seem to exist.");
            return;
        }

        if (!Files.isDirectory(routePath)) {
            addRoute(routePath);
            return;
        }

        /* TODO: Agree error handling strategy. Currently, if a route file fails, we just continue on with all other files
            this might not fit a fail fast error handling strategy.. */
        try {
            DirectoryStream<Path> stream =
                    Files.newDirectoryStream(routePath, "*.{xml,route}");

            for (Path file : stream) {
                addRoute(file);
            }
        }
        catch (IOException | DirectoryIteratorException x) {
            logger.error("Failed to read from route configuration location <"
                    + routeFolder + ">. Exception was " + x.getMessage());
        }
    }

    private void addRoute(Path file) {

        try {
            logger.debug("Attempting to load route from file " + file);

            InputStream stream = Files.newInputStream(file);
            RoutesDefinition routes = context.loadRoutesDefinition(stream);
            context.addRouteDefinitions(routes.getRoutes());

        } catch (Exception e) /*Sorry.. Camel throws a base type in 'loadRoutesDefinition' */ {
            logger.warn("Failed to load route from file <" + file +
                    "> with exception " + e.getMessage());
        }
    }

}
