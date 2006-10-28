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

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.RESTCall;
import org.apache.axis2.databinding.utils.BeanUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.S2Container;
import org.seasar.remoting.axis2.annotation.AnnotationReaderFactory;
import org.seasar.remoting.axis2.annotation.BeanAnnotationReader;
import org.seasar.remoting.axis2.annotation.impl.AnnotationReaderFactoryImpl;
import org.seasar.remoting.axis2.util.OMElementUtil;
import org.seasar.remoting.axis2.xml.OMElementDeserializer;
import org.seasar.remoting.axis2.xml.XMLBindException;

/**
 * RESTをサポートしているWebサービスと通信するためのコネクタです。
 * 
 * @author takanori
 */
public class RESTConnector extends AbstractAxisConnector {

    /** GETを行う際の指定子 */
    public static final String      REST_GET_OPERATION      = "get";

    /** POSTを行う際の指定子 */
    public static final String      REST_POST_OPERATION     = "post";

    /** エンコード */
    private String                  encode                  = OMElementUtil.DEFAULT_ENCODE;

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
        
        if (super.options == null) {
            super.options = new Options();
        }

        fillRestOptions(super.options, method.getName());

        String targetUrl = createTarget(args);
        EndpointReference targetEPR = new EndpointReference(targetUrl);
        super.options.setTo(targetEPR);

        RESTCall call = new RESTCall();
        call.setOptions(super.options);

        OMElement response = call.sendReceive();

        Class returnType = method.getReturnType();
        Object result = deserialize(returnType, response);

        return result;
    }

    /**
     * REST形式の呼び出しを行う際のAxis2のOptionを設定します。
     * 
     * @param options Axis2のOption
     * @param methodName メソッド名
     */
    protected void fillRestOptions(Options options, String methodName) {
        if (options == null) {
            return;
        }

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
    }

    /**
     * RESTリクエストを生成します。
     * 
     * @param args リクエストに設定するパラメータ
     * @return RESTリクエスト（指定するURL）
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws UnsupportedEncodingException
     */
    protected String createTarget(Object[] args)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException,
            UnsupportedEncodingException {

        String target;

        if (args == null || args.length <= 0) {
            target = this.baseURL.toString();
        } else if (args.length == 1) {
            StringBuffer buff = new StringBuffer(this.baseURL.toString());
            buff.append("?");
            buff.append(createRestParameter(args[0]));

            target = buff.toString();
        } else {
            throw new IllegalArgumentException(
                    "argument num is too much. it must be 0 or 1.");
        }

        return target;
    }

    /**
     * RESTリクエストを作成します。
     * 
     * @param bean パラメータを保持するオブジェクト
     * @return RESTリクエスト
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws UnsupportedEncodingException
     */
    private String createRestParameter(Object bean)
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

        StringBuffer buff = new StringBuffer();
        Object[] keys = paramMap.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            Object key = keys[i];
            Object value = paramMap.get(key);

            if ((!key.equals("class")) && (value != null)) {
                if (i > 0) {
                    buff.append("&");
                }

                buff.append(key);
                buff.append("=");
                buff.append(URLEncoder.encode(value.toString(), encode));
            }
        }

        String restParam = buff.toString();

        return restParam.toString();
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
            result = OMElementUtil.toString(om);
        } else if (returnType.isAssignableFrom(OMElement.class)) {
            result = om;
        } else {
            // Bind by BeanUtil(expect simple JavaBeans)
            try {
                result = BeanUtil.deserialize(returnType, om);
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
