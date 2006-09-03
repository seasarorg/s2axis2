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
package org.seasar.remoting.axis2.examples.rest.amazon;

public class AmazonSearchDto {
    
    private String             t;

    public static final String devT_PARAMETER       = "dev-t";

    private String             devT;

    public static final String asinSearch_PARAMETER = "AsinSearch";

    private String             asinSearch;

    private String             locale;

    private String             type;

    private String             f;

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getDevT() {
        return devT;
    }

    public void setDevT(String devT) {
        this.devT = devT;
    }

    public String getAsinSearch() {
        return asinSearch;
    }

    public void setAsinSearch(String asinSearch) {
        this.asinSearch = asinSearch;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getF() {
        return f;
    }

    public void setF(String f) {
        this.f = f;
    }
}
