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
package org.seasar.remoting.axis2.deployment.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.deployment.util.Utils;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.engine.MessageReceiver;
import org.apache.axis2.wsdl.WSDLConstants.WSDL20_2004Constants;
import org.apache.ws.java2wsdl.DefaultNamespaceGenerator;
import org.apache.ws.java2wsdl.Java2WSDLUtils;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.StringUtil;
import org.seasar.remoting.axis2.DeployFailedException;
import org.seasar.remoting.axis2.ServiceDef;
import org.seasar.remoting.axis2.deployment.ServiceBuilder;
import org.seasar.remoting.axis2.receivers.S2MessageReceiver;
import org.seasar.remoting.axis2.receivers.S2RPCInOnlyMessageReceiver;
import org.seasar.remoting.axis2.receivers.S2RPCMessageReceiver;
import org.seasar.remoting.axis2.receivers.ServiceHolder;

/**
 * 
 * @author takanori
 * 
 */
public class S2ServiceBuilderImpl implements ServiceBuilder {

    /** デフォルトのMessageReceiver */
    private Map                 defaultMessageReceivers = new HashMap();

    private static final Logger logger                  = Logger.getLogger(S2ServiceBuilderImpl.class);

    /**
     * デフォルトのコンストラクタ。
     */
    public S2ServiceBuilderImpl(ServiceHolder serviceHolder) {
        S2MessageReceiver ioReceiver = new S2RPCMessageReceiver();
        ioReceiver.setServiceHolder(serviceHolder);
        this.defaultMessageReceivers.put(WSDL20_2004Constants.MEP_URI_IN_OUT,
                ioReceiver);

        S2MessageReceiver inOnlyReceiver = new S2RPCInOnlyMessageReceiver();
        inOnlyReceiver.setServiceHolder(serviceHolder);
        this.defaultMessageReceivers.put(WSDL20_2004Constants.MEP_URI_IN_ONLY,
                inOnlyReceiver);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.remoting.axis2.deployment.ServiceBuilder#populateService(
     *      org.apache.axis2.engine.AxisConfiguration,
     *      org.seasar.framework.container.ComponentDef)
     */
    public AxisService populateService(AxisConfiguration axisConfig,
                                       ComponentDef componentDef) {

        AxisService service = populateService(axisConfig, componentDef, null);

        return service;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.remoting.axis2.deployment.ServiceBuilder#populateService(
     *      org.apache.axis2.engine.AxisConfiguration,
     *      org.seasar.framework.container.ComponentDef,
     *      org.seasar.remoting.axis2.ServiceDef)
     */
    public AxisService populateService(AxisConfiguration axisConfig,
                                       ComponentDef componentDef,
                                       ServiceDef serviceDef) {

        if (serviceDef == null) {
            serviceDef = new ServiceDef();
        }

        Class serviceClass = componentDef.getComponentClass();
        String className = serviceClass.getName();
        Parameter parameter = new Parameter(Constants.SERVICE_CLASS, className);
        AxisService service = new AxisService(componentDef.getComponentName());

        ClassLoader loader = axisConfig.getServiceClassLoader();
        
        // Service Interface
        Class serviceType = serviceDef.getServiceType();
        if (serviceType == null) {
            serviceType = getServiceType(serviceClass);
        }

        // targetNamespace
        String targetNamespace;
        if (StringUtil.isEmpty(serviceDef.getTargetNamespace()) == false) {
            targetNamespace = serviceDef.getTargetNamespace();
        } else {
            targetNamespace = createTargetNamespace(serviceType, loader);
        }
        service.setTargetNamespace(targetNamespace);

        // schemaNamespace
        String schemaNamespace;
        if (StringUtil.isEmpty(serviceDef.getSchemaNamespace()) == false) {
            schemaNamespace = serviceDef.getSchemaNamespace();
        } else {
            schemaNamespace = createSchemaNamespace(serviceType, loader);
        }
        service.setSchematargetNamespace(schemaNamespace);

        // MessageReceiver
        addMessageReceiver(service, this.defaultMessageReceivers);
        Map msgReceivers = serviceDef.getMessageReceivers();
        if (msgReceivers != null) {
            addMessageReceiver(service, msgReceivers);
        }

        List excludeOperations = createExcludeOperations(serviceClass,
                serviceType);
        excludeOperations.addAll(serviceDef.getExcludeOperations());

        // サービスの生成
        try {
            service.addParameter(parameter);
            service.setClassLoader(axisConfig.getServiceClassLoader());

            Utils.fillAxisService(service, axisConfig, new ArrayList(
                    excludeOperations));
        } catch (AxisFault ex) {
            throw new DeployFailedException("EAXS0003",
                    new Object[] { service.getName() }, ex);
        } catch (Exception ex) {
            throw new DeployFailedException("EAXS0003",
                    new Object[] { service.getName() }, ex);
        }

        // 除外メソッドの削除
        for (int i = 0; i < excludeOperations.size(); i++) {
            String opName = (String) excludeOperations.get(i);
            service.removeOperation(new QName(opName));
        }

        // WSDLの公開設定
        service.setWsdlFound(true);

        return service;
    }

    /**
     * サービスの実装クラスから、公開するサービスクラスの型を取得します。
     * 
     * @param serviceClass サービスの実装クラス
     * @return 公開するサービスクラスの型
     */
    protected Class getServiceType(Class serviceClass) {
        Class serviceType;

        if (serviceClass.isInterface() == false) {
            Class[] interfaces = serviceClass.getInterfaces();

            switch (interfaces.length) {
            case 0:
                serviceType = serviceClass;
                break;
            case 1:
                serviceType = (Class) interfaces[0];
                break;
            default:
                serviceType = serviceClass;
            }
        } else {
            serviceType = serviceClass;
        }

        return serviceType;
    }

    /**
     * サービスの実装クラスから、公開するサービスクラスのメソッドを除いたリストを返します。<br>
     * 非公開とするメソッド名を特定するのに利用します。
     * 
     * @param serviceClass サービスの実装クラス
     * @param serviceType 公開するサービスクラス（インタフェース）
     * @return メソッドの除外リスト
     */
    protected List createExcludeOperations(Class serviceClass, Class serviceType) {

        if (serviceType == null) {
            return new ArrayList();
        }

        // 実装クラスと公開するクラスが同じ場合は、空のリストを返す。
        if (serviceClass == serviceType) {
            return new ArrayList();
        }

        // サービスクラスが、公開するサービスクラスを、
        // 実装しているかどうかを確認する。
        boolean isAssignable = serviceType.isAssignableFrom(serviceClass);
        if (isAssignable == false) {
            Object[] args = new Object[] { serviceClass, serviceType };
            throw new DeployFailedException("EAXS0004", args);
        }

        Set excludes = new HashSet();

        // サービスクラスの全メソッド名を抽出する。
        // 同じメソッド名で引数が異なるものは、公開対象から除かれる。
        Method[] classMethods = serviceClass.getDeclaredMethods();
        for (int i = 0; i < classMethods.length; i++) {
            String classMethodName = classMethods[i].getName();
            excludes.add(classMethodName);
        }

        // 公開するサービスクラスのメソッド名は、除外リストから削除する。
        Method[] typeMethods = serviceType.getMethods();
        for (int i = 0; i < typeMethods.length; i++) {
            String typeMethodName = typeMethods[i].getName();
            excludes.remove(typeMethodName);
        }

        return new ArrayList(excludes);
    }

    /**
     * サービスクラスから、TargetNamespaceを生成します。
     * 
     * @param serviceClass サービスクラス
     * @param loader クラスローダー
     * @return TargetNamespace
     */
    protected String createTargetNamespace(Class serviceClass,
                                           ClassLoader loader) {
        String className = serviceClass.getName();

        StringBuffer nsBuff;
        try {
            nsBuff = Java2WSDLUtils.targetNamespaceFromClassName(className,
                    loader, new DefaultNamespaceGenerator());
        } catch (Exception ex) {
            throw new DeployFailedException("EAXS0003",
                    new Object[] { className }, ex);
        }
        String targetNamespace = nsBuff.toString();

        return targetNamespace;
    }

    /**
     * サービスクラスから、スキーマの名前空間を生成します。
     * 
     * @param serviceClass サービスクラス
     * @param loader クラスローダー
     * @return スキーマの名前空間
     */
    protected String createSchemaNamespace(Class serviceClass,
                                           ClassLoader loader) {
        String className = serviceClass.getName();

        StringBuffer nsBuff;
        try {
            nsBuff = Java2WSDLUtils.schemaNamespaceFromClassName(className,
                    loader);
        } catch (Exception ex) {
            throw new DeployFailedException("EAXS0003",
                    new Object[] { className }, ex);
        }
        String schemaNamespace = nsBuff.toString();

        return schemaNamespace;
    }

    /**
     * 指定されたAxisServiceに、MessageReceiverを追加します。<br>
     * 
     * @param service AxisService
     * @param msgReceivers MessageReceiverのマップ
     */
    private void addMessageReceiver(AxisService service, Map msgReceivers) {
        if (service == null || msgReceivers == null) {
            return;
        }

        Object[] mepArray = msgReceivers.keySet().toArray();
        for (int i = 0; i < mepArray.length; i++) {
            Object key = mepArray[i];
            Object value = msgReceivers.get(key);

            if ((key instanceof String) && (value instanceof MessageReceiver)) {
                service.addMessageReceiver((String) key,
                        (MessageReceiver) value);

                if (logger.isDebugEnabled()) {
                    Object[] args = new Object[] { service.getName(), key,
                            value };
                    logger.log("DAXS0005", args);
                }
            }

        }
    }

}
