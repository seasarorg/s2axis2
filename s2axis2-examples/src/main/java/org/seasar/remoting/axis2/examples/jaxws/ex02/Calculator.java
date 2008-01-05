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
package org.seasar.remoting.axis2.examples.jaxws.ex02;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(name = "Calc")
public interface Calculator {

    @WebMethod(action = "add")
    @WebResult
    @RequestWrapper(localName = "add", className = "org.seasar.remoting.axis2.examples.jaxws.ex02.jaxws.Add")
    @ResponseWrapper(localName = "addResponse", className = "org.seasar.remoting.axis2.examples.jaxws.ex02.jaxws.AddResponse")
    public int add(@WebParam(name = "value1")
    int value1, @WebParam(name = "value2")
    int value2);
}
