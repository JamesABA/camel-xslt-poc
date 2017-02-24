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
@ContextConfiguration("classpath:camelpoc-config.xml")
@MockEndpoints
public class ContactUpdateJMSRouteTest {

    public static final String MESSAGE = "<ATestMessage><FirstName>JMSfirstname</FirstName><LastName>JMSlastname</LastName></ATestMessage>";

    @Autowired
    CamelContext context;

    //TODO: Again, not happy with this as ActiveMQ broker needs to be up.
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

        producerJMS.sendBody(MESSAGE);

        velocityComponent.assertIsSatisfied();
    }

    @Test
    public void testLogComponent() throws InterruptedException {
        logComponent.expectedMessageCount(2);

        producerJMS.sendBody(MESSAGE);

        logComponent.assertIsSatisfied();
    }

    @Test
    public void testRequestQueue() throws InterruptedException {
        requestQ.expectedMessageCount(1);
        requestQ.expectedBodiesReceived(MESSAGE);

        producerJMS.sendBody(MESSAGE);

        requestQ.assertIsSatisfied();
    }

    @Test
    public void testResponseQueue() throws InterruptedException {
        responseQ.expectedMessageCount(1);
        //responseQ.expectedBodiesReceived(VM_MESSAGE);

        producerJMS.sendBody(MESSAGE);

        responseQ.assertIsSatisfied();
    }

}
