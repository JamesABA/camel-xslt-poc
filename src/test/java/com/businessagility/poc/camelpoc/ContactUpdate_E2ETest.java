package com.businessagility.poc.camelpoc;


import com.businessagility.poc.webservices.ContactUpdatePortType;
import org.apache.camel.test.spring.CamelSpringRunner;
import org.apache.camel.test.spring.CamelTestContextBootstrapper;
import org.apache.camel.test.spring.DisableJmx;
import org.apache.camel.test.spring.MockEndpointsAndSkip;
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

//Make sure we don't write output to file as the result of this test
@MockEndpointsAndSkip("file*")
@DisableJmx(false)

@ContextConfiguration("classpath:spring/camelpoc-config.xml")
/**
 * Created by JamesAyling on 21/02/2017.
 *
 * More like a system test, not really 'unit', but will suffice for now.
 *
 * Going to try to launch the routes, and check that the webservice responds ok
 *
 */
public class ContactUpdate_E2ETest {

    private final static String url_CU = "http://localhost:9090/ws/ContactUpdate";

    static ContactUpdatePortType createCUClient() {

        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(ContactUpdatePortType.class);
        factory.setAddress(url_CU);

        return (ContactUpdatePortType) factory.create();
    }

//    @Test
//    public void dummyTest()
//    {
//        assert(true);
//    }

    @Test
    public void testCustomerUpdateRoute() {

        ContactUpdatePortType client = createCUClient();

        String response = client.updateContact("ab:123", "Jimmy", "Tester");

        assertEquals("Return code from service should be OK", response, "OK");
    }

}
