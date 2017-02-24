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
@MockEndpoints

@DisableJmx(false)

@ContextConfiguration("classpath:camelpoc-config.xml")
/**
 * Created by JamesAyling on 23/02/2017.
 */
public class MockTestsUsingCamelSpringRunnerTest {


    final static String MESSAGE =
            "  <web:updateContact xmlns:web=\"http://webservices.poc.businessagility.com/\">\n" +
                    "         <web:_publicID>ab:999</web:_publicID>\n" +
                    "         <web:_firstName>ImaUnit</web:_firstName>\n" +
                    "         <web:_lastName>Test</web:_lastName>\n" +
                    "      </web:updateContact>\n";
    @Autowired
    CamelContext context;

    @Produce(uri = "cxf:bean:contactUpdateWS?dataFormat=PAYLOAD")
    ProducerTemplate startContactUpdate;

    @EndpointInject(uri = "mock:cxf:bean:contactUpdateWS")
    MockEndpoint webservice;

    @EndpointInject(uri = "mock:file://target/outputCU")
    MockEndpoint fileComponent;

    @EndpointInject(uri = "mock:velocity:vm/camelpocResponse.vm")
    MockEndpoint velocityComponent;

    @EndpointInject(uri = "mock:xslt:xslt/mapCamelPoc.xslt")
    MockEndpoint xsltComponent;

    @EndpointInject(uri = "mock:log:com.businessagility.poc.camelpoc")
    MockEndpoint logComponent;


    @Test
    public void testMockEndpointsAvailable() throws InterruptedException {


        assertNotNull(context.hasEndpoint("mock:file://target/outputCU"));
        assertNotNull(context.hasEndpoint("mock:xslt:xslt/mapCamelPoc.xslt"));
        assertNotNull(context.hasEndpoint("mock:log:com.businessagility.poc.camelpoc"));
        assertNotNull(context.hasEndpoint("mock:velocity:vm/camelpocResponse.vm"));
        assertNotNull(context.hasEndpoint("mock:cxf:bean:CamelPocWS"));
    }

    @Test
    public void testWebservice() throws InterruptedException {
        webservice.expectedMinimumMessageCount(1);
        webservice.expectedBodiesReceived(MESSAGE);

        startContactUpdate.sendBody(MESSAGE);

        webservice.assertIsSatisfied();
    }




    //    @Test
//    public void testFileComponent() throws InterruptedException {
//        fileComponent.expectedMinimumMessageCount(1);
//
//        startContactUpdate.sendBody(MESSAGE);
//
//        fileComponent.assertIsSatisfied();
//
//    }
//
//    @Test
//    public void testXSLTComponent() throws InterruptedException {
//        xsltComponent.expectedBodiesReceived(MESSAGE);
//        xsltComponent.expectedMessageCount(1);
//
//        startContactUpdate.sendBody(MESSAGE);
//
//        xsltComponent.assertIsSatisfied();
//
//    }
//
//    @Test
//    public void testVelocityComponent() throws InterruptedException {
//        velocityComponent.expectedMessageCount(1);
//
//        startContactUpdate.sendBody(MESSAGE);
//
//        velocityComponent.assertIsSatisfied();
//    }
//
//    @Test
//    public void testLogComponent() throws InterruptedException {
//        logComponent.expectedMessageCount(3);
//
//        startContactUpdate.sendBody(MESSAGE);
//
//        logComponent.assertIsSatisfied();
//
//    }

}
