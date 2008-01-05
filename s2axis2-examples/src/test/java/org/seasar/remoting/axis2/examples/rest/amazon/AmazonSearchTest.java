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
package org.seasar.remoting.axis2.examples.rest.amazon;

import org.seasar.extension.unit.S2TestCase;

public class AmazonSearchTest extends S2TestCase {

    public void setUp() {
        include("AmazonSearchTest.dicon");
    }

    public void testPost() {
        AmazonSearch rest = (AmazonSearch)getComponent(AmazonSearch.class);
        AmazonSearchDto dto = createDto();
        String result = rest.postSearchResult(dto);
        System.out.println(result);
    }

    public void testGet() {
        AmazonSearch rest = (AmazonSearch)getComponent(AmazonSearch.class);
        AmazonSearchDto dto = createDto();
        String result = rest.getSearchResult(dto);
        System.out.println(result);
    }

    private AmazonSearchDto createDto() {
        AmazonSearchDto dto = new AmazonSearchDto();
        // アソシエイトID
        // See: http://affiliate.amazon.co.jp/gp/associates/join/main.html
        dto.setT("webservice-20");
        // デベロッパー・トークン
        // See:
        // http://www.amazon.co.jp/exec/obidos/subst/associates/join/webservices.html
        dto.setDevT("0Y6WJGPB6TW8AVAHGFR2");
        dto.setAsinSearch("4774128554");
        dto.setLocale("jp");
        dto.setType("heavy");
        dto.setF("xml");
        return dto;
    }
}
