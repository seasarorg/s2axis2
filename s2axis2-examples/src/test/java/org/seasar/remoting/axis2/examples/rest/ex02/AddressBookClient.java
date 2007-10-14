package org.seasar.remoting.axis2.examples.rest.ex02;

import java.io.UnsupportedEncodingException;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.description.WSDL20DefaultValueHolder;
import org.apache.axis2.description.WSDL2Constants;
import org.apache.axis2.transport.http.util.URIEncoderDecoder;

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
        options.setProperty(Constants.Configuration.ENABLE_REST_THROUGH_GET,
                Constants.VALUE_TRUE);
        options.setProperty(
                Constants.Configuration.MESSAGE_TYPE,
                org.apache.axis2.transport.http.HTTPConstants.MEDIA_TYPE_X_WWW_FORM);

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
        OMElement rootElement = fac.createOMElement("addEntry", null);

        OMElement id = fac.createOMElement("id", null, rootElement);
        id.setText(getQueryText(entry.getId()));

        OMElement name = fac.createOMElement("name", null, rootElement);
        name.setText(getQueryText(entry.getName()));

        OMElement street = fac.createOMElement("street", null, rootElement);
        street.setText(getQueryText(entry.getStreet()));

        OMElement city = fac.createOMElement("city", null, rootElement);
        city.setText(getQueryText(entry.getCity()));

        OMElement state = fac.createOMElement("state", null, rootElement);
        state.setText(getQueryText(entry.getState()));

        OMElement postalCode = fac.createOMElement("postalCode", null,
                rootElement);
        postalCode.setText(getQueryText(entry.getPostalCode()));

        return rootElement;
    }

    protected String getQueryText(Object value) {
        if (value == null) {
            return "";
        }

        String queryParameterSeparator = WSDL20DefaultValueHolder.getDefaultValue(WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR);
        String legalCharacters = WSDL2Constants.LEGAL_CHARACTERS_IN_QUERY.replaceAll(
                queryParameterSeparator, "");

        String text;
        try {
            text = URIEncoderDecoder.quoteIllegal(value.toString(),
                    legalCharacters);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();

            text = value.toString();
        }

        return text;
    }

    public static void main(String[] args1) throws AxisFault {

        String url = "http://127.0.0.1:8088/s2axis2-examples/services/AddressBookService";

        AddressBookClient client = new AddressBookClient();
        client.init();

        // /////////////////////////////////////////////////////////////////////

        /*
         * Creates an Entry and stores it in the AddressBook.
         */
        for (int i = 0; i < 5; i++) {
            Entry entry = new Entry();

            entry.setId(Integer.valueOf(i));
            entry.setName("Abby Cadabby : " + i);
            entry.setStreet("Sesame Street : " + i);
            entry.setCity("Sesame City : " + i);
            entry.setState("Sesame State : " + i);
            entry.setPostalCode("123-" + i);

            client.setUp(url, "addEntry");
            OMElement om = client.getAddPayload(entry);

            client.serviceClient.sendRobust(om);
        }

    }
}
