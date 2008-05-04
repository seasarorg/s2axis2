/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the
 * License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package org.seasar.remoting.axis2.examples.ex03;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.seasar.remoting.axis2.examples.common.util.ConvertUtil;

public class SympleTypeBean {

    private String   strParam    = "";

    private short    shortParam  = 1;

    private int      intParam    = 2;

    private long     longParam   = 3L;

    private float    floatParam  = 1.0f;

    private double   doubleParam = 1.1;

    private boolean  boolParam   = true;

    private Date     date        = new Date();

    // private Calendar cal = Calendar.getInstance();

    private Calendar calGMT      = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

    public SympleTypeBean() {}

    public String getStrParam() {
        return this.strParam;
    }

    public void setStrParam(String strParam) {
        this.strParam = strParam;
    }

    public short getShortParam() {
        return this.shortParam;
    }

    public void setShortParam(short shortParam) {
        this.shortParam = shortParam;
    }

    public int getIntParam() {
        return this.intParam;
    }

    public void setIntParam(int intParam) {
        this.intParam = intParam;
    }

    public long getLongParam() {
        return this.longParam;
    }

    public void setLongParam(long longParam) {
        this.longParam = longParam;
    }

    public float getFloatParam() {
        return this.floatParam;
    }

    public void setFloatParam(float floatParam) {
        this.floatParam = floatParam;
    }

    public double getDoubleParam() {
        return this.doubleParam;
    }

    public void setDoubleParam(double doubleParam) {
        this.doubleParam = doubleParam;
    }

    public boolean isBoolParam() {
        return this.boolParam;
    }

    public void setBoolParam(boolean boolParam) {
        this.boolParam = boolParam;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    // FIXME
    // public Calendar getCal() {
    // return cal;
    // }
    //
    // public void setCal(Calendar cal) {
    // this.cal = cal;
    // }

    public Calendar getCalGMT() {
        return calGMT;
    }

    public void setCalGMT(Calendar calGMT) {
        Calendar cal = ConvertUtil.convertAtGMT(calGMT);
        this.calGMT = cal;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer("[");
        buf.append(strParam).append(", ");
        buf.append(shortParam).append(", ");
        buf.append(intParam).append(", ");
        buf.append(longParam).append(", ");
        buf.append(floatParam).append(", ");
        buf.append(doubleParam).append(", ");
        buf.append(boolParam).append(", ");
        buf.append(date).append(", ");
        // FIXME
        // buf.append(cal).append(", ");
        buf.append(calGMT).append("");
        buf.append("]");
        return buf.toString();
    }

}
