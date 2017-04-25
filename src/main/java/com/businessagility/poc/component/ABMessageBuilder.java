package com.businessagility.poc.component;

import com.businessagility.poc.types.ABRequest;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * ABMessageBuilder.java
 *
 * Takes a Camel message exchange (in XML String form), some properties defining BridgeServices
 *  to be called, and converts the body to an ABRequest object
 *
 *
 *
 */
public class ABMessageBuilder {

    private Logger _logger = LoggerFactory.getLogger(ABMessageBuilder.class);
    private static String configFolder;

    @Autowired
    public static void setConfigFolder(String configFolder) {
        ABMessageBuilder.configFolder = configFolder;
    }

    public void process(Exchange exchange) {

        Message message = exchange.getIn();
        String body = message.getBody(String.class);
        String serviceName = (String)message.getHeader("ABServiceName");

        //TODO: Abstract properties retrieval away from here and only do on startup?
        Properties props = new Properties();
        try {
            props.load(Files.newInputStream(Paths.get(configFolder+"/serviceconfig.properties")));
        }
        catch (IOException io) {
            _logger.error("Failed to load service configuration file.");
            exchange.setException(io);
        }

        ABRequest request = new ABRequest();

        request.set_serviceList(props.getProperty(serviceName+"_services"));
        request.set_username(props.getProperty(serviceName+"_user"));
        request.set_body(body);

        exchange.getIn().setBody(request, ABRequest.class);

    }

}
