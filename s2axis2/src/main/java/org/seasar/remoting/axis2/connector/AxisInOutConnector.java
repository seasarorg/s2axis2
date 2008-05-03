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
package org.seasar.remoting.axis2.connector;

import java.lang.reflect.Method;

import javax.xml.namespace.QName;

import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.seasar.remoting.axis2.util.RPCUtil;

/**
 * 同期型要求応答形式（Request/Response）で、RPCとしてサービスを呼び出すためのConnectorです。
 * 
 * @author takanori
 */
public class AxisInOutConnector extends AbstractRPCConnector {

    /**
     * デフォルトのコンストラクタ。
     */
    public AxisInOutConnector() {}

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object execute(Method method, Object[] args, Options options)
            throws Exception {

        RPCServiceClient client = getClient();

        QName targetQName = RPCUtil.createOperationQName(method);
        Class returnType = method.getReturnType();

        Object result;
        if (returnType.equals(void.class)) {
            client.invokeRobust(targetQName, args);
            result = null;
        } else {
            Class[] returnTypes = new Class[] { method.getReturnType() };
            Object[] response = client.invokeBlocking(targetQName, args,
                    returnTypes);
            result = response[0];
        }

        return result;
    }

}
