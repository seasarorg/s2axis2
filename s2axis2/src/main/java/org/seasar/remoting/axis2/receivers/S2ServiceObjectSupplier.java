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
package org.seasar.remoting.axis2.receivers;

import org.apache.axis2.AxisFault;
import org.apache.axis2.ServiceObjectSupplier;
import org.apache.axis2.description.AxisService;

/**
 * S2コンテナで管理されるオブジェクトを、 Axis2で利用するMessageReciverに提供します。
 * 
 * @author takanori
 */
public class S2ServiceObjectSupplier implements ServiceObjectSupplier {

    /**
     * デフォルトコンストラクタ。
     */
    public S2ServiceObjectSupplier() {}

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.axis2.ServiceObjectSupplier#getServiceObject(org.apache.axis2.description.AxisService)
     */
    public Object getServiceObject(AxisService axisService) throws AxisFault {
        Object serviceObject;

        ServiceHolder serviceHolder = new ServiceHolder();

        try {
            serviceObject = serviceHolder.getServiceObject(axisService);
        } catch (Exception ex) {
            throw AxisFault.makeFault(ex);
        }

        return serviceObject;
    }

}