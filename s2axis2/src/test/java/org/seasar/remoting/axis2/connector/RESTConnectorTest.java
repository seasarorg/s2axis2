package org.seasar.remoting.axis2.connector;

import org.apache.axiom.om.OMElement;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.remoting.axis2.rest.example.RestDto;

public class RESTConnectorTest extends S2TestCase {

    private RESTConnector restConnector;

    protected void setUp() throws Exception {
        super.setUp();
        include("s2axis2-rest.dicon");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetTargetOperation_success() {
        String actual = this.restConnector.getTargetOperation();
        assertEquals("restService", actual);
    }

    public void testCreateRequest_success() throws Exception {

        RestDto dto = new RestDto();
        dto.setId(new Integer(1));
        dto.setName("name");

        String expectedData = "<restService><name>name</name><id>1</id></restService>";

        OMElement actual = this.restConnector.createRequest("method",
                new Object[] { dto });

        System.out.println(actual);

        assertEquals(expectedData, actual.toString());
    }

}
