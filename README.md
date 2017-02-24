# camel-xslt-poc
A proof of concept using cxf webservices, camel routing and xslt with minimal lines of code (currently 0 excluding tests)

CXF webservices

  CamelPoc - hand generated WSDL with single operation that takes a firstname and lastname String parameter
  ContactUpdate - CXF generated (Java2WSDL) webservice with a single operation that takes a publicID, firstname and lastname String parameter


Routing

    CXF consumers (webservices)
    XSLT transformer to map webservice body to a simple structure (GX)
    File component to save transformed XML output
    Velocity template to generate webservice response
    Logging component used between each route step (DEBUG level)
  

Logging

  SLF4j with console and rollingfile appenders
    Configured in 'src/main/resources/log4j2.xml'
  
  Set the 'Root' level attribute to debug if you want to see detailed runtime info


Running the code 

  Standard Maven goals with embedded jetty server
  
    mvn compile
      will compile the code
      
    mvn clean
      will scrub the target folder of all generated and compiled code
    
    mvn install 
      will resolve dependencies, compile, and run tests
      
    mvn jetty:run
      will launch the app 
      
      
 
Next up :

    Add a JMS consumer
    Security options with camel for endpoints
    'Dozer' component for bean mapping option
    Drools integration?
    Spring Namespace handlers / extensions of Camel Spring XML routing
    ACORD standard services, routes, transformations for OOTB

Improvements :

    Externalise routing / endpoint config
    Better xslt using namespaces
    Better tests using mocked components
    Properties files for various configuration options
    XSD schema validation on GX transformed messages?

