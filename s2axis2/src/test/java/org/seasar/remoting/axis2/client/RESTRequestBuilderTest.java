/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.remoting.axis2.client;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;

import org.apache.axiom.om.OMElement;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.remoting.axis2.rest.example.RestDto;
import org.seasar.remoting.axis2.rest.example.SampleRestService;

public class RestRequestBuilderTest extends S2TestCase {

    RestRequestBuilder builder;

    protected void setUp() throws Exception {
        super.setUp();
        include("s2axis2.dicon");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCreateRequestByBean_noBean() throws Exception {

        Method method = SampleRestService.class.getMethod("getHello",
                (Class[])null);

        String expectedData = "<getHello />";

        OMElement actual = this.builder.createRequestByBean(method, null);

        assertEquals(expectedData, actual.toString());
    }

    public void testCreateRequestByBean_hasBean1() throws Exception {

        Method method = SampleRestService.class.getMethod("beanEcho",
                new Class[] { RestDto.class });

        String expectedData = "<beanEcho><id>1</id><name></name><msg>テスト</msg></beanEcho>";

        RestDto dto = new RestDto();
        dto.setId(Integer.valueOf(1));
        dto.setName(null);
        dto.setMessage("テスト");

        OMElement actual = this.builder.createRequestByBean(method, dto);

        assertEquals(expectedData, actual.toString());
    }

    public void testCreateRequestByBean_hasBean2() throws Exception {

        Method method = SampleRestService.class.getMethod("beanEcho2",
                new Class[] { RestDto.class });

        String expectedData = "<echo2><id>1</id><name>name</name><msg>test</msg></echo2>";

        RestDto dto = new RestDto();
        dto.setId(Integer.valueOf(1));
        dto.setName("name");
        dto.setMessage("test");
        OMElement actual = this.builder.createRequestByBean(method, dto);

        assertEquals(expectedData, actual.toString());
    }

    public void testCreateRequestByBean_null() throws Exception {

        Method method = SampleRestService.class.getMethod("beanEcho",
                new Class[] { RestDto.class });

        String expectedData = "<beanEcho />";

        OMElement actual = this.builder.createRequestByBean(method, null);

        assertEquals(expectedData, actual.toString());
    }

    public void testCreateRequestByParameters_noParam() throws Exception {

        Method method = SampleRestService.class.getMethod("getHello",
                (Class[])null);

        String expectedData = "<getHello />";

        OMElement actual = this.builder.createRequestByParameters(method, null);

        assertEquals(expectedData, actual.toString());
    }

    public void testCreateRequestByParameters_hasParam1() throws Exception {

        Method method = SampleRestService.class.getMethod("postEcho",
                new Class[] { String.class });

        String expectedData = "<postEcho><msg>テスト</msg></postEcho>";

        OMElement actual = this.builder.createRequestByParameters(method,
                new Object[] { "テスト" });

        assertEquals(expectedData, actual.toString());
    }

    public void testCreateRequestByParameters_hasParam2() throws Exception {

        Method method = SampleRestService.class.getMethod("postEcho2",
                new Class[] { Integer.class, String.class });

        String expectedData = "<echo2><id>1</id><msg>test</msg></echo2>";

        OMElement actual = this.builder.createRequestByParameters(method,
                new Object[] { Integer.valueOf(1), "test" });

        assertEquals(expectedData, actual.toString());
    }

    public void testCreateRequestByParameters_null() throws Exception {

        Method method = SampleRestService.class.getMethod("postEcho",
                new Class[] { String.class });

        String expectedData = "<postEcho />";

        OMElement actual = this.builder.createRequestByParameters(method, null);

        assertEquals(expectedData, actual.toString());
    }

    public void testCreateRequestByParameters_invalid() throws Exception {

        Method method = SampleRestService.class.getMethod("invalid",
                new Class[] { Integer.class, String.class });

        try {
            OMElement actual = this.builder.createRequestByParameters(method,
                    new Object[] { Integer.valueOf(1), "test" });
            fail(actual.toString());
        } catch (IllegalServiceMethodException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void testCreate_noArgs() throws Exception {
        Method method = SampleRestService.class.getMethod("getHello",
                (Class[])null);

        String expectedData = "<getHello />";

        OMElement actual = this.builder.create(method, null);

        assertEquals(expectedData, actual.toString());
    }

    public void testCreate_bean() throws Exception {
        Method method = SampleRestService.class.getMethod("beanEcho",
                new Class[] { RestDto.class });

        String expectedData = "<beanEcho><id>1</id><name>name</name><msg>test</msg></beanEcho>";

        RestDto dto = new RestDto();
        dto.setId(Integer.valueOf(1));
        dto.setName("name");
        dto.setMessage("test");
        OMElement actual = this.builder.create(method, new Object[] { dto });

        assertEquals(expectedData, actual.toString());
    }

    public void testCreate_parameters() throws Exception {
        Method method = SampleRestService.class.getMethod("postEcho",
                new Class[] { String.class });

        String expectedData = "<postEcho><msg>test</msg></postEcho>";

        OMElement actual = this.builder.create(method, new Object[] { "test" });

        assertEquals(expectedData, actual.toString());
    }

    public void testCreate_invalid() throws Exception {
        Method method = SampleRestService.class.getMethod("invalid2",
                new Class[] { Integer.class, RestDto.class });

        RestDto dto = new RestDto();
        dto.setId(Integer.valueOf(1));
        dto.setName("name");
        dto.setMessage("test");

        try {
            OMElement actual = this.builder.create(method, new Object[] {
                    Integer.valueOf(1), dto });

            fail(actual.toString());
        } catch (IllegalServiceMethodException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private String encode(String value, String encode) {
        String text;
        try {
            text = URLEncoder.encode(value, encode);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            text = value;
        }
        return text;
    }
}
