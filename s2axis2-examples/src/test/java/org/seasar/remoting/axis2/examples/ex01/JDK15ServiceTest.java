/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
import org.seasar.remoting.axis2.examples.ex01.JDK15Service.ColorType;

public class JDK15ServiceTest extends S2TestCase {

    JDK15Service service;

    public JDK15ServiceTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        include("JDK15ServiceTest.dicon");
    }

    public void testGetColorType() {
        String colorTypeValue = this.service.getColorType();
        ColorType actual = ColorType.valueOf(colorTypeValue);

        assertEquals(ColorType.RED, actual);
    }

}
