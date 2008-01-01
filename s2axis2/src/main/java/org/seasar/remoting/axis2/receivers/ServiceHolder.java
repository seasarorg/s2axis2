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

import org.apache.axis2.Constants;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.Parameter;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.remoting.axis2.util.AxisServiceUtil;

/**
 * MessageReceiverで利用される、サービスオブジェクトを返すクラスです。
 * 
 * @author takanori
 */
public class ServiceHolder {

    private static final String SERVICE_CLASS = Constants.SERVICE_CLASS;

    private S2Container         container;

    /**
     * デフォルトのコンストラクタ。
     */
    public ServiceHolder() {}

    /**
     * このオブジェクトを初期化します。
     */
    public void init() {
        this.container = SingletonS2ContainerFactory.getContainer();
    }

    /**
     * 
     * サービスオブジェクトを返します。
     * 
     * @param msgContext MessageContext
     * @return サービスオブジェクト
     */
    public Object getServiceObject(AxisService service) {

        Object serviceObj;
        try {
            ClassLoader classLoader = service.getClassLoader();
            Parameter implInfoParam = service.getParameter(SERVICE_CLASS);

            if (implInfoParam != null) {
                Class implClass = Class.forName(
                        ((String)implInfoParam.getValue()).trim(), true,
                        classLoader);

                // S2コンテナが保持しているオブジェクトを取得する。
                serviceObj = getComponent(service, implClass);
            } else {
                throw new NotFoundServiceException(service.getName());
            }
        } catch (Exception ex) {
            if (ex instanceof NotFoundServiceException) {
                throw (NotFoundServiceException)ex;
            } else {
                throw new NotFoundServiceException(service.getName(), ex);
            }
        }

        return serviceObj;
    }

    /**
     * S2コンテナで管理されているコンポーネントを返します。
     * 
     * @param clazz サービスクラス
     * @return コンポーネント
     */
    protected Object getComponent(AxisService service, Class clazz) {

        if (this.container == null) {
            init();
        }

        Object component;

        if (this.container != null && clazz != null) {
            ComponentDef componentDef = this.container.getComponentDef(clazz);
            if (componentDef != null) {
                component = this.container.getComponent(clazz);
                String scope = AxisServiceUtil.getAxisScope(componentDef.getInstanceDef());
                service.setScope(scope);
            } else {
                component = null;
            }

        } else {
            component = null;
        }

        return component;
    }

}
