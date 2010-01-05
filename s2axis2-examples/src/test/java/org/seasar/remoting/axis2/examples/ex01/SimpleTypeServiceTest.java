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

/**
 * @author takanori
 */
public class SimpleTypeServiceTest extends S2TestCase {

    private SimpleTypeService service;

    public SimpleTypeServiceTest(String name) {
        super(name);
    }

    public void setUp() {
        include("SimpleTypeServiceTest.dicon");
        this.service = (SimpleTypeService) getComponent(SimpleTypeService.class);
    }

    public void testBoolean() {
        boolean expected = true;
        this.service.setBooleanParam(expected);
        boolean actual = this.service.isBooleanParam();
        assertEquals(expected, actual);
    }

    public void testByte() {
        byte expected = Byte.MAX_VALUE;
        this.service.setByteParam(expected);
        byte actual = this.service.getByteParam();
        assertEquals(expected, actual);
    }

    public void testChar() {
        char expected = 'a';
        this.service.setCharParam(expected);
        char actual = this.service.getCharParam();
        assertEquals(expected, actual);
    }

    public void testDouble() {
        double expected = Double.MAX_VALUE;
        this.service.setDoubleParam(expected);
        double actual = this.service.getDoubleParam();
        assertEquals(expected, actual, 0);
    }

    public void testFloat() {
        float expected = Float.MAX_VALUE;
        this.service.setFloatParam(expected);
        float actual = this.service.getFloatParam();
        assertEquals(expected, actual, 0);
    }

    public void testInt() {
        int expected = Integer.MAX_VALUE;
        this.service.setIntParam(expected);
        int actual = this.service.getIntParam();
        assertEquals(expected, actual);
    }

    public void testLong() {
        long expected = Long.MAX_VALUE;
        this.service.setLongParam(expected);
        long actual = this.service.getLongParam();
        assertEquals(expected, actual);
    }

    public void testShort() {
        short expected = Short.MAX_VALUE;
        this.service.setShortParam(expected);
        short actual = this.service.getShortParam();
        assertEquals(expected, actual);
    }

}
