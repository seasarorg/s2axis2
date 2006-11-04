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
import java.util.Date;

public class SimpleWrapTypeServiceImpl implements SimpleWrapTypeService {

    private Object[]  arrayParam;

    private String[]  arrayStringParam;

    private Boolean   booleanParam;

    private Byte      byteParam;

    private Calendar  calendarParam;

    private Character charParam;

    private Date      dateParam;

    private Double    doubleParam;

    private Float     floatParam;

    private Integer   intParam;

    private Long      longParam;

    private Short     shortParam;

    private String    stringParam;

    public SimpleWrapTypeServiceImpl() {}

    public Object[] getArrayParam() {
        return arrayParam;
    }

    public String[] getArrayStringParam() {
        return this.arrayStringParam;
    }

    public Boolean getBooleanParam() {
        return booleanParam;
    }

    public Byte getByteParam() {
        return byteParam;
    }

    public Calendar getCalendarParam() {
        return calendarParam;
    }

    public Character getCharParam() {
        return charParam;
    }

    public Date getDateParam() {
        return dateParam;
    }

    public Double getDoubleParam() {
        return doubleParam;
    }

    public Float getFloatParam() {
        return floatParam;
    }

    public Integer getIntParam() {
        return intParam;
    }

    public Long getLongParam() {
        return longParam;
    }

    public Short getShortParam() {
        return shortParam;
    }

    public String getStringParam() {
        return stringParam;
    }

    public boolean setArrayParam(Object[] arrayParam) {
        this.arrayParam = arrayParam;
        return true;
    }

    public boolean setArrayStringParam(String[] arrayStringParam) {
        this.arrayStringParam = arrayStringParam;
        return true;
    }

    public boolean setBooleanParam(Boolean booleanParam) {
        this.booleanParam = booleanParam;
        return true;
    }

    public boolean setByteParam(Byte byteParam) {
        this.byteParam = byteParam;
        return true;
    }

    public boolean setCalendarParam(Calendar calendarParam) {
        this.calendarParam = calendarParam;
        return true;
    }

    public boolean setCharParam(Character charParam) {
        this.charParam = charParam;
        return true;
    }

    public boolean setDateParam(Date dateParam) {
        this.dateParam = dateParam;
        return true;
    }

    public boolean setDoubleParam(Double doubleParam) {
        this.doubleParam = doubleParam;
        return true;
    }

    public boolean setFloatParam(Float floatParam) {
        this.floatParam = floatParam;
        return true;
    }

    public boolean setIntParam(Integer intParam) {
        this.intParam = intParam;
        return true;
    }

    public boolean setLongParam(Long longParam) {
        this.longParam = longParam;
        return true;
    }

    public boolean setShortParam(Short shortParam) {
        this.shortParam = shortParam;
        return true;
    }

    public boolean setStringParam(String stringParam) {
        this.stringParam = stringParam;
        return true;
    }

}
