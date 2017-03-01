package com.businessagility.poc.camelpoc;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertNotNull;

/**
 * Created by JamesAyling on 24/02/2017.
 */

@RunWith(CamelSpringRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisableJmx(false)
@ContextConfiguration("classpath:spring/camelpoc-config.xml")
@MockEndpoints
public class ContactUpdateJMSRouteTest {

    public static final String MESSAGECC = "<ATestMessage><PublicID>cc:1234</PublicID><FirstName>JMSfirstnameCC</FirstName><LastName>JMSlastname</LastName></ATestMessage>";
    public static final String MESSAGEAB = "<ATestMessage><PublicID>ab:1234</PublicID><FirstName>JMSfirstnameAB</FirstName><LastName>JMSlastname</LastName></ATestMessage>";
    @Autowired
    CamelContext context;

    //TODO: Not happy with this as ActiveMQ broker needs to be up. Perhaps 'adviceWith' the endpoint so it uses a 'direct' EP for test?
    @Produce(uri = "activemq:queue:camelpoc.requestQ")
    ProducerTemplate producerJMS;

    @EndpointInject(uri = "mock:activemq:queue:camelpoc.requestQ")
    MockEndpoint requestQ;

    @EndpointInject(uri = "mock:velocity:vm/camelpocJMSResponse.vm")
    MockEndpoint velocityComponent;

    @EndpointInject(uri = "mock:log:com.businessagility.poc.camelpoc")
    MockEndpoint logComponent;

    @EndpointInject(uri = "mock:activemq:queue:camelpoc.responseQ")
    MockEndpoint responseQ;

    @EndpointInject(uri = "mock:activemq:queue:camelpoc.ccQ")
    MockEndpoint ccQ;

    @EndpointInject(uri = "mock:activemq:queue:camelpoc.abQ")
    MockEndpoint abQ;

    @Test
    public void testMockEndpointsAvailable() throws InterruptedException {

        assertNotNull(context.hasEndpoint("mock:log:com.businessagility.poc.camelpoc"));
        assertNotNull(context.hasEndpoint("mock:velocity:vm/camelpocJMSResponse.vm"));
        assertNotNull(context.hasEndpoint("mock:activemq:queue:camelpoc.requestQ"));
        assertNotNull(context.hasEndpoint("mock:activemq:queue:camelpoc.responseQ"));
    }

    @Test
    public void testVelocityComponent() throws InterruptedException {
        velocityComponent.expectedMessageCount(1);

        producerJMS.sendBody(MESSAGECC);

        velocityComponent.assertIsSatisfied();
    }

    @Test
    public void testLogComponent() throws InterruptedException {
        logComponent.expectedMessageCount(2);

        producerJMS.sendBody(MESSAGECC);

        logComponent.assertIsSatisfied();
    }

    @Test
    public void testRequestQueue() throws InterruptedException {
        requestQ.expectedMessageCount(1);
        requestQ.expectedBodiesReceived(MESSAGECC);

        producerJMS.sendBody(MESSAGECC);

        requestQ.assertIsSatisfied();
    }

    @Test
    public void testResponseQueue() throws InterruptedException {
        responseQ.expectedMessageCount(1);
        //responseQ.expectedBodiesReceived(VM_MESSAGE);

        producerJMS.sendBody(MESSAGECC);

        responseQ.assertIsSatisfied();
    }

    @Test
    public void testCBRouting() throws InterruptedException {
        ccQ.expectedMessageCount(1);
        abQ.expectedMessageCount(1);

        producerJMS.sendBody(MESSAGECC);
        producerJMS.sendBody(MESSAGEAB);

        ccQ.assertIsSatisfied();
        abQ.assertIsSatisfied();
    }


}
