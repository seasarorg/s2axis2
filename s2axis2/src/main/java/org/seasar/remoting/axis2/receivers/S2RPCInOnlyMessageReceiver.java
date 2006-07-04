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
package org.seasar.remoting.axis2.receivers;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.rpc.receivers.RPCInOnlyMessageReceiver;

/**
 * S2Axis2で利用するRPCInOnlyMessageReceiver。
 * 
 * @author takanori
 */
public class S2RPCInOnlyMessageReceiver extends RPCInOnlyMessageReceiver
        implements S2MessageReceiver {

    private ServiceHolder serviceHolder;

    public S2RPCInOnlyMessageReceiver() {}

    /**
     * Method makeNewServiceObject.
     * 
     * @param msgContext
     * @return Returns Object.
     * @throws AxisFault
     */
    protected Object makeNewServiceObject(MessageContext msgContext)
            throws AxisFault {
        Object serviceObject;

        try {
            serviceObject = this.serviceHolder.getServiceObject(msgContext);
        }
        catch (Exception ex) {
            throw AxisFault.makeFault(ex);
        }

        return serviceObject;
    }

    public void setServiceHolder(ServiceHolder serviceHolder) {
        this.serviceHolder = serviceHolder;
    }

}
