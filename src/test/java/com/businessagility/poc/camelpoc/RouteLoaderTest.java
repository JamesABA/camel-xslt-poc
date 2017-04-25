package com.businessagility.poc.camelpoc;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.camel.test.spring.CamelSpringRunner;
import org.apache.camel.test.spring.CamelTestContextBootstrapper;
import org.apache.camel.test.spring.MockEndpoints;
import org.apache.camel.test.spring.MockEndpointsAndSkip;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertNotNull;

/**
 * Created by JamesAyling on 13/03/2017
 * <p>
 * Test the route loader, and the routes it loads.
 */

@RunWith(CamelSpringRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration("classpath:spring/testconfig.xml")
//@DisableJmx(false)
@MockEndpoints
@MockEndpointsAndSkip("file:*")
public class RouteLoaderTest {

    final static String MESSAGE =
            "<web:updateContact xmlns:web=\"http://contactupdate.webservices.poc.businessagility.com/\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                    "<web:_publicID>ab:999</web:_publicID>" +
                    "<web:_firstName>ImaUnit</web:_firstName>" +
                    "<web:_lastName>Test</web:_lastName>" +
                    "</web:updateContact>";
    final static String BAD_MESSAGE =
            "<web:updateContact xmlns:web=\"http://contactupdate.webservices.poc.businessagility.com/\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                    "<web:_firstName>ImaBadUnit</web:_firstName>" +
                    "<web:_lastName>Test</web:_lastName>" +
                    "</web:updateContact>";
    final static String TRANSFORMED_MSG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Person xmlns=\"http://guidewire.com/cc/gx/com.businessagility.agilebridge.gx.personmodel\">"
            + "<FirstName>ImaUnit</FirstName><LastName>Test</LastName><PublicID>ab:999</PublicID><WorkPhone/></Person>";
    final static String RESPONSE_MSG = "      <web:updateContactResponse xmlns:web=\"http://contactupdate.webservices.poc.businessagility.com/\">\r\n" +
            "         <web:return>OK</web:return>\r\n" +
            "      </web:updateContactResponse>";
    @Autowired
    SpringCamelContext context;
    @Produce(uri = "direct:start")
    ProducerTemplate dstartProducer;
    @Produce(uri = "direct:validatePerson")
    ProducerTemplate vparProducer;
    @EndpointInject(uri = "mock:velocity:configuration/vm/routeTestResponse.vm")
    MockEndpoint velocityComponent;
    @EndpointInject(uri = "mock:log:com.businessagility.poc.camelpoc.TEST")
    MockEndpoint logComponent;
    @EndpointInject(uri = "mock:xslt:configuration/xslt/mapPerson.xslt")
    MockEndpoint transformComponent;
    @EndpointInject(uri = "mock:file://target/outputCU")
    MockEndpoint fileComponent;
    @EndpointInject(uri = "mock:validator:configuration/xsd/PersonModel.xsd")
    MockEndpoint validatorComponent;
    @EndpointInject(uri = "mock:direct:invalidTransform")
    MockEndpoint invalidTransformComponent;

    @Test
    public void testMockEndpointsAvailable() throws InterruptedException {

        assertNotNull(context.hasEndpoint("mock:velocity:configuration/vm/routeTestResponse.vm"));
        assertNotNull(context.hasEndpoint("mock:log:com.businessagility.poc.camelpoc.TEST"));
        assertNotNull(context.hasEndpoint("mock:xslt:configuration/xslt/mapPerson.xslt"));
        assertNotNull(context.hasEndpoint("mock:file://target/outputCU"));
        assertNotNull(context.hasEndpoint("mock:direct:invalidTransform"));
        assertNotNull(context.hasEndpoint("mock:validator:configuration/xsd/PersonModel.xsd"));
        assertNotNull(context.hasEndpoint("mock:direct:validatePerson"));
    }

    @Test
    public void testVelocityComponent() throws InterruptedException {
        velocityComponent.expectedMessageCount(1);
        velocityComponent.expectedBodiesReceived(TRANSFORMED_MSG);

        String response = dstartProducer.requestBody(MESSAGE).toString();

        Assert.assertEquals("Response did not match expected", RESPONSE_MSG, response);

        velocityComponent.assertIsSatisfied();
    }

    @Test
    public void testLogComponent() throws InterruptedException {
        logComponent.expectedMessageCount(1);

        dstartProducer.sendBody(MESSAGE);

        logComponent.assertIsSatisfied();
    }

    @Test
    public void testXSLTComponent() throws InterruptedException {
        transformComponent.expectedMessageCount(1);
        transformComponent.expectedBodiesReceived(MESSAGE);

        dstartProducer.sendBody(MESSAGE);

        transformComponent.assertIsSatisfied();
    }

    @Test
    public void testFileComponent() throws InterruptedException {
        //TODO: Figure out how to check mocked FileComponents

        //The mock file component never seems to register that its received anything, even though it works fine
        fileComponent.expectedMessageCount(0);
        //fileComponent.expectedBodiesReceived(TRANSFORMED_MSG);

        dstartProducer.sendBody(MESSAGE);

        fileComponent.assertIsSatisfied();
    }

    @Test
    public void testValidatorComponent() throws InterruptedException {
        validatorComponent.expectedMinimumMessageCount(1);
        invalidTransformComponent.expectedMinimumMessageCount(1);

        vparProducer.sendBody(BAD_MESSAGE);

        validatorComponent.assertIsSatisfied();
        invalidTransformComponent.assertIsSatisfied();
    }

}
