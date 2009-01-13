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
package org.seasar.remoting.axis2.client;

import java.lang.reflect.Method;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.client.Options;
import org.seasar.remoting.axis2.util.RPCUtil;

/**
 * RPC形式のリクエストを構築するクラスです。
 * 
 * @author takanori
 *
 */
public class RPCRequestBuilder implements RequestBuilder {

    /**
     * デフォルトコンストラクタ。
     */
    public RPCRequestBuilder() {}

    /**
     * {@inheritDoc}
     */
    public OMElement create(Method method, Object[] args, Options options) {
        OMElement request;
        try {
            request = RPCUtil.createRequest(method, args);
        } catch (Exception ex) {
            throw new IllegalServiceMethodException(method, args, ex);
        }
        return request;
    }

}
