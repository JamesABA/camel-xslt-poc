package com.businessagility.poc.types;

/**
 * ABRequest.java
 *
 * POJO to define JSON message format used with outbound messages
 */
public class ABRequest {

    private String _serviceList;
    private String _body;
    private String _username;

    public String get_username() {
        return _username;
    }

    public void set_username(String _username) {
        this._username = _username;
    }

    public String get_serviceList() {
        return _serviceList;
    }

    public void set_serviceList(String _serviceList) {
        this._serviceList = _serviceList;
    }

    public String get_body() {
        return _body;
    }

    public void set_body(String _body) {
        this._body = _body;
    }
}
