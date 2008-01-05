/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.remoting.axis2.examples.context;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.remoting.axis2.client.S2AxisClientContext;
import org.seasar.remoting.axis2.client.S2AxisClientException;
import org.seasar.remoting.axis2.examples.ex01.Echo;
import org.seasar.remoting.axis2.examples.rest.ex01.EchoDto;
import org.seasar.remoting.axis2.examples.rest.ex01.RESTfulEcho;

/**
 * @author takanori
 */
public class LocalParameterTest extends S2TestCase {

    public LocalParameterTest(String name) {
        super(name);
    }

    public void setUp() {
        include("LocalParameterTest.dicon");
    }

    protected void tearDown() throws Exception {
        S2AxisClientContext.setEndpointURL(null);
    }

    public void testEndpoint_io() {
        Echo service = (Echo)getComponent(Echo.class);
        String endpointURL = "http://localhost:8080/s2axis2-examples/services/Echo";

        int id = 1;
        String msg = "echo message";

        String expect = "[id = " + id + "] " + msg;
        String actual;

        // diconに設定されているURLで接続
        S2AxisClientContext.setEndpointURL(null);
        try {
            actual = service.echo(id, msg);
            fail();
        } catch (S2AxisClientException ex) {
            System.err.println(ex.getMessage());
        }

        // このクラスで指定したURLで接続
        S2AxisClientContext.setEndpointURL(endpointURL);
        actual = service.echo(id, msg);

        assertEquals(expect, actual);
    }

    public void testEndpoint_rest() {
        RESTfulEcho service = (RESTfulEcho)getComponent(RESTfulEcho.class);
        String endpointURL = "http://localhost:8080/s2axis2-examples/services/RESTfulEcho/postEcho";

        Integer id = Integer.valueOf(1);
        String msg = "echo message";

        EchoDto actual;

        // diconに設定されているURLで接続
        S2AxisClientContext.setEndpointURL(null);
        try {
            actual = service.postEcho(id, msg);
            fail();
        } catch (S2AxisClientException ex) {
            System.err.println(ex.getMessage());
        }

        // このクラスで指定したURLで接続
        S2AxisClientContext.setEndpointURL(endpointURL);
        actual = service.postEcho(id, msg);

        assertEquals(id, actual.getId());
        assertEquals(msg, actual.getMessage());
    }

}
