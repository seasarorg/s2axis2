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

import java.util.Calendar;
import java.util.Date;

import org.seasar.extension.unit.S2TestCase;

/**
 * @author takanori
 */
public class SimpleWrapTypeServiceTest extends S2TestCase {

    private SimpleWrapTypeService service;

    public SimpleWrapTypeServiceTest(String name) {
        super(name);
    }

    public void setUp() {
        include("SimpleWrapTypeServiceTest.dicon");
    }

    public void testBoolean() {
        Boolean expected = Boolean.TRUE;
        this.service.setBooleanParam(expected);
        Boolean actual = this.service.getBooleanParam();
        assertEquals(expected, actual);
    }

    public void testBoolean_Null() {
        this.service.setBooleanParam(null);
        Boolean actual = this.service.getBooleanParam();
        assertNull(actual);
    }

    public void testByte() {
        Byte expected = new Byte(Byte.MAX_VALUE);
        this.service.setByteParam(expected);
        Byte actual = this.service.getByteParam();
        assertEquals(expected, actual);
    }

    public void testByte_Null() {
        this.service.setByteParam(null);
        Byte actual = this.service.getByteParam();
        assertNull(actual);
    }

    public void testChar() {
        Character expected = new Character('a');
        this.service.setCharParam(expected);
        Character actual = this.service.getCharParam();
        assertEquals(expected, actual);
    }

    public void testChar_Null() {
        this.service.setCharParam(null);
        Character actual = this.service.getCharParam();
        assertNull(actual);
    }

    public void testDouble() {
        Double expected = new Double(Double.MAX_VALUE);
        this.service.setDoubleParam(expected);
        Double actual = this.service.getDoubleParam();
        assertEquals(expected, actual);
    }

    public void testDouble_Null() {
        this.service.setDoubleParam(null);
        Double actual = this.service.getDoubleParam();
        assertNull(actual);
    }

    public void testFloat() {
        Float expected = new Float(Float.MAX_VALUE);
        this.service.setFloatParam(expected);
        Float actual = this.service.getFloatParam();
        assertEquals(expected, actual);
    }

    public void testFloat_Null() {
        this.service.setFloatParam(null);
        Float actual = this.service.getFloatParam();
        assertNull(actual);
    }

    public void testInt() {
        Integer expected = new Integer(Integer.MAX_VALUE);
        this.service.setIntParam(expected);
        Integer actual = this.service.getIntParam();
        assertEquals(expected, actual);
    }

    public void testInt_Null() {
        this.service.setIntParam(null);
        Integer actual = this.service.getIntParam();
        assertNull(actual);
    }

    public void testLong() {
        Long expected = new Long(Long.MAX_VALUE);
        this.service.setLongParam(expected);
        Long actual = this.service.getLongParam();
        assertEquals(expected, actual);
    }

    public void testLong_Null() {
        this.service.setLongParam(null);
        Long actual = this.service.getLongParam();
        assertNull(actual);
    }

    public void testShort() {
        Short expected = new Short(Short.MAX_VALUE);
        this.service.setShortParam(expected);
        Short actual = this.service.getShortParam();
        assertEquals(expected, actual);
    }

    public void testShort_Null() {
        this.service.setShortParam(null);
        Short actual = this.service.getShortParam();
        assertNull(actual);
    }

    public void testDate() {
        Date expected = new Date();
        this.service.setDateParam(expected);
        Date actual = this.service.getDateParam();
        assertEquals(expected, actual);
    }

    public void testDate_Null() {
        this.service.setDateParam(null);
        Date actual = this.service.getDateParam();
        assertNull(actual);
    }

    // FIXME
    // public void testCalendar() {
    // Calendar expected = Calendar.getInstance();
    // this.service.setCalendarParam(expected);
    // Calendar actual = this.service.getCalendarParam();
    // assertEquals(expected, actual);
    // }

    public void testCalendar_Null() {
        this.service.setCalendarParam(null);
        Calendar actual = this.service.getCalendarParam();
        assertNull(actual);
    }

    public void testArray_Size0() {
        this.service.setArrayParam(new Object[0]);
        Object[] actual = this.service.getArrayParam();
        assertEquals(0, actual.length);
    }

    // Axis2では、配列の場合はnullにならない。
    public void testArray_Null() {
        this.service.setArrayParam(null);
        Object[] actual = this.service.getArrayParam();
        assertEquals(0, actual.length);
    }

    public void testArrayString() {
        int size = 3;
        String[] expected = new String[size];
        for (int i = 0; i < size; i++) {
            expected[i] = "array" + i;
        }

        this.service.setArrayStringParam(expected);
        String[] actual = this.service.getArrayStringParam();
        for (int i = 0; i < size; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    // Axis2では、配列の場合はnullにならない。
    public void testArrayString_Null() {
        this.service.setArrayStringParam(null);
        String[] actual = this.service.getArrayStringParam();
        assertEquals(0, actual.length);
    }

    public void testArrayString_Size0() {
        this.service.setArrayStringParam(new String[0]);
        String[] actual = this.service.getArrayStringParam();
        assertEquals(0, actual.length);
    }
}
