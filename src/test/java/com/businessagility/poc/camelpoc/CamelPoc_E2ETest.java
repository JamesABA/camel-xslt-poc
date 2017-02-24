package com.businessagility.poc.camelpoc;

import org.apache.camel.test.spring.CamelSpringRunner;
import org.apache.camel.test.spring.CamelTestContextBootstrapper;
import org.apache.camel.test.spring.DisableJmx;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;

@RunWith(CamelSpringRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//@MockEndpointsAndSkip("file:*")

@DisableJmx(false)

/**
 * Created by JamesAyling on 23/02/2017.
 */
@ContextConfiguration("classpath:camelpoc-config.xml")
public class CamelPoc_E2ETest {

    private final static String url_CP = "http://localhost:8090/camelxsltpoc/ws/CamelPoc";

    static CamelPocServicePortType createCPClient() {

        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(CamelPocServicePortType.class);
        factory.setAddress(url_CP);

        return (CamelPocServicePortType) factory.create();
    }


    @Test
    public void testCamelPocRoute() {

        CamelPocServicePortType client = createCPClient();
        CamelPocRequestType in = new CamelPocRequestType();

        in.setLastName("Tester");
        in.setFirstName("Jimmy");

        CamelPocResponseType response = client.setName(in);

        assertEquals("Return code from service should be OK", response.getReturnCode(), "OK");

    }

}
