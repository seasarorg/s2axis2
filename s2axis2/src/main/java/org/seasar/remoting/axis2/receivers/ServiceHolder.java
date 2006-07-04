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

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.receivers.AbstractMessageReceiver;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

/**
 * MessageReciverで利用される、サービスオブジェクトを返すクラスです。
 * 
 * @author takanori
 */
public class ServiceHolder {

    private static final String SERVICE_CLASS = AbstractMessageReceiver.SERVICE_CLASS;

    private S2Container         container;

    /**
     * デフォルトのコンストラクタ。
     */
    public ServiceHolder() {
        this.container = SingletonS2ContainerFactory.getContainer();
    }

    /**
     * 
     * サービスオブジェクトを返します。
     * 
     * @param msgContext
     *            MessageContext
     * @return サービスオブジェクト
     * @throws NotFoundServiceException
     *             サービスオブジェクトが見つからない場合
     */
    public Object getServiceObject(MessageContext msgContext)
            throws NotFoundServiceException {

        Object serviceObj;
        try {
            AxisService service = msgContext.getOperationContext().getServiceContext().getAxisService();
            ClassLoader classLoader = service.getClassLoader();
            Parameter implInfoParam = service.getParameter(SERVICE_CLASS);

            if (implInfoParam != null) {
                Class implClass = Class.forName(((String) implInfoParam.getValue()).trim(),
                                                true,
                                                classLoader);

                // S2コンテナが保持しているオブジェクトを取得する。
                serviceObj = getComponent(implClass);
            }
            else {
                throw new NotFoundServiceException("Can't find SERVICE_CLASS.");
            }
        }
        catch (Exception ex) {
            throw new NotFoundServiceException(ex);
        }

        return serviceObj;
    }

    /**
     * S2コンテナで管理されているコンポーネントを返します。
     * 
     * @param clazz
     *            サービスクラス
     * @return コンポーネント
     */
    protected Object getComponent(Class clazz) {

        Object component;

        if (this.container != null && clazz != null) {
            component = this.container.getComponent(clazz);
        }
        else {
            component = null;
        }

        return component;
    }

}
