/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
import java.util.HashMap;
import java.util.Map;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.databinding.utils.BeanUtil;
import org.apache.axis2.engine.DefaultObjectSupplier;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.seasar.framework.util.SerializeUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.remoting.axis2.client.S2AxisClientContext;
import org.seasar.remoting.axis2.client.S2AxisClientException;
import org.seasar.remoting.axis2.xml.OMElementDeserializer;
import org.seasar.remoting.axis2.xml.XMLBindException;

/**
 * RPC形式でのWebサービス呼び出しを行うコネクタです。
 * 
 * @author takanori
 */
public abstract class AbstractRPCConnector extends AbstractAxisConnector {

    /** 初期化フラグ */
    protected boolean                           isInitialized   = false;

    /** サービスの応答メッセージからオブジェクトを生成するデシアライザのマップ */
    protected Map<Class, OMElementDeserializer> deserializerMap = new HashMap<Class, OMElementDeserializer>();

    /**
     * デフォルトのコンストラクタ。
     */
    public AbstractRPCConnector() {}

    /**
     * このオブジェクトの初期化を行います。
     */
    protected void init() {

        if (this.isInitialized) {
            return;
        }

        if (super.options == null) {
            super.options = new Options();
        }

        super.options.setManageSession(true);

        // タイムアウトの設定
        if (super.timeout != null) {
            super.options.setTimeOutInMilliSeconds(super.timeout.longValue());

            // HTTPプロトコルに対する設定
            super.options.setProperty(HTTPConstants.SO_TIMEOUT, super.timeout);
            super.options.setProperty(HTTPConstants.CONNECTION_TIMEOUT,
                    super.timeout);
        }

        if (super.client == null) {
            try {
                super.client = new RPCServiceClient();
            } catch (AxisFault ex) {
                throw new S2AxisClientException("EAXS1001", null, ex);
            }
        }

        this.isInitialized = true;
    }

    /**
     * 共通の設定を行い、executeを呼び出します。
     * 
     * @param url 接続先のURL
     * @param method Webサービスの実行メソッド
     * @param args Webサービスの引数
     * @return Webサービスの呼び出し結果
     * @throws Throwable 通信に失敗した場合
     */
    @Override
    protected Object invoke(URL url, Method method, Object[] args)
            throws Throwable {

        init();

        // clientで利用するOptionsが競合してしまうため、
        // 送受信が完了するまで、このオブジェクトを排他する。
        synchronized (this) {
            // ローカル変数化するために、Optionsのディープコピーを行う。
            // propertyがコピーされないため、parentに指定する。
            // ServiceClientは、セッション情報がクリアされてしまうため、コピーしない。
            Options options = (Options)SerializeUtil.serialize(super.options);
            options.setParent(super.options);
            super.client.setOptions(options);

            // WS-Addressingを利用する場合の設定
            options.setAction("urn:" + method.getName());

            // エンドポイントの指定
            String localEPR = S2AxisClientContext.getEndpointURL();
            EndpointReference targetEPR;
            if (!StringUtil.isEmpty(localEPR)) {
                targetEPR = new EndpointReference(localEPR);
            } else {
                targetEPR = new EndpointReference(url.toString());
            }
            options.setTo(targetEPR);

            Object returnValue;
            try {
                returnValue = execute(method, args, options);
            } catch (Exception ex) {
                throw new S2AxisClientException("EAXS1002",
                        new Object[] { targetEPR }, ex);
            }

            return returnValue;
        }
    }

    /**
     * Webサービスを呼び出します。
     * 
     * @param method Webサービスの実行メソッド
     * @param args Webサービスの引数
     * @param options オプション
     * @return Webサービスの呼び出し結果
     * @throws Exception 通信に失敗した場合
     */
    abstract protected Object execute(Method method,
                                      Object[] args,
                                      Options options) throws Exception;

    /**
     * このオブジェクトが持つ、RPCServiceClientを返します。
     * 
     * @return RPCServiceClient
     */
    protected RPCServiceClient getClient() {
        return (RPCServiceClient)super.client;
    }

    /**
     * OMElemnt をデシアライズします。
     * 
     * @param returnType 戻り値の型
     * @param om OMElemnt
     * @return 戻り値
     * @throws XMLBindException
     */
    protected Object deserialize(Class returnType, OMElement om)
            throws XMLBindException {

        if (om == null || returnType.equals(void.class)) {
            return null;
        }

        Object result;

        OMElementDeserializer deserializer = this.deserializerMap.get(returnType);

        if (deserializer != null) {
            // Bind by deserializer
            result = deserializer.deserialize(om);
        } else if (returnType.equals(String.class)) {
            // Convert to String
            result = om.toString();
        } else if (returnType.isAssignableFrom(OMElement.class)) {
            result = om;
        } else {
            // Bind by BeanUtil(expect simple JavaBeans)
            try {
                Object[] response = BeanUtil.deserialize(om,
                        new Class[] { returnType }, new DefaultObjectSupplier());

                result = response[0];
            } catch (AxisFault ex) {
                throw new XMLBindException(ex);
            }
        }

        return result;
    }

    /**
     * Webサービスからの応答メッセージ（XMLデータ）をオブジェクトに変換するデシアライザを追加します。
     * 
     * @param clazz 戻り値の型となるクラス
     * @param deserializer デシアライザ
     */
    public void addDeserializer(Class clazz, OMElementDeserializer deserializer) {

        this.deserializerMap.put(clazz, deserializer);
    }

}
