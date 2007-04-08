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

import javax.xml.namespace.QName;

import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;

/**
 * RPC形式で、同期的にサービスを呼び出すためのConnectorです。
 * 
 * @author takanori
 */
public class AxisInOutConnector extends AbstractRPCConnector {

    /**
     * デフォルトのコンストラクタ。
     */
    public AxisInOutConnector() {}

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.remoting.axis2.connector.AbstractRPCConnector#execute(org.apache.axis2.client.Options,
     *      java.lang.reflect.Method, java.lang.Object[])
     */
    protected Object execute(Options options, Method method, Object[] args)
            throws Exception {

        RPCServiceClient client = createClient();

        QName targetQName = createOperationQName(method);
        Class returnType = method.getReturnType();

        // WS-Addressingを利用する場合の設定
        options.setAction("urn:" + method.getName());
        
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
