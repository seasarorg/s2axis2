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

public class SimpleTypeServiceImpl implements SimpleTypeService {

    private boolean booleanParam;

    private byte    byteParam;

    private char    charParam;

    private double  doubleParam;

    private float   floatParam;

    private int     intParam;

    private long    longParam;

    private short   shortParam;

    public SimpleTypeServiceImpl() {}

    public byte getByteParam() {
        return byteParam;
    }

    public char getCharParam() {
        return charParam;
    }

    public double getDoubleParam() {
        return doubleParam;
    }

    public float getFloatParam() {
        return floatParam;
    }

    public int getIntParam() {
        return intParam;
    }

    public long getLongParam() {
        return longParam;
    }

    public short getShortParam() {
        return shortParam;
    }

    public boolean isBooleanParam() {
        return booleanParam;
    }

    public boolean setBooleanParam(boolean booleanParam) {
        this.booleanParam = booleanParam;
        return true;
    }

    public boolean setByteParam(byte byteParam) {
        this.byteParam = byteParam;
        return true;
    }

    public boolean setCharParam(char charParam) {
        this.charParam = charParam;
        return true;
    }

    public boolean setDoubleParam(double doubleParam) {
        this.doubleParam = doubleParam;
        return true;
    }

    public boolean setFloatParam(float floatParam) {
        this.floatParam = floatParam;
        return true;
    }

    public boolean setIntParam(int intParam) {
        this.intParam = intParam;
        return true;
    }

    public boolean setLongParam(long longParam) {
        this.longParam = longParam;
        return true;
    }

    public boolean setShortParam(short shortParam) {
        this.shortParam = shortParam;
        return true;
    }

}
