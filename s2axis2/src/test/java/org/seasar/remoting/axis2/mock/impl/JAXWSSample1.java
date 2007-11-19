package org.seasar.remoting.axis2.mock.impl;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public class JAXWSSample1 {

    public JAXWSSample1() {}

    @WebMethod
    public String echo(String message) {
        return message;
    }

}
