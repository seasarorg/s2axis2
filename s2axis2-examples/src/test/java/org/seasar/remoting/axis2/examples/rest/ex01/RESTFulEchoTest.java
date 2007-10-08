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
package org.seasar.remoting.axis2.examples.rest.ex01;

import org.seasar.extension.unit.S2TestCase;

/**
 * @author takanori
 */
public class RESTFulEchoTest extends S2TestCase {

    public RESTFulEchoTest(String name) {
        super(name);
    }

    public void setUp() {
        include("RESTFulEchoTest.dicon");
    }

    public void testPostEcho() {
        RESTFulEcho service = (RESTFulEcho)getComponent(RESTFulEcho.class);
        EchoDto expected = createEchoDto();

        EchoDto actual = service.postEcho(expected.getId(),
                expected.getMessage());

        assertEquals(expected, actual);
    }

    public void testGetEcho() {
        RESTFulEcho service = (RESTFulEcho)getComponent(RESTFulEcho.class);
        EchoDto expected = createEchoDto();

        EchoDto actual = service.getEcho(expected.getId(),
                expected.getMessage());

        assertEquals(expected, actual);
    }

    public void testPostEchoByBean() {
        RESTFulEcho service = (RESTFulEcho)getComponent(RESTFulEcho.class);
        EchoDto expected = createEchoDto();

        EchoDto actual = service.postEchoByBean(expected);

        assertEquals(expected, actual);
    }

    public void testGetEchoByBean() {
        RESTFulEcho service = (RESTFulEcho)getComponent(RESTFulEcho.class);
        EchoDto expected = createEchoDto();

        EchoDto actual = service.getEchoByBean(expected);

        assertEquals(expected, actual);
    }

    public void testGetEchoArray() {
        RESTFulEcho service = (RESTFulEcho)getComponent(RESTFulEcho.class);
        EchoDto[] actual = service.getEchoArray(10, "RESTFul エコーメッセージ");

        assertEquals(10, actual.length);
    }

    public void testCreateEchoArray() {
        RESTFulEcho service = (RESTFulEcho)getComponent(RESTFulEcho.class);
        EchoDto[] actual = service.createEchoArray(10,
                "RESTFul エコーメッセージ create");

        assertEquals(10, actual.length);
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
