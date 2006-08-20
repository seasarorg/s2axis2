/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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

    public void testString() {
        this.service.registerString("test");
        String value = this.service.readString();
        System.out.println(value);
        
        assertTrue(true);
    }
    
    public void testCharacter() {
        this.service.registerCharacter(new Character('a'));
        Character value = this.service.readCharacter();
        System.out.println(value);
        
        assertTrue(true);
    }
    
    public void testBoolean() {
        this.service.registerBoolean(new Boolean(true));
        Boolean value = this.service.readBoolean();
        System.out.println(value);
        
        assertTrue(true);
    }
    
    public void testByte() {
        this.service.registerByte(new Byte((byte)1));
        Byte value = this.service.readByte();
        System.out.println(value);
        
        assertTrue(true);
    }
    
    public void testShort() {
        this.service.registerShort(new Short((short)1));
        Short value = this.service.readShort();
        System.out.println(value);
        
        assertTrue(true);
    }
    
    public void testInteger() {
        this.service.registerInteger(new Integer(1));
        Integer value = this.service.readInteger();
        System.out.println(value);
        
        assertTrue(true);
    }
    
    public void testLong() {
        this.service.registerLong(new Long(1));
        Long value = this.service.readLong();
        System.out.println(value);
        
        assertTrue(true);
    }
    
    public void testDouble() {
        this.service.registerDouble(new Double(1.1));
        Double value = this.service.readDouble();
        System.out.println(value);
        
        assertTrue(true);
    }
    
    public void testFloat() {
        this.service.registerFloat(new Float(1.1));
        Float value = this.service.readFloat();
        System.out.println(value);
        
        assertTrue(true);
    }
    
    public void testStringArray() {
        String[] array = new String[3];
        array[0] = "value0";
        array[1] = "value1";
        array[2] = "value2";
        
        this.service.registerStringArray(array);
        String[] value = this.service.readStringArray();
        System.out.println(value);
        
        assertTrue(true);
    }
    
    public void testCalendar() {
        this.service.registerCalendar(Calendar.getInstance());
        Calendar value = this.service.readCalendar();
        System.out.println(value);
        
        assertTrue(true);
    }

}
