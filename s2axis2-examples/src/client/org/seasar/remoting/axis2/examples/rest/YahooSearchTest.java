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
package org.seasar.remoting.axis2.examples.rest;

import org.seasar.extension.unit.S2TestCase;

/**
 * @author takanori
 */
public class YahooSearchTest extends S2TestCase {

    public void setUp() {
        include("YahooSearchTest.dicon");
    }

    public void testPost() {

        YahooSearch rest = (YahooSearch) getComponent(YahooSearch.class);

        YahooSearchDto dto = new YahooSearchDto();
        dto.setAppid("ApacheRestDemo");
        dto.setQuery("Axis2 REST");

        String result = rest.post(dto);
        try {
            result = new String(result.getBytes(), "UTF-8");
            System.out.println(result);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            
            fail();
        }

       
    }

}
