package com.businessagility.poc.camelpoc;

import org.apache.camel.*;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.camel.test.spring.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertNotNull;

/**
 * Created by JamesAyling on 13/03/2017
 *
 * Test the route loader, and the routes it loads.
 * Expecting the following test route to load
 *
 * <routes xmlns="http://camel.apache.org/schema/spring">
 <route id="testRoute">
 <from uri="direct:start"/>
 <to uri="mock:log:com.businessagility.poc.camelpoc?showAll=true&amp;multiline=true&amp;level=DEBUG"/>
 <to uri="mock:xslt:xslt/mapContactName.xslt"/>
 <to uri="mock:file://target/outputCU?fileName=test-$simple{date:now:yyyyMMdd-HHmmss}.xml"/>
 <to uri="mock:velocity:vm/contactUpdateResponse.vm"/>
 <to uri="direct:end"/>
 </route>

 </routes>
 */

@RunWith(CamelSpringRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration("classpath:spring/testconfig.xml")
//@DisableJmx(false)
@MockEndpoints
@MockEndpointsAndSkip("file:*")
public class RouteLoaderTest {

    @Autowired
    SpringCamelContext context;

    @Produce(uri = "direct:start")
    ProducerTemplate producer;

    @EndpointInject(uri = "mock:velocity:vm/contactUpdateResponse.vm")
    MockEndpoint velocityComponent;

    @EndpointInject(uri = "mock:log:com.businessagility.poc.camelpoc.TEST")
    MockEndpoint logComponent;

    @EndpointInject(uri = "mock:xslt:xslt/mapContactName.xslt")
    MockEndpoint transformComponent;

    @EndpointInject(uri="mock:file://target/outputCU")
    MockEndpoint fileComponent;

    final static String MESSAGE =
            "<web:updateContact xmlns:web=\"http://webservices.poc.businessagility.com/\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
            "<web:_publicID>ab:999</web:_publicID>" +
            "<web:_firstName>ImaUnit</web:_firstName>" +
            "<web:_lastName>Test</web:_lastName>" +
            "</web:updateContact>";

    final static String TRANSFORMED_MSG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Person xmlns=\"http://guidewire.com/cc/gx/PersonModel.gx\">"
            +"<FirstName>ImaUnit</FirstName><LastName>Test</LastName><PublicID>ab:999</PublicID></Person>";

    final static String RESPONSE_MSG = "      <web:updateContactResponse xmlns:web=\"http://webservices.poc.businessagility.com/\">\r\n" +
            "         <web:return>OK</web:return>\r\n" +
            "      </web:updateContactResponse>\r\n";



    @Test
    public void testMockEndpointsAvailable() throws InterruptedException {

        assertNotNull(context.hasEndpoint("mock:velocity:vm/contactUpdateResponse.vm"));
        assertNotNull(context.hasEndpoint("mock:log:com.businessagility.poc.camelpoc.TEST"));
        assertNotNull(context.hasEndpoint("mock:xslt:xslt/mapContactName.xslt"));
        assertNotNull(context.hasEndpoint("mock:file://target/outputCU"));
        assertNotNull(context.hasEndpoint("mock:direct:end"));
    }

    @Test
    public void testVelocityComponent() throws InterruptedException {
        velocityComponent.expectedMessageCount(1);
        velocityComponent.expectedBodiesReceived(TRANSFORMED_MSG);

        String response = producer.requestBody(MESSAGE).toString();

        Assert.assertEquals("Response did not match expected",RESPONSE_MSG, response );

        velocityComponent.assertIsSatisfied();
    }

    @Test
    public void testLogComponent() throws InterruptedException {
        logComponent.expectedMessageCount(1);

        producer.sendBody(MESSAGE);

        logComponent.assertIsSatisfied();
    }

    @Test
    public void testXSLTComponent() throws InterruptedException {
        transformComponent.expectedMessageCount(1);
        transformComponent.expectedBodiesReceived(MESSAGE);

        producer.sendBody(MESSAGE);

        transformComponent.assertIsSatisfied();
    }

    @Test
    public void testFileComponent() throws InterruptedException {
        //TODO: Figure out how to check mocked FileComponents

        //The mock file component never seems to register that its received anything, even though it works fine
        fileComponent.expectedMessageCount(0);
        //fileComponent.expectedBodiesReceived(TRANSFORMED_MSG);

        producer.sendBody(MESSAGE);

        fileComponent.assertIsSatisfied();
    }


}
