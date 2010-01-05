/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.remoting.axis2.examples.rest.ex02;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;

public class AddressBookClient {

    private ServiceClient serviceClient;

    /** HTTP POST として実行するメソッドの接頭文字列 */
    protected String[]    httpPostPrefixArray   = new String[] { "post",
            "create", "insert", "add"          };

    /** HTTP PUT として実行するメソッドの接頭文字列 */
    protected String[]    httpPutPrefixArray    = new String[] { "put",
            "update", "modify", "store"        };

    /** HTTP DELETE として実行するメソッドの接頭文字列 */
    protected String[]    httpDeletePrefixArray = new String[] { "delete",
            "remove"                           };

    public AddressBookClient() throws AxisFault {
        this.serviceClient = new ServiceClient();
    }

    public void init() {
        Options options = this.serviceClient.getOptions();
        if (options == null) {
            options = new Options();
        }

        options.setManageSession(true);

        // REST
        options.setTransportInProtocol(Constants.TRANSPORT_HTTP);

        options.setProperty(Constants.Configuration.ENABLE_REST,
                Constants.VALUE_TRUE);

        this.serviceClient.setOptions(options);
    }

    public void setUp(String url, String methodName) {

        Options options = this.serviceClient.getOptions();
        if (options == null) {
            options = new Options();
        }

        // Endpoint
        EndpointReference targetEPR = new EndpointReference(url + "/"
                + methodName);
        options.setTo(targetEPR);

        // WS-Addressing
        options.setAction("urn:" + methodName);

        // HttpMethod
        String httpMethod = getHttpMethod(methodName);
        options.setProperty(Constants.Configuration.HTTP_METHOD, httpMethod);

        this.serviceClient.setOptions(options);
    }

    protected String getHttpMethod(String methodName) {

        if (matchMethodPrefix(methodName, this.httpPostPrefixArray) >= 0) {
            return Constants.Configuration.HTTP_METHOD_POST;
        } else if (matchMethodPrefix(methodName, this.httpPutPrefixArray) >= 0) {
            return Constants.Configuration.HTTP_METHOD_PUT;
        } else if (matchMethodPrefix(methodName, this.httpDeletePrefixArray) >= 0) {
            return Constants.Configuration.HTTP_METHOD_DELETE;
        } else {
            return Constants.Configuration.HTTP_METHOD_GET;
        }
    }

    protected int matchMethodPrefix(String methodName, String[] prefixArray) {
        for (int i = 0; i < prefixArray.length; i++) {
            if (methodName.startsWith(prefixArray[i])) {
                return i;
            }
        }
        return -1;
    }

    public OMElement getAddPayload(Entry entry) {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace ns = fac.createOMNamespace(
                "http://ex02.rest.examples.axis2.remoting.seasar.org", "");

        OMElement rootElement = fac.createOMElement("addEntry", ns);

        OMElement dto = fac.createOMElement("entity", null, rootElement);

        OMElement id = fac.createOMElement("id", null, dto);
        id.setText(getQueryText(entry.getId()));

        OMElement name = fac.createOMElement("name", null, dto);
        name.setText(getQueryText(entry.getName()));

        OMElement street = fac.createOMElement("street", null, dto);
        street.setText(getQueryText(entry.getStreet()));

        OMElement city = fac.createOMElement("city", null, dto);
        city.setText(getQueryText(entry.getCity()));

        OMElement state = fac.createOMElement("state", null, dto);
        state.setText(getQueryText(entry.getState()));

        OMElement postalCode = fac.createOMElement("postalCode", null, dto);
        postalCode.setText(getQueryText(entry.getPostalCode()));

        return rootElement;
    }

    public OMElement getFindPayload(Integer id) {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace ns = fac.createOMNamespace(
                "http://ex02.rest.examples.axis2.remoting.seasar.org", "");

        OMElement rootElement = fac.createOMElement("findEntry", ns);

        OMElement idElem = fac.createOMElement("id", null, rootElement);
        idElem.setText(getQueryText(id));

        return rootElement;
    }

    protected String getQueryText(Object value) {
        if (value == null) {
            return "";
        } else {
            return value.toString();
        }
    }

    public static void main(String[] args1) throws AxisFault {

        String url = "http://127.0.0.1:8080/s2axis2-examples/services/addressBook";

        AddressBookClient client = new AddressBookClient();
        client.init();

        OMElement request;
        OMElement response;

        client.setUp(url, "findEntry");
        request = client.getFindPayload(Integer.valueOf(0));

        response = client.serviceClient.sendReceive(request);
        System.out.println(response);

        for (int i = 0; i < 5; i++) {
            Entry entry = new Entry();

            entry.setId(Integer.valueOf(i));
            entry.setName("Abby Cadabby : " + i);
            entry.setStreet("Sesame Street : " + i);
            entry.setCity("Sesame City : " + i);
            entry.setState("Sesame State : " + i);
            entry.setPostalCode("123-" + i);

            client.setUp(url, "addEntry");
            request = client.getAddPayload(entry);

            client.serviceClient.sendRobust(request);
        }

        client.setUp(url, "findEntry");
        request = client.getFindPayload(Integer.valueOf(0));

        response = client.serviceClient.sendReceive(request);
        System.out.println(response);

    }
}
