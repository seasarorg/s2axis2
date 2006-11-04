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

interface SimpleWrapTypeService {

    Object[] getArrayParam();

    String[] getArrayStringParam();

    Boolean getBooleanParam();

    Byte getByteParam();

    Calendar getCalendarParam();

    Character getCharParam();

    Date getDateParam();

    Double getDoubleParam();

    Float getFloatParam();

    Integer getIntParam();

    Long getLongParam();

    Short getShortParam();

    String getStringParam();

    boolean setArrayParam(Object[] arrayParam);

    boolean setArrayStringParam(String[] arrayStringParam);

    boolean setBooleanParam(Boolean booleanParam);

    boolean setByteParam(Byte byteParam);

    boolean setCalendarParam(Calendar calendarParam);

    boolean setCharParam(Character charPram);

    boolean setDateParam(Date dateParam);

    boolean setDoubleParam(Double doubleParam);

    boolean setFloatParam(Float floatParam);

    boolean setIntParam(Integer intParam);

    boolean setLongParam(Long longParam);

    boolean setShortParam(Short shortParam);

    boolean setStringParam(String stringParam);
}
