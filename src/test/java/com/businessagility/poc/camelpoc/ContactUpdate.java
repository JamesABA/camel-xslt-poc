package com.businessagility.poc.camelpoc;

/**
 * Created by JamesAyling on 17/02/2017.
 *
 * Dev class used for simple webservice construction
 *
 *
 */

class ContactUpdate {

    private String firstName;
    private String lastName;
    private String publicID;

    public String updateContact(String _publicID, String _firstName, String _lastName){
        this.firstName = _firstName;
        this.lastName = _lastName;
        this.publicID = _publicID;

        return "0";
    }


}
