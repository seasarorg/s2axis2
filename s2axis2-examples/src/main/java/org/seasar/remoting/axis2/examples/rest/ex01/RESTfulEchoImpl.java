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

import org.seasar.remoting.axis2.annotation.RestUriParam;

/**
 * @author takanori
 */
public class RESTfulEchoImpl implements RESTfulEcho {

    public RESTfulEchoImpl() {}

    public EchoDto postEcho(@RestUriParam("id")
    Integer id, @RestUriParam("message")
    String message) {
        EchoDto dto = new EchoDto();
        dto.setId(id);
        dto.setMessage(message);
        return dto;
    }

    public EchoDto getEcho(@RestUriParam("id")
    Integer id, @RestUriParam("message")
    String message) {
        EchoDto dto = new EchoDto();
        dto.setId(id);
        dto.setMessage(message);
        return dto;
    }

    public EchoDto[] getEchoArray(int count, String message) {
        if (count < 0) {
            count = 0;
        }

        EchoDto[] array = new EchoDto[count];
        for (int i = 0; i < count; i++) {
            EchoDto dto = new EchoDto();
            dto.setId(i);
            dto.setMessage(message);

            array[i] = dto;
        }
        return array;
    }

    public EchoDto[] createEchoArray(int count, String message) {
        return getEchoArray(count, message);
    }

}
