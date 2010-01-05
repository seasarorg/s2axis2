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
package org.seasar.remoting.axis2.examples.ex01;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.remoting.axis2.examples.ex01.Echo;

/**
 * @author takanori
 */
public class EchoTest extends S2TestCase {

    public EchoTest(String name) {
        super(name);
    }

    public void setUp() {
        include("EchoTest.dicon");
    }

    public void testEcho() {
        Echo service = (Echo) getComponent(Echo.class);

        int id = 1;
        String msg = "echo message";

        String expect = "[id = " + id + "] " + msg;
        String result = service.echo(id, msg);

        assertEquals(expect, result);

        System.out.println(result);
    }

}
