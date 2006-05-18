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
import org.seasar.remoting.axis2.util.OMElementUtil;
import org.seasar.remoting.axis2.xml.OMElementDeserializer;
import org.seasar.remoting.axis2.xml.XMLBindException;
import org.seasar.remoting.common.connector.impl.TargetSpecificURLBasedConnector;

/**
 * 
 * @author takanori
 * 
 */
public class RESTConnector extends TargetSpecificURLBasedConnector {

    private static final String REST_GET_OPERATION  = "get";

    private static final String REST_POST_OPERATION = "post";

    private String              encode              = OMElementUtil.DEFAULT_ENCODE;

    private Map                 deserializerMap     = new HashMap();

    public RESTConnector() {}

    protected Object invoke(URL url, Method method, Object[] args)
            throws Throwable {

        String targetUrl = createTarget(args);
        EndpointReference targetEPR = new EndpointReference(targetUrl);

        Options options = createOptions(method.getName());
        options.setTo(targetEPR);

        RESTCall call = new RESTCall();
        call.setOptions(options);

        OMElement response = call.sendReceive();

        Class returnType = method.getReturnType();
        Object result = deserialize(returnType, response);

        return result;
    }

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
        }
        else {
            opeProp = Constants.Configuration.HTTP_METHOD_GET;
        }
        options.setProperty(Constants.Configuration.HTTP_METHOD, opeProp);

        return options;
    }

    protected String createTarget(Object[] args)
            throws IllegalArgumentException,
                IllegalAccessException,
                InvocationTargetException,
                NoSuchMethodException,
                UnsupportedEncodingException {

        String target;

        if (args == null || args.length <= 0) {
            target = this.baseURL.toString();
        }
        else if (args.length == 1) {
            StringBuffer buff = new StringBuffer(this.baseURL.toString());
            buff.append("?");
            buff.append(createRestParameter(args[0]));

            target = buff.toString();
        }
        else {
            throw new IllegalArgumentException("argument num is too much. it must be 0 or 1.");
        }

        return target;
    }

    private String createRestParameter(Object bean)
            throws IllegalAccessException,
                InvocationTargetException,
                NoSuchMethodException,
                UnsupportedEncodingException {
        Map paramMap = BeanUtils.describe(bean);

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

    private Object deserialize(Class returnType, OMElement om)
            throws XMLBindException {

        Object result;

        OMElementDeserializer deserializer = (OMElementDeserializer) this.deserializerMap.get(returnType);

        if (deserializer != null) {
            // Bind by deserializer
            result = deserializer.deserialize(om);
        }
        else if (returnType.equals(String.class)) {
            // Convert to String
            result = OMElementUtil.toString(om);
        }
        else if (returnType.isAssignableFrom(OMElement.class)) {
            result = om;
        }
        else {
            // Bind by BeanUtil(expect simple JavaBeans)
            try {
                result = BeanUtil.deserialize(returnType, om);
            }
            catch (AxisFault ex) {
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

}
