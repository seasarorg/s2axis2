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
import java.util.Map;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.RESTCall;
import org.apache.commons.beanutils.BeanUtils;
import org.seasar.remoting.common.connector.impl.TargetSpecificURLBasedConnector;

/**
 * 
 * @author takanori
 * 
 */
public class RESTConnector extends TargetSpecificURLBasedConnector {

    public static final String DEFAULT_ENCODE = "UTF-8";

    public RESTConnector() {}

    protected Object invoke(URL url, Method method, Object[] args)
            throws Throwable {

        String targetUrl = target(args);
        EndpointReference targetEPR = new EndpointReference(targetUrl);

        Options options = new Options();
        options.setTo(targetEPR);
        options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
        options.setProperty(Constants.Configuration.ENABLE_REST,
                            Constants.VALUE_TRUE);
        options.setProperty(Constants.Configuration.ENABLE_REST_THROUGH_GET,
                            Constants.VALUE_TRUE);

        RESTCall call = new RESTCall();
        call.setOptions(options);

        OMElement response = call.sendReceive();

        Class retunType = method.getReturnType();
        Object result;
        if (retunType.equals(String.class)) {
            result = response.toString();
        }
        else {
            result = response;
        }

        return result;
    }

    protected String target(Object[] args)
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

    protected String createRestParameter(Object bean)
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
                buff.append(URLEncoder.encode(value.toString(), DEFAULT_ENCODE));
            }
        }

        String restParam = buff.toString();

        return restParam.toString();
    }

}
