package com.businessagility.poc.component;

import com.businessagility.poc.types.ABResponse;
import org.apache.camel.Exchange;

/**
 * Created by JamesAyling on 25/04/2017.
 */
public class ABMessageUnpack {

    public void process(Exchange exchange) {

        ABResponse response = exchange.getIn().getBody(ABResponse.class);

        exchange.getIn().setHeader("ABErrors", response.getErrors());
        exchange.getIn().setBody(response.getBody(), String.class);

    }
}
