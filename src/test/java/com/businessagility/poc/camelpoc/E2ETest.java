package com.businessagility.poc.camelpoc;


import com.businessagility.poc.webservices.ContactUpdatePortType;
import org.apache.camel.test.spring.*;
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
@MockEndpointsAndSkip("file:*")

@DisableJmx(false)

@ContextConfiguration("classpath:camelpoc-config.xml")
/**
 * Created by JamesAyling on 21/02/2017.
 *
 * More like a system test, not really 'unit', but will suffice for now.
 *
 * Going to try to launch the full integration layer, stub the file endpoint,
 *  and check its received summat..
 */
public class E2ETest {


    private final static String url_CP = "http://localhost:8090/camelxsltpoc/ws/CamelPoc";
    private final static String url_CU = "http://localhost:8090/camelxsltpoc/ws/ContactUpdate";

    static CamelPocServicePortType createCPClient() {

        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(CamelPocServicePortType.class);
        factory.setAddress(url_CP);

        return (CamelPocServicePortType) factory.create();
    }

    static ContactUpdatePortType createCUClient() {

        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(ContactUpdatePortType.class);
        factory.setAddress(url_CU);

        return (ContactUpdatePortType) factory.create();
    }

    @Test
    public void testCamelPocRoute()
    {

        CamelPocServicePortType client = createCPClient();

        CamelPocRequestType in = new CamelPocRequestType();

        in.setLastName("Tester");
        in.setFirstName("Jimmy");

        CamelPocResponseType response = client.setName(in);

        assertEquals("Return code from service should be OK",response.getReturnCode(), "OK");

    }

    @Test
    public void testCustomerUpdateRoute()
    {

        ContactUpdatePortType client = createCUClient();

        String response = client.updateContact("ab:123","Jimmy", "Tester");

        assertEquals("Return code from service should be OK",response, "OK");

    }


}
