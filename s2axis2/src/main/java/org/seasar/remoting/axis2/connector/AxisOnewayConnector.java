/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.remoting.axis2.connector;

import java.lang.reflect.Method;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.seasar.framework.message.MessageFormatter;
import org.seasar.remoting.axis2.util.RPCUtil;

/**
 * 一方向形式（Oneway）で、RPCとしてサービスを呼び出すためのConnectorです。
 * 
 * @author takanori
 */
public class AxisOnewayConnector extends AbstractRPCConnector {

    /**
     * デフォルトのコンストラクタ。
     */
    public AxisOnewayConnector() {}

    /**
     * {@inheritDoc}
     */
    protected Object execute(Method method, Object[] args, Options options)
            throws Exception {

        RPCServiceClient client = getClient();

        Class returnType = method.getReturnType();
        if (!returnType.equals(void.class)) {
            Object[] msgArgs = new Object[] { method.getReturnType() };
            throw new AxisFault(MessageFormatter.getSimpleMessage("EAXS1003",
                    msgArgs));
        }

        OMElement request = RPCUtil.createRequest(method, args);

        client.fireAndForget(request);

        return null;
    }

}
