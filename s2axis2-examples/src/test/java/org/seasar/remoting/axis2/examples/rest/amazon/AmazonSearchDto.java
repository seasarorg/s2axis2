/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import org.seasar.remoting.axis2.annotation.RestUriParam;

public class AmazonSearchDto {

    @RestUriParam("Service")
    private static final String SERVICE   = "AWSECommerceService";

    @RestUriParam("Operation")
    private static final String OPERATION = "ItemLookup";

    @RestUriParam("AWSAccessKeyId")
    private String              accessKeyId;

    @RestUriParam("AssociateTag")
    private String              associateTag;

    @RestUriParam("ItemId")
    private String              itemId;

    public AmazonSearchDto() {}

    public String getService() {
        return SERVICE;
    }

    public String getOperation() {
        return OPERATION;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAssociateTag() {
        return associateTag;
    }

    public void setAssociateTag(String associateTag) {
        this.associateTag = associateTag;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

}
