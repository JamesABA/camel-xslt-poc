# camel-xslt-poc
A proof of concept using cxf web services, ActiveMQ, camel routing and xslt with minimal lines of code

Updates :
- Now Tomcat compliant
- Externally configurable in '/AB2/configuration/override.properties'
- Example external config held in the project folder - AB2.zip

Routing

    Route1 - deployed by default
    CXF consumer
    XSLT transformer to map webservice body to a simple structure (GX)
    File component to save transformed XML output
    Velocity template to generate webservice response
    Logging component used between each route step (DEBUG level)

    Route2 - used in test mode only (avoid AMQ broker dependency)
    JMS inbound queue
    XSLT transformer to map message body to a simple structure (GX)
    File component to save transformed XML output
    Logging component used between each route step (DEBUG level)
    CBR filter to pass on JMS to queue based on XML element content
    JMS response queue



Logging

    SLF4j with console and rollingfile appenders
        Configured in 'src/main/resources/log4j2.xml'
  
    Set the 'Root' attribute to debug if you want to see detailed runtime info


Running the code 

  Standard Maven goals with embedded jetty server
  
    mvn compile
      will compile the code
      
    mvn clean
      will scrub the target folder of all generated and compiled code
    
    mvn install 
      will resolve dependencies, compile, and run tests
      
    mvn jetty:run
      will launch the app in embedded jetty mode

    mvn tomcat7:deploy
      will deploy into a Tomcat 7/8/8.5 container if you give it credentials to a manager-text role
      (alternatively run clean-compile-package and manually deploy war)
      
 
Next up :
    External wsdl generation / addition to system
    Externalise (some) logging config options


Improvements / Analysis / Other features:

    Error handling examples
	RESTful service consumers
    Security options with camel for endpoints
    'Dozer' component for bean mapping option?
    Drools integration?
    Spring Namespace handlers / extensions of Camel Spring XML routing
    Industry standard services, routes, transformations for standard deployment pattern
    Better xslt using namespaces
    XSD schema validation on GX transformed messages
    Container analysis - Karaf? Embedded jetty of any production value at all?
    Look at Akka

