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
package org.seasar.remoting.axis2.deployment.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.deployment.util.Utils;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.description.java2wsdl.DefaultNamespaceGenerator;
import org.apache.axis2.description.java2wsdl.Java2WSDLUtils;
import org.apache.axis2.engine.AxisConfiguration;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.util.StringUtil;
import org.seasar.remoting.axis2.S2AxisConstants;
import org.seasar.remoting.axis2.ServiceDef;
import org.seasar.remoting.axis2.deployer.DeployFailedException;
import org.seasar.remoting.axis2.deployment.AbstractServiceBuilder;
import org.seasar.remoting.axis2.deployment.ComponentBasedServiceBuilder;

/**
 * コンポーネントベースでサービスを構築するクラスです。
 * 
 * @author takanori
 * 
 */
public class ComponentBasedServiceBuilderImpl extends AbstractServiceBuilder
        implements ComponentBasedServiceBuilder {

    /**
     * デフォルトのコンストラクタ。
     */
    public ComponentBasedServiceBuilderImpl() {}

    /**
     * {@inheritDoc}
     */
    public AxisService populateService(ConfigurationContext configCtx,
                                       ComponentDef componentDef) {

        AxisService service = populateService(configCtx, componentDef, null);

        return service;
    }

    /**
     * {@inheritDoc}
     */
    public AxisService populateService(ConfigurationContext configCtx,
                                       ComponentDef componentDef,
                                       ServiceDef serviceDef) {

        if (serviceDef == null) {
            serviceDef = new ServiceDef();
        }

        String serviceName = componentDef.getComponentName();

        AxisConfiguration axisConfig = configCtx.getAxisConfiguration();
        ClassLoader loader = axisConfig.getServiceClassLoader();

        // Parameter
        Map<String, Object> parameterMap = serviceDef.getParameterMap();

        // AxisServiceの生成
        // カスタム WSDLの適用をチェックする。
        //   →適用する場合は、WSDLファイルを基に生成。
        //   →適用しない場合は、サービス名のみを指定して生成。
        AxisService service;
        if (parameterMap != null && parameterMap.size() > 0) {
            Object useOriginalwsdlValue = parameterMap.get(S2AxisConstants.PARAM_KEY_USE_ORIGINAL_WSDL);

            boolean useOriginalwsdl;
            if (useOriginalwsdlValue == null) {
                useOriginalwsdl = false;
            } else if (useOriginalwsdlValue instanceof Boolean) {
                useOriginalwsdl = ((Boolean)useOriginalwsdlValue).booleanValue();
            } else {
                useOriginalwsdl = Boolean.valueOf(useOriginalwsdlValue.toString());
            }

            if (useOriginalwsdl) {
                service = createWsdlService(serviceName);
            } else {
                service = new AxisService(serviceName);
            }

            // Parameterの適用
            try {
                Set<String> keySet = parameterMap.keySet();
                String[] keyArray = keySet.toArray(new String[0]);
                for (int index = 0; index < keyArray.length; index++) {
                    String name = keyArray[index];
                    Object value = parameterMap.get(name);
                    service.addParameter(name, value);
                }
            } catch (AxisFault ex) {
                throw new DeployFailedException("EAXS0003",
                        new Object[] { service.getName() }, ex);
            }
        } else {
            service = new AxisService(serviceName);
        }

        buildMessageBuilder(axisConfig);
        buildServiceParameter(service);

        // ServiceClass
        Class serviceClass = componentDef.getComponentClass();
        String className = serviceClass.getName();
        Parameter paramServiceClass = new Parameter(Constants.SERVICE_CLASS,
                className);
        try {
            service.addParameter(paramServiceClass);
        } catch (AxisFault ex) {
            throw new DeployFailedException("EAXS0003",
                    new Object[] { service.getName() }, ex);
        }

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
        service.setSchemaTargetNamespace(schemaNamespace);

        // MessageReceiver
        addMessageReceiver(service, createMessageReceiverDefAtRPC());
        Map<String, Class> msgReceivers = serviceDef.getMessageReceivers();
        if (msgReceivers != null) {
            addMessageReceiver(service, msgReceivers);
        }

        // exclude method
        List<String> excludeOperations = createExcludeOperations(serviceClass,
                serviceType);
        excludeOperations.addAll(serviceDef.getExcludeOperations());

        // サービスの生成
        try {
            Utils.fillAxisService(service, axisConfig, new ArrayList<String>(
                    excludeOperations), new ArrayList());
        } catch (AxisFault ex) {
            throw new DeployFailedException("EAXS0003",
                    new Object[] { service.getName() }, ex);
        } catch (Exception ex) {
            throw new DeployFailedException("EAXS0003",
                    new Object[] { service.getName() }, ex);
        }

        // 除外メソッドの削除
        for (int i = 0; i < excludeOperations.size(); i++) {
            String opName = excludeOperations.get(i);
            service.removeOperation(new QName(opName));
        }

        return service;
    }

    /**
     * サービスの実装クラスから、公開するサービスクラスの型を取得します。
     * 
     * @param serviceClass
     *            サービスの実装クラス
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
                serviceType = interfaces[0];
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
     * @param serviceClass
     *            サービスの実装クラス
     * @param serviceType
     *            公開するサービスクラス（インタフェース）
     * @return メソッドの除外リスト
     */
    protected List<String> createExcludeOperations(Class serviceClass,
                                                   Class serviceType) {

        if (serviceType == null) {
            return new ArrayList<String>();
        }

        // 実装クラスと公開するクラスが同じ場合は、空のリストを返す。
        if (serviceClass == serviceType) {
            return new ArrayList<String>();
        }

        // サービスクラスが、公開するサービスクラスを、
        // 実装しているかどうかを確認する。
        boolean isAssignable = serviceType.isAssignableFrom(serviceClass);
        if (isAssignable == false) {
            Object[] args = new Object[] { serviceClass, serviceType };
            throw new DeployFailedException("EAXS0004", args);
        }

        Set<String> excludes = new HashSet<String>();

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

        return new ArrayList<String>(excludes);
    }

    /**
     * サービスクラスから、TargetNamespaceを生成します。
     * 
     * @param serviceClass
     *            サービスクラス
     * @param loader
     *            クラスローダー
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
     * @param serviceClass
     *            サービスクラス
     * @param loader
     *            クラスローダー
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

}
