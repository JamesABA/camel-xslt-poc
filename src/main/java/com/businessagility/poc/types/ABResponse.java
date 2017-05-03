package com.businessagility.poc.types;

/**
 * ABResponse.java
 *
 * POJO to describe JSON message content for inbound messages
 */
public class ABResponse {

    private String _errors;
    private String _body;

    public String getErrors() {
        return _errors;
    }

    public void set_errors(String _errors) {
        this._errors = _errors;
    }

    public String getBody() {
        return _body;
    }

    public void set_body(String _body) {
        this._body = _body;
    }
}
