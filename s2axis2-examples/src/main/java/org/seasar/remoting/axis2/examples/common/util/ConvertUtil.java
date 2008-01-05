/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.remoting.axis2.examples.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.seasar.framework.exception.ParseRuntimeException;

/**
 * 値の変換を行うユーティリティ。
 * 
 * @author takanori
 *
 */
public class ConvertUtil {

    public static final String      FORMAT_GMT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private static final DateFormat DATE_FORMAT;

    static {
        DateFormat dateFormat = new SimpleDateFormat(FORMAT_GMT);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        DATE_FORMAT = dateFormat;
    }

    /**
     * 指定されたDateオブジェクトのタイムゾーンをGMTにしたものを返す。
     * 
     * @param date 変更前の日付
     * @return 変更後の日付
     */
    public static Date convertAtGMT(Date date) {
        String source = DATE_FORMAT.format(date);
        Date converted;
        try {
            converted = DATE_FORMAT.parse(source);
        } catch (ParseException ex) {
            throw new ParseRuntimeException(ex);
        }
        return converted;
    }

    /**
     * 指定されたCalendarオブジェクトのタイムゾーンをGMTにしたものを返す。
     * 
     * @param cal 変更前のカレンダ
     * @return 変更後のカレンダ
     */
    public static Calendar convertAtGMT(Calendar cal) {
        Date date = convertAtGMT(cal.getTime());

        Calendar converted = Calendar.getInstance(DATE_FORMAT.getTimeZone());
        converted.setTime(date);

        return converted;
    }
}
