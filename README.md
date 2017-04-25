# camel-xslt-poc
A proof of concept using cxf web services, ActiveMQ, camel routing and xslt with minimal lines of code

Updates :
- Includes a JSON datamapper and sample types to send multiple 
    service instructions to a remote host.
- Tomcat compliant
- Externally configurable by creating '/AB2/configuration/override.properties'
- Example external config held in the project folder - AB2.zip


Logging

    SLF4j with console and rollingfile appenders
        Configured in 'src/main/resources/log4j2.xml'
  
    Set the 'Root' attribute to debug if you want to see detailed runtime info


Running the code 

  Standard Maven goals with a couple of extra plugins
  
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
      
 
Next up

    External wsdl generation / addition to system
    Externalise (some) logging config options


Improvements / Analysis / Other features:

	JPA / Database route
    Error handling examples
	RESTful service consumers
    Security options with camel for endpoints
    'Dozer' component for bean mapping option?
    Drools integration?
    Spring Namespace handlers / extensions of Camel Spring XML routing
    Industry standard services, routes, transformations for standard deployment pattern
    Better xslt using namespaces
    Container analysis - Karaf? Embedded jetty of any production value at all?
    Akka

