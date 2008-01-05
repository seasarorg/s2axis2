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
package org.seasar.remoting.axis2.util;

import java.lang.reflect.Method;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.databinding.utils.BeanUtil;
import org.apache.axis2.description.java2wsdl.Java2WSDLUtils;

public class RPCUtil {

    /**
     * Webサービスの実行メソッドのQNameを生成します。
     * 
     * @param method Webサービスの実行メソッド
     * @return QName
     * @throws AxisFault
     */
    public static QName createOperationQName(Method method) throws Exception {

        String className = method.getDeclaringClass().getName();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        StringBuffer nsBuff;
        try {
            nsBuff = Java2WSDLUtils.schemaNamespaceFromClassName(className,
                    loader);
        } catch (Exception ex) {
            // TODO 例外処理の見直し
            throw ex;
        }
        String schemaTargetNameSpace = nsBuff.toString();

        QName qName = new QName(schemaTargetNameSpace, method.getName());

        return qName;
    }

    /**
     * OMElement型のリクエストを生成します。
     * 
     * @param method Webサービスの実行メソッド
     * @param args Webサービスの実行メソッドの引数
     * @return リクエスト
     * @throws AxisFault
     */
    public static OMElement createRequest(Method method, Object[] args)
            throws Exception {

        QName qName = createOperationQName(method);

        // see org.apache.axis2.rpc.client.RPCServiceClient
        OMElement request = BeanUtil.getOMElement(qName, args, null, false,
                null);

        return request;
    }

}
