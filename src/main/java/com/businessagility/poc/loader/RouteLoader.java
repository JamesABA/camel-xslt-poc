package com.businessagility.poc.loader;

import org.apache.camel.model.RoutesDefinition;
import org.apache.camel.spring.SpringCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

/**
 * RouteLoader.java
 *
 * Spring activated singleton used for configuration
 *
 * Iterates around the specified folder and updates the camel context with any routes found.
 *  Looks for files with an extension of .xml or .route
 *
 * Update - if the path points to a file, just load that file (handy for testing)
 *
 * Note - TODO: Might be worth making this recursive so we can traverse a tree, lots of routes on one level
 *  would get messy..
 */
public class RouteLoader {

    private SpringCamelContext context;
    private Logger logger = LoggerFactory.getLogger(RouteLoader.class);

    public RouteLoader(SpringCamelContext context, String routeFolder) {
        this.context = context;
        loadRoutes(routeFolder);
    }

    private void loadRoutes(String routeFolder) {
        Path routePath = Paths.get(routeFolder);

        if (!Files.exists(routePath)) {
            logger.error("Failed to read from route configuration location <"
                    + routeFolder + "> at [" + routePath.toAbsolutePath() + "]. It doesn't seem to exist.");
            return;
        }

        if (!Files.isDirectory(routePath)) {
            addRoute(routePath);
            return;
        }

        // TODO: This should probably fail very loudly if things go wrong as routes are kinda important.
        try {
            DirectoryStream<Path> stream =
                    Files.newDirectoryStream(routePath, "*.{xml,route}");

            for (Path file : stream) {
                addRoute(file);
            }
        } catch (IOException | DirectoryIteratorException x) {
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
