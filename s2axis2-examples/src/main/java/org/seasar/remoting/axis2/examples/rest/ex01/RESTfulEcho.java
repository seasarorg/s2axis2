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

import org.seasar.remoting.axis2.annotation.RestMethod;
import org.seasar.remoting.axis2.annotation.RestUriParam;

/**
 * @author takanori
 */
public interface RESTfulEcho {

    EchoDto postEcho(@RestUriParam("id")
    Integer id, @RestUriParam("message")
    String message);

    EchoDto getEcho(@RestUriParam("id")
    Integer id, @RestUriParam("message")
    String message);

    EchoDto[] getEchoArray(@RestUriParam("count")
    int count, @RestUriParam("message")
    String message);

    @RestMethod(httpMethod = RestMethod.HTTP_METHOD_POST)
    EchoDto[] createEchoArray(@RestUriParam("count")
    int count, @RestUriParam("message")
    String message);
}
