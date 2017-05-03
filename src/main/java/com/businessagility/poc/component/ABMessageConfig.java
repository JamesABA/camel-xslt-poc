package com.businessagility.poc.component;

import com.businessagility.poc.types.ABRequest;
import com.businessagility.poc.types.ABResponse;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * ABMessageConfig.java
 *
 * Spring loaded singleton to configure messages sent to and received from a remote tier
 *
 * configureRequest()
 * Takes a Camel exchange (in XML String form), some properties defining BridgeServices
 *  to be called, and converts the body to an ABRequest object
 *
 * configureResponse()
 * Takes an ABResponse object and moves the payload to the Camel exchange. Any errors received
 *  are added to a header for velocity to insert into the response.
 *
 */
public class ABMessageConfig {

    private Logger _logger = LoggerFactory.getLogger(ABMessageConfig.class);
    private Properties configProps;

    public ABMessageConfig(String path) throws IOException {

        configProps = new Properties();
        try {
            configProps.load(Files.newInputStream(Paths.get(path+"/serviceconfig.properties")));
        }
        catch (IOException io) {
            _logger.error("Failed to load service configuration file.");
            throw io;
        }
    }

    public void configureRequest(Exchange exchange) {

        ABRequest request = new ABRequest();

        //Take the inbound message from the Camel Exchange
        Message message = exchange.getIn();

        //Grab our service name from the 'ABServiceName' header (set in the route)
        String serviceName = (String)message.getHeader("ABServiceName");

        //Set the service list and username according to the service configuration properties
        request.set_serviceList(configProps.getProperty(serviceName+"_services"));
        request.set_username(configProps.getProperty(serviceName+"_user"));

        //Extract the body from the exchange message and insert into the request object
        request.set_body(message.getBody(String.class));

        //Put the ABRequest object into the Exchange so it's ready to be converted to JSON.
        exchange.getIn().setBody(request, ABRequest.class);
    }

    public void configureResponse(Exchange exchange) {

        //Take the inbound message from the exchange
        ABResponse response = exchange.getIn().getBody(ABResponse.class);

        //Set the 'ABErrors' header in the camel message to contain any errors we've received
        exchange.getIn().setHeader("ABErrors", response.getErrors());

        //If we have a body, make sure it doesn't contain an xml preamble, and put it into the exchange
        if (response.getBody() != null) {
            exchange.getIn().setBody(response.getBody().replaceAll("<\\?xml.*\\?>", ""), String.class);
        }
    }

}
