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
package org.seasar.remoting.axis2.connector;

import java.lang.reflect.Method;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.remoting.axis2.rest.example.SampleRestService;

public class RESTConnectorTest extends S2TestCase {

    private RESTConnector restConnector;

    protected void setUp() throws Exception {
        super.setUp();
        include("s2axis2-rest.dicon");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetTargetUrl_service() throws Exception {
        Method method = SampleRestService.class.getMethod("getHello",
                (Class[])null);
        String actual = this.restConnector.getTargetUrl(method);
        assertEquals("http://localhost:8080/RestService", actual);
    }

    public void testGetTargetUrl_method() throws Exception {
        Method method = SampleRestService.class.getMethod("getHello",
                (Class[])null);
        this.restConnector.setAppendAction(true);
        String actual = this.restConnector.getTargetUrl(method);
        assertEquals("http://localhost:8080/RestService/getHello", actual);
    }
}
