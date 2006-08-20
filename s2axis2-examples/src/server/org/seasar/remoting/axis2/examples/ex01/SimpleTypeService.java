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

public interface SimpleTypeService {

    boolean registerString(String value);

    String readString();

    boolean registerCharacter(Character value);

    Character readCharacter();

    boolean registerBoolean(Boolean value);

    Boolean readBoolean();

    boolean registerByte(Byte value);

    Byte readByte();

    boolean registerShort(Short value);

    Short readShort();

    boolean registerInteger(Integer value);

    Integer readInteger();

    boolean registerLong(Long value);

    Long readLong();

    boolean registerDouble(Double value);

    Double readDouble();

    boolean registerFloat(Float value);

    Float readFloat();
    
    boolean registerStringArray(String[] value);

    String[] readStringArray();

    boolean registerCalendar(Calendar value);

    Calendar readCalendar();

}
