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

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.databinding.utils.BeanUtil;
import org.apache.axis2.engine.DefaultObjectSupplier;
import org.apache.commons.beanutils.BeanUtils;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.S2Container;
import org.seasar.remoting.axis2.annotation.AnnotationReaderFactory;
import org.seasar.remoting.axis2.annotation.BeanAnnotationReader;
import org.seasar.remoting.axis2.annotation.impl.AnnotationReaderFactoryImpl;
import org.seasar.remoting.axis2.xml.OMElementDeserializer;
import org.seasar.remoting.axis2.xml.XMLBindException;
import org.seasar.remoting.common.connector.impl.TargetSpecificURLBasedConnector;

/**
 * RESTをサポートしているWebサービスと通信するためのコネクタです。
 * 
 * @author takanori
 */
public class RESTConnector extends TargetSpecificURLBasedConnector {

    /** GETを行う際の指定子 */
    public static final String      REST_GET_OPERATION      = "get";

    /** POSTを行う際の指定子 */
    public static final String      REST_POST_OPERATION     = "post";

    /** エンコード */
    private String                  encode                  = "UTF-8";

    /** RESTのメッセージを生成するデシアライザのマップ */
    private Map                     deserializerMap         = new HashMap();

    private S2Container             container;

    private AnnotationReaderFactory annotationReaderFactory = new AnnotationReaderFactoryImpl();

    private BeanAnnotationReader    reader;

    /**
     * デフォルトのコンストラクタ。
     */
    public RESTConnector() {}

    /**
     * RESTサービスを呼び出します。
     * 
     * @param url 呼び出し先のURL
     * @param method 呼び出し先のメソッド
     * @param args リクエストに設定するパラメータ
     * @return RESTサービスの呼び出し結果
     */
    protected Object invoke(URL url, Method method, Object[] args)
            throws Throwable {

        EndpointReference targetEPR = new EndpointReference(
                this.baseURL.toString());

        Options options = createOptions(method.getName());
        options.setTo(targetEPR);

        ServiceClient client = new ServiceClient();
        client.setOptions(options);

        OMElement request = createRequest(method.getName(), args);
        OMElement response = client.sendReceive(request);

        Class returnType = method.getReturnType();
        Object result = deserialize(returnType, response);

        return result;
    }

    /**
     * REST形式の呼び出しを行う際のAxis2のOptionを生成します。
     * 
     * @param methodName メソッド名
     * @return Axis2のオプション
     */
    private Options createOptions(String methodName) {
        Options options = new Options();
        options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
        options.setProperty(Constants.Configuration.ENABLE_REST,
                Constants.VALUE_TRUE);
        options.setProperty(Constants.Configuration.ENABLE_REST_THROUGH_GET,
                Constants.VALUE_TRUE);

        String opeProp;
        if (methodName.toLowerCase().startsWith(REST_POST_OPERATION)) {
            opeProp = Constants.Configuration.HTTP_METHOD_POST;
        } else {
            opeProp = Constants.Configuration.HTTP_METHOD_GET;
        }
        options.setProperty(Constants.Configuration.HTTP_METHOD, opeProp);

        return options;
    }

    /**
     * RESTリクエストを作成します。
     * 
     * @param methodName メソッド名
     * @param args リクエストに設定するパラメータ
     * @return RESTリクエスト
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws UnsupportedEncodingException
     */
    protected OMElement createRequest(String methodName, Object[] args)
            throws UnsupportedEncodingException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMElement root = fac.createOMElement(methodName, null);

        if (args == null || args.length <= 0) {
            // do nothing
        } else if (args.length == 1) {
            fillRestParameter(fac, root, args[0]);
        } else {
            throw new IllegalArgumentException(
                    "argument num is too much. it must be 0 or 1.");
        }

        return root;
    }

    /**
     * RESTリクエストを作成します。
     * 
     * @param fac OMFactory
     * @param root ルートエレメント
     * @param bean パラメータを保持するオブジェクト
     * @return RESTリクエスト
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws UnsupportedEncodingException
     */
    private void fillRestParameter(OMFactory fac, OMElement root, Object bean)
            throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException, UnsupportedEncodingException {

        Map paramMap = BeanUtils.describe(bean);

        this.reader = createBeanAnnotationReader(bean.getClass());
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(bean.getClass());
        for (int i = 0; i < beanDesc.getPropertyDescSize(); i++) {
            PropertyDesc pd = beanDesc.getPropertyDesc(i);
            String key = pd.getPropertyName();
            Object value = paramMap.get(key);
            String newKey = getParameterName(pd);
            paramMap.remove(key);
            paramMap.put(newKey, value);
        }

        String[] keys = (String[]) paramMap.keySet().toArray(new String[0]);
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            Object value = paramMap.get(key);

            if ((!key.equals("class")) && (value != null)) {
                OMElement query = fac.createOMElement(key, null, root);
                String text = URLEncoder.encode(value.toString(), encode);
                query.setText(text);
            }
        }

        return;
    }

    /**
     * BeanAnnotationReaderを生成します。
     * 
     * @param bean
     * @return BeanAnnotationReader
     */
    private BeanAnnotationReader createBeanAnnotationReader(Class bean) {
        BeanAnnotationReader reader;

        if (this.annotationReaderFactory == null) {
            this.annotationReaderFactory = (AnnotationReaderFactory) this.container.getComponent(AnnotationReaderFactory.class);
        }

        if (this.annotationReaderFactory != null) {
            reader = this.annotationReaderFactory.createBeanAnnotationReader(bean);
        } else {
            reader = null;
        }

        return reader;
    }

    /**
     * RESTリクエストで指定するパラメータ名を取得します。 <br>
     * アノテーションが指定されている場合は、そのパラメータを取得します。
     * 
     * @param propertyDesc
     * @return パラメータ名
     */
    private String getParameterName(PropertyDesc propertyDesc) {
        String ca;

        if (this.reader != null) {
            ca = this.reader.getParameterAnnotation(propertyDesc);
        } else {
            ca = null;
        }

        if (ca == null) {
            ca = propertyDesc.getPropertyName();
        }
        return ca;
    }

    /**
     * OMElemnt をデシアライズします。
     * 
     * @param returnType 戻り値の型
     * @param om OMElemnt
     * @return 戻り値
     * @throws XMLBindException
     */
    private Object deserialize(Class returnType, OMElement om)
            throws XMLBindException {

        Object result;

        OMElementDeserializer deserializer = (OMElementDeserializer) this.deserializerMap.get(returnType);

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
                result = BeanUtil.deserialize(returnType, om,
                        new DefaultObjectSupplier(), null);
            } catch (AxisFault ex) {
                throw new XMLBindException(ex);
            }
        }

        return result;
    }

    public void addUnmarshaller(Class clazz, OMElementDeserializer deserializer) {
        this.deserializerMap.put(clazz, deserializer);
    }

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public void setContainer(S2Container container) {
        this.container = container;
    }

}
