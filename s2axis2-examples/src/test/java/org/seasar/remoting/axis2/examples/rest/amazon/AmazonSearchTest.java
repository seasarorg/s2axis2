/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

        assertResult(result);
    }

    public void testGet() {
        AmazonSearch rest = (AmazonSearch)getComponent(AmazonSearch.class);
        AmazonSearchDto dto = createDto();
        String result = rest.getSearchResult(dto);

        assertResult(result);
    }

    public void assertResult(String result) {
        System.out.println(result);

        assertTrue(result.contains("<ItemId>4774128554</ItemId>"));
        assertTrue(result.contains("<Title>Seasar2で学ぶ DIとAOP アスペクト指向によるJava開発</Title>"));
    }

    private AmazonSearchDto createDto() {
        AmazonSearchDto dto = new AmazonSearchDto();
        // デベロッパー・トークン
        // See:
        // http://www.amazon.co.jp/exec/obidos/subst/associates/join/webservices.html
        dto.setAccessKeyId("0Y6WJGPB6TW8AVAHGFR2");
        // アソシエイトID
        // See: http://affiliate.amazon.co.jp/gp/associates/join/main.html
        dto.setAssociateTag("webservice-20");

        dto.setItemId("4774128554");

        return dto;
    }
}
