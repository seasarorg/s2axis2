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

public class SimpleTypeServiceImpl implements SimpleTypeService {

    public boolean registerString(String value) {
        System.out.println(value);
        return true;
    }

    public String readString() {
        return "string";
    }

    public boolean registerCharacter(Character value) {
        System.out.println(value);
        return true;
    }

    public Character readCharacter() {
        return new Character('a');
    }

    public boolean registerBoolean(Boolean value) {
        System.out.println(value);
        return true;
    }

    public Boolean readBoolean() {
        return new Boolean(true);
    }

    public boolean registerByte(Byte value) {
        System.out.println(value);
        return true;
    }

    public Byte readByte() {
        return new Byte((byte)1);
    }

    public boolean registerShort(Short value) {
        System.out.println(value);
        return true;
    }

    public Short readShort() {
        return new Short((short)1);
    }

    public boolean registerInteger(Integer value) {
        System.out.println(value);
        return true;
    }

    public Integer readInteger() {
        return new Integer(1);
    }

    public boolean registerLong(Long value) {
        System.out.println(value);
        return true;
    }

    public Long readLong() {
        return new Long(1);
    }

    public boolean registerDouble(Double value) {
        System.out.println(value);
        return true;
    }

    public Double readDouble() {
        return new Double(1.1);
    }

    public boolean registerFloat(Float value) {
        System.out.println(value);
        return true;
    }

    public Float readFloat() {
        return new Float(1.1);
    }
    
    public boolean registerStringArray(String[] value) {
        System.out.println(value);
        return true;
    }

    public String[] readStringArray() {
        String[] value = new String[3];
        value[0] = "value0";
        value[1] = "value1";
        value[2] = "value2";
              
        return value;
    }

    public boolean registerCalendar(Calendar value) {
        System.out.println(value);
        return true;
    }

    public Calendar readCalendar() {
        return Calendar.getInstance();
    }

}
