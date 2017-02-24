package com.businessagility.poc.camelpoc;


import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringRunner;
import org.apache.camel.test.spring.CamelTestContextBootstrapper;
import org.apache.camel.test.spring.DisableJmx;
import org.apache.camel.test.spring.MockEndpoints;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertNotNull;


@RunWith(CamelSpringRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisableJmx(false)
@ContextConfiguration("classpath:camelpoc-config.xml")
@MockEndpoints
/**
 * Created by JamesAyling on 23/02/2017.
 */
public class ContactUpdateWSRouteTest {

    final static String MESSAGE ="<web:updateContact xmlns:web=\"http://webservices.poc.businessagility.com/\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                    "<web:_publicID>ab:999</web:_publicID>" +
                    "<web:_firstName>ImaUnit</web:_firstName>" +
                    "<web:_lastName>Test</web:_lastName>" +
                    "</web:updateContact>";

    final static String TRANSFORMED_MSG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Person xmlns=\"http://guidewire.com/cc/gx/PersonModel.gx\"><FirstName>ImaUnit</FirstName><LastName>Test</LastName><PublicID>ab:999</PublicID></Person>";

    @Autowired
    CamelContext context;

    //Set the test producer to hit the real webservice consumer, not the mock, or it wont route.
    //TODO: Unhappy with this as we shouldn't need CXF to unit test. Fix it.
    @Produce(uri = "cxf:bean:contactUpdateWS?dataFormat=PAYLOAD")
    ProducerTemplate startContactUpdate;

    @EndpointInject(uri = "mock:cxf:bean:contactUpdateWS")
    MockEndpoint webservice;

    @EndpointInject(uri = "mock:file://target/outputCU")
    MockEndpoint fileComponent;

    @EndpointInject(uri = "mock:velocity:vm/contactUpdateResponse.vm")
    MockEndpoint velocityComponent;

    @EndpointInject(uri = "mock:xslt:xslt/mapContactName.xslt")
    MockEndpoint xsltComponent;

    @EndpointInject(uri = "mock:log:com.businessagility.poc.camelpoc")
    MockEndpoint logComponent;


    @Test
    public void testMockEndpointsAvailable() throws InterruptedException {

        assertNotNull(context.hasEndpoint("mock:file://target/outputCU"));
        assertNotNull(context.hasEndpoint("mock:xslt:xslt/mapContactName.xslt"));
        assertNotNull(context.hasEndpoint("mock:log:com.businessagility.poc.camelpoc"));
        assertNotNull(context.hasEndpoint("mock:velocity:vm/contactUpdateResponse.vm"));
        assertNotNull(context.hasEndpoint("mock:cxf:bean:contactUpdateWS"));
    }

    @Test
    public void testWebservice() throws InterruptedException {
        webservice.expectedMinimumMessageCount(1);
        webservice.expectedBodiesReceived(MESSAGE);

        startContactUpdate.sendBody(MESSAGE);

        webservice.assertIsSatisfied();
    }

    @Test
    public void testXSLTComponent() throws InterruptedException {

        //Watch out, Mock xslt seems to add namespaces and trim all whitespace
        // so if your inbound message is missing a namespace for something (e.g. soap),
        // or is pretty printed then this test will fail.
        xsltComponent.expectedBodiesReceived(MESSAGE);
        xsltComponent.expectedMessageCount(1);

        startContactUpdate.sendBody(MESSAGE);

        xsltComponent.assertIsSatisfied();
    }

    @Test
    public void testVelocityComponent() throws InterruptedException {
        velocityComponent.expectedMessageCount(1);

        startContactUpdate.sendBody(MESSAGE);

        velocityComponent.assertIsSatisfied();
    }

    @Test
    public void testLogComponent() throws InterruptedException {
        logComponent.expectedMessageCount(3);

        startContactUpdate.sendBody(MESSAGE);

        logComponent.assertIsSatisfied();
    }

    //TODO:Figure this test out.
    //      Dead confused, doesnt seem to write file until test is complete, meaning that the test fails..
    //      Maybe inject the real file endpoint into the mock endpoint declaration as that seems to write
    //      immediately.. Would be good to know why though.

//    @Test
//    public void testFileComponent() throws InterruptedException {
//        fileComponent.expectedMinimumMessageCount(1);
//
//        startContactUpdate.sendBody(MESSAGE);
//
//        fileComponent.assertIsSatisfied();
//
//    }

}
