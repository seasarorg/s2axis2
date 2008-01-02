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
package org.seasar.remoting.axis2.rest.example;

import org.seasar.remoting.axis2.annotation.RestMethod;
import org.seasar.remoting.axis2.annotation.RestUriTemplate;
import org.seasar.remoting.axis2.annotation.RestUriParam;

@RestUriTemplate("/RestService")
public interface SampleRestService {

    SampleRestDto beanEcho(SampleRestDto dto);

    @RestMethod(name = "echo2", httpMethod = RestMethod.HTTP_METHOD_POST)
    SampleRestDto beanEcho2(SampleRestDto dto);

    String getHello();

    @RestMethod(name = "hello2", httpMethod = RestMethod.HTTP_METHOD_GET)
    String getHello2();

    String postEcho(@RestUriParam("msg")
    String message);

    @RestMethod(name = "echo2", httpMethod = RestMethod.HTTP_METHOD_POST)
    String postEcho2(@RestUriParam("id")
    Integer id, @RestUriParam("msg")
    String message);

    String invalid(@RestUriParam("id")
    Integer id, String message);

    String invalid2(@RestUriParam("id")
    Integer id, SampleRestDto dto);

}
