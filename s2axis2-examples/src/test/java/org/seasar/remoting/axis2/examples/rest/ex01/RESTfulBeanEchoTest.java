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
package org.seasar.remoting.axis2.examples.rest.ex01;

import org.seasar.extension.unit.S2TestCase;

/**
 * @author takanori
 */
public class RESTfulBeanEchoTest extends S2TestCase {

    public RESTfulBeanEchoTest(String name) {
        super(name);
    }

    public void setUp() {
        include("RESTfulBeanEchoTest.dicon");
    }

    public void testPostEchoByBean() {
        RESTfulBeanEcho service = (RESTfulBeanEcho)getComponent(RESTfulBeanEcho.class);
        EchoDto expected = createEchoDto();

        EchoDto actual = service.postEchoByBean(expected);

        assertEquals(expected, actual);
    }

    public void testGetEchoByBean() {
        RESTfulBeanEcho service = (RESTfulBeanEcho)getComponent(RESTfulBeanEcho.class);
        EchoDto expected = createEchoDto();

        EchoDto actual = service.getEchoByBean(expected);

        assertEquals(expected, actual);
    }

    private EchoDto createEchoDto() {
        int id = 1;
        String msg = "RESTFul エコーメッセージ";

        EchoDto dto = new EchoDto();
        dto.setId(id);
        dto.setMessage(msg);

        return dto;
    }

}
