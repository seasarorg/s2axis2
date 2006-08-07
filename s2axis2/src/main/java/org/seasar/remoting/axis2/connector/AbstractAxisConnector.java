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
package org.seasar.remoting.axis2.connector;

import java.lang.reflect.Method;
import java.net.URL;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.ws.java2wsdl.Java2WSDLUtils;
import org.apache.ws.java2wsdl.SchemaGenerator;
import org.seasar.remoting.axis2.util.BeanUtil;
import org.seasar.remoting.common.connector.impl.TargetSpecificURLBasedConnector;

/**
 * Webサービスを呼び出すコネクタです。
 * 
 * @author takanori
 */
public abstract class AbstractAxisConnector extends
        TargetSpecificURLBasedConnector {

    /** AxisService */
    protected AxisService service;

    /** タイムアウト値 */
    private int           timeout = 0;

    /**
     * デフォルトのコンストラクタ。
     */
    public AbstractAxisConnector() {}

    /**
     * Webサービスを呼び出します。
     * 
     * @param options
     *            オプション
     * @param method
     *            Webサービスの実行メソッド
     * @param args
     *            Webサービスの引数
     * @return Webサービスの呼び出し結果
     * @throws AxisFault
     *             通信に失敗した場合
     */
    abstract protected Object execute(Options options, Method method,
            Object[] args) throws AxisFault;

    /**
     * 共通の設定を行い、executeを呼び出します。
     * 
     * @param url
     *            接続先のURL
     * @param method
     *            Webサービスの実行メソッド
     * @param args
     *            Webサービスの引数
     * @return Webサービスの呼び出し結果
     * @throws Throwable
     *             通信に失敗した場合
     */
    protected Object invoke(URL url, Method method, Object[] args)
            throws Throwable {

        EndpointReference targetEPR = new EndpointReference(url.toString());

        Options options = new Options();
        options.setTo(targetEPR);

        if (this.timeout > 0) {

            if (options.isUseSeparateListener()) {
                options.setTimeOutInMilliSeconds(timeout);
            }
            else {
                // HTTPプロトコルに対する設定
                options.setProperty(HTTPConstants.SO_TIMEOUT,
                                    new Integer(timeout));
                options.setProperty(HTTPConstants.CONNECTION_TIMEOUT,
                                    new Integer(timeout));
            }
        }

        Object returnValue = execute(options, method, args);

        return returnValue;
    }

    /**
     * リクエストを生成します。
     * 
     * @param method
     *            Webサービスの実行メソッド
     * @param args
     *            Webサービスの引数
     * @return リクエスト
     */
    protected OMElement createRequest(Method method, Object[] args) {
        OMFactory fac = OMAbstractFactory.getOMFactory();

        String packageName = method.getDeclaringClass().getPackage().getName();

        StringBuffer nsBuff = Java2WSDLUtils.schemaNamespaceFromPackageName(packageName);
        String schemaTargetNameSpace = nsBuff.toString();

        OMNamespace omNs = fac.createOMNamespace(schemaTargetNameSpace,
                                                 SchemaGenerator.SCHEMA_NAMESPACE_PRFIX);

        QName qName = new QName(method.getName());

        OMElement request = BeanUtil.getOMElement(qName, args, null);
        request.setNamespace(omNs);

        return request;
    }

    public void setService(AxisService service) {
        this.service = service;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

}
