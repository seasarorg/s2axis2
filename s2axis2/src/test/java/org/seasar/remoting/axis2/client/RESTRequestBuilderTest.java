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

import java.lang.reflect.Method;

import org.apache.axiom.om.OMElement;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.remoting.axis2.rest.example.SampleRestDto;
import org.seasar.remoting.axis2.rest.example.SampleRestService;

public class RESTRequestBuilderTest extends S2TestCase {

    RESTRequestBuilder builder;

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

        String expectedData = "<getHello xmlns=\"http://example.rest.axis2.remoting.seasar.org\" />";

        OMElement actual = this.builder.createRequestByBean(method, null, null);

        assertEquals(expectedData, actual.toString());
    }

    public void testCreateRequestByBean_hasBean1() throws Exception {

        Method method = SampleRestService.class.getMethod("beanEcho",
                new Class[] { SampleRestDto.class });

        StringBuffer buff = new StringBuffer();
        buff.append("<beanEcho xmlns=\"http://example.rest.axis2.remoting.seasar.org\">");
        buff.append("<sampleRestDto xmlns=\"\" type=\"org.seasar.remoting.axis2.rest.example.SampleRestDto\">");
        buff.append("<id>1</id>");
        buff.append("<name></name>");
        buff.append("<msg>テスト</msg>");
        buff.append("</sampleRestDto>");
        buff.append("</beanEcho>");

        String expectedData = buff.toString();

        SampleRestDto dto = new SampleRestDto();
        dto.setId(Integer.valueOf(1));
        dto.setName(null);
        dto.setMessage("テスト");

        OMElement actual = this.builder.createRequestByBean(method, dto, null);

        assertEquals(expectedData, actual.toString());
    }

    public void testCreateRequestByBean_hasBean2() throws Exception {

        Method method = SampleRestService.class.getMethod("beanEcho2",
                new Class[] { SampleRestDto.class });

        StringBuffer buff = new StringBuffer();
        buff.append("<echo2 xmlns=\"http://example.rest.axis2.remoting.seasar.org\">");
        buff.append("<sampleRestDto xmlns=\"\" type=\"org.seasar.remoting.axis2.rest.example.SampleRestDto\">");
        buff.append("<id>1</id>");
        buff.append("<name>name</name>");
        buff.append("<msg>test</msg>");
        buff.append("</sampleRestDto>");
        buff.append("</echo2>");

        String expectedData = buff.toString();

        SampleRestDto dto = new SampleRestDto();
        dto.setId(Integer.valueOf(1));
        dto.setName("name");
        dto.setMessage("test");
        OMElement actual = this.builder.createRequestByBean(method, dto, null);

        assertEquals(expectedData, actual.toString());
    }

    public void testCreateRequestByBean_null() throws Exception {

        Method method = SampleRestService.class.getMethod("beanEcho",
                new Class[] { SampleRestDto.class });

        String expectedData = "<beanEcho xmlns=\"http://example.rest.axis2.remoting.seasar.org\" />";

        OMElement actual = this.builder.createRequestByBean(method, null, null);

        assertEquals(expectedData, actual.toString());
    }

    public void testCreateRequestByParameters_noParam() throws Exception {

        Method method = SampleRestService.class.getMethod("getHello",
                (Class[])null);

        String expectedData = "<getHello xmlns=\"http://example.rest.axis2.remoting.seasar.org\" />";

        OMElement actual = this.builder.createRequestByParameters(method, null,
                null);

        assertEquals(expectedData, actual.toString());
    }

    public void testCreateRequestByParameters_hasParam1() throws Exception {

        Method method = SampleRestService.class.getMethod("postEcho",
                new Class[] { String.class });

        StringBuffer buff = new StringBuffer();
        buff.append("<postEcho xmlns=\"http://example.rest.axis2.remoting.seasar.org\">");
        buff.append("<msg xmlns=\"\">テスト</msg>");
        buff.append("</postEcho>");

        String expectedData = buff.toString();

        OMElement actual = this.builder.createRequestByParameters(method,
                new Object[] { "テスト" }, null);

        assertEquals(expectedData, actual.toString());
    }

    public void testCreateRequestByParameters_hasParam2() throws Exception {

        Method method = SampleRestService.class.getMethod("postEcho2",
                new Class[] { Integer.class, String.class });

        StringBuffer buff = new StringBuffer();
        buff.append("<echo2 xmlns=\"http://example.rest.axis2.remoting.seasar.org\">");
        buff.append("<id xmlns=\"\">1</id>");
        buff.append("<msg xmlns=\"\">test</msg>");
        buff.append("</echo2>");

        String expectedData = buff.toString();

        OMElement actual = this.builder.createRequestByParameters(method,
                new Object[] { Integer.valueOf(1), "test" }, null);

        assertEquals(expectedData, actual.toString());
    }

    public void testCreateRequestByParameters_null() throws Exception {

        Method method = SampleRestService.class.getMethod("postEcho",
                new Class[] { String.class });

        String expectedData = "<postEcho xmlns=\"http://example.rest.axis2.remoting.seasar.org\" />";

        OMElement actual = this.builder.createRequestByParameters(method, null,
                null);

        assertEquals(expectedData, actual.toString());
    }

    public void testCreateRequestByParameters_invalid() throws Exception {

        Method method = SampleRestService.class.getMethod("invalid",
                new Class[] { Integer.class, String.class });

        try {
            OMElement actual = this.builder.createRequestByParameters(method,
                    new Object[] { Integer.valueOf(1), "test" }, null);
            fail(actual.toString());
        } catch (IllegalServiceMethodException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void testCreate_noArgs() throws Exception {
        Method method = SampleRestService.class.getMethod("getHello",
                (Class[])null);

        String expectedData = "<getHello xmlns=\"http://example.rest.axis2.remoting.seasar.org\" />";

        OMElement actual = this.builder.create(method, null, null);

        assertEquals(expectedData, actual.toString());
    }

    public void testCreate_bean() throws Exception {
        Method method = SampleRestService.class.getMethod("beanEcho",
                new Class[] { SampleRestDto.class });

        StringBuffer buff = new StringBuffer();
        buff.append("<beanEcho xmlns=\"http://example.rest.axis2.remoting.seasar.org\">");
        buff.append("<sampleRestDto xmlns=\"\" type=\"org.seasar.remoting.axis2.rest.example.SampleRestDto\">");
        buff.append("<id>1</id>");
        buff.append("<name>name</name>");
        buff.append("<msg>test</msg>");
        buff.append("</sampleRestDto>");
        buff.append("</beanEcho>");

        String expectedData = buff.toString();

        SampleRestDto dto = new SampleRestDto();
        dto.setId(Integer.valueOf(1));
        dto.setName("name");
        dto.setMessage("test");
        OMElement actual = this.builder.create(method, new Object[] { dto },
                null);

        assertEquals(expectedData, actual.toString());
    }

    public void testCreate_parameters() throws Exception {
        Method method = SampleRestService.class.getMethod("postEcho",
                new Class[] { String.class });

        StringBuffer buff = new StringBuffer();
        buff.append("<postEcho xmlns=\"http://example.rest.axis2.remoting.seasar.org\">");
        buff.append("<msg xmlns=\"\">test</msg>");
        buff.append("</postEcho>");

        String expectedData = buff.toString();

        OMElement actual = this.builder.create(method, new Object[] { "test" },
                null);

        assertEquals(expectedData, actual.toString());
    }

    public void testCreate_invalid() throws Exception {
        Method method = SampleRestService.class.getMethod("invalid2",
                new Class[] { Integer.class, SampleRestDto.class });

        SampleRestDto dto = new SampleRestDto();
        dto.setId(Integer.valueOf(1));
        dto.setName("name");
        dto.setMessage("test");

        try {
            OMElement actual = this.builder.create(method, new Object[] {
                    Integer.valueOf(1), dto }, null);

            fail(actual.toString());
        } catch (IllegalServiceMethodException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
