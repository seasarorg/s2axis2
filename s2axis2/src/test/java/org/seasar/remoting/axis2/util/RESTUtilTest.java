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
package org.seasar.remoting.axis2.util;

import java.lang.reflect.Method;

import org.seasar.remoting.axis2.annotation.RestMethod;
import org.seasar.remoting.axis2.annotation.RestUriTemplate;

import junit.framework.TestCase;

public class RESTUtilTest extends TestCase {

    public void setUp() throws Exception {}

    public void tearDown() throws Exception {}

    public void testGetUriTemplate_1() throws Exception {
        Method method = RestService1.class.getMethod("method1", new Class[0]);
        String acutal = RESTUtil.getUriTemplate(method, true);

        assertEquals("/RESTUtilTest$RestService1/method1", acutal);
    }

    public void testGetUriTemplate_post1() throws Exception {
        Method method = RestService1.class.getMethod("postMethod", new Class[0]);
        String acutal = RESTUtil.getUriTemplate(method, true);

        assertEquals("/RESTUtilTest$RestService1/postMethod", acutal);
    }

    public void testGetUriTemplate_2() throws Exception {
        Method method = RestService2.class.getMethod("method1", new Class[0]);
        String acutal = RESTUtil.getUriTemplate(method, true);

        assertEquals("/ws/RestService/method", acutal);
    }

    public void testGetUriTemplate_post2() throws Exception {
        Method method = RestService2.class.getMethod("postMethod", new Class[0]);
        String acutal = RESTUtil.getUriTemplate(method, true);

        assertEquals("/ws/RestService/methodByPost", acutal);
    }

    public void testGetServiceName_1() throws Exception {
        Method method = RestService1.class.getMethod("method1", new Class[0]);
        String acutal = RESTUtil.getServiceName(method);

        assertEquals("RESTUtilTest$RestService1", acutal);
    }

    public void testGetServiceName_2() throws Exception {
        Method method = RestService2.class.getMethod("method1", new Class[0]);
        String acutal = RESTUtil.getServiceName(method);

        assertEquals("/ws/RestService", acutal);
    }

    public void testGetOperationName_1() throws Exception {
        Method method = RestService1.class.getMethod("method1", new Class[0]);
        String acutal = RESTUtil.getOperationName(method);

        assertEquals("method1", acutal);
    }

    public void testGetOperationName_2() throws Exception {
        Method method = RestService2.class.getMethod("method1", new Class[0]);
        String acutal = RESTUtil.getOperationName(method);

        assertEquals("method", acutal);
    }

    private interface RestService1 {
        public void method1();

        public void postMethod();

        public void getMethod();
    }

    @RestUriTemplate("/ws/RestService")
    private interface RestService2 {
        @RestMethod(name = "method")
        public void method1();

        @RestMethod(name = "methodByPost")
        public void postMethod();

        @RestMethod(name = "methodByGet")
        public void getMethod();
    }

}
