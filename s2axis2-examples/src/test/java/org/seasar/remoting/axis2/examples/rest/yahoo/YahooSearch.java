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
package org.seasar.remoting.axis2.examples.rest.yahoo;

import org.seasar.remoting.axis2.annotation.RestMethod;
import org.seasar.remoting.axis2.annotation.RestUriTemplate;

/**
 * @author takanori
 */
@RestUriTemplate("/WebSearchService/V1/webSearch")
public interface YahooSearch {

    @RestMethod(contentType = "application/x-www-form-urlencoded")
    String postSearch(YahooSearchDto dto);

    String getSearch(YahooSearchDto dto);

}
