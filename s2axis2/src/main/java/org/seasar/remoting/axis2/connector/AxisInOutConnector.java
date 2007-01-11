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
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.databinding.utils.BeanUtil;
import org.apache.axis2.engine.DefaultObjectSupplier;

/**
 * 
 * @author takanori
 * 
 */
public class AxisInOutConnector extends AbstractRPCConnector {

    public AxisInOutConnector() {}

    protected Object execute(Options options, Method method, Object[] args)
            throws Exception {

        ServiceClient client = new ServiceClient();
        client.setOptions(options);

        OMElement request = createRequest(method, args);
        OMElement response = client.sendReceive(request);

        Class returnType = method.getReturnType();
        Object result;
        if (returnType.equals(void.class)) {
            result = null;
        }
        else {
            Object[] returnTypes = new Object[] { returnType };
            
            // see org.apache.axis2.rpc.client.RPCServiceClient
            Object[] returnValue = BeanUtil.deserialize(response, returnTypes, new DefaultObjectSupplier());

            result = returnValue[0];
        }

        return result;
    }

}
