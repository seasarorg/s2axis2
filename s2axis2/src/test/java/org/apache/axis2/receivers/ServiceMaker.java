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
package org.apache.axis2.receivers;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.OperationContext;
import org.apache.axis2.context.ServiceContext;

public class ServiceMaker {

    AbstractMessageReceiver receiver;

    public ServiceMaker() {}

    public void make() {

        OperationContext opeContext = new OperationContext();
        opeContext.setParent(new ServiceContext());

        MessageContext msgContext = new MessageContext();
        msgContext.setOperationContext(opeContext);
        try {
            this.receiver.getTheImplementationObject(new MessageContext());
        } catch (AxisFault ex) {
            ex.printStackTrace();
        }
    }

    public void setReceiver(AbstractMessageReceiver receiver) {
        this.receiver = receiver;
    }

}
