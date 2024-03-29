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
package org.seasar.remoting.axis2.examples.http;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.remoting.axis2.examples.ex03.BeanEcho;
import org.seasar.remoting.axis2.examples.ex03.SympleTypeBean;

/**
 * @author takanori
 */
public class ProxyTest extends S2TestCase {

    public ProxyTest(String name) {
        super(name);
    }

    public void setUp() {
        include("ProxyTest.dicon");
    }

    public void test() {
        SympleTypeBean expected = new SympleTypeBean();
        expected.setStrParam("echo");

        BeanEcho beanEcho = (BeanEcho) getComponent(BeanEcho.class);
        SympleTypeBean actual = beanEcho.echo(expected);

        assertEquals(expected.toString(), actual.toString());
    }
}
