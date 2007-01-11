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
package org.seasar.remoting.axis2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.remoting.axis2.receivers.S2MessageReceiver;

/**
 * 
 * サービスの設定情報を保持するクラスです。<br>
 * 以下の項目を保持します。<br>
 * <dl>
 * <dt>serviceType</dt>
 * <dd>サービスとして公開するクラス／インタフェースの型</dd>
 * <dt>targetNamespace</dt>
 * <dd>スキーマ定義をする際に対象となる名前空間</dd>
 * <dt>schemaNamespace</dt>
 * <dd>スキーマの名前空間</dd>
 * <dt>excludeOperations</dt>
 * <dd>サービスで公開しないメソッド</dd>
 * <dt>messageReceivers</dt>
 * <dd>サービスがデフォルトで使用するMessageReceiver</dd>
 * </dl>
 * 
 * @author takanori
 */
public class ServiceDef {

    /** サービスとして公開するクラス／インタフェースの型 */
    private Class  serviceType       = null;

    /** スキーマ定義をする際に対象となる名前空間 */
    private String targetNamespace   = null;

    /** スキーマの名前空間 */
    private String schemaNamespace   = null;

    /** サービスで公開しないメソッドのリスト */
    private List   excludeOperations = new ArrayList();

    /** サービスがデフォルトで使用するMessageReceiverのマップ */
    private Map    messageReceivers  = new HashMap();

    public ServiceDef() {}

    public Class getServiceType() {
        return serviceType;
    }

    public void setServiceType(Class serviceType) {
        this.serviceType = serviceType;
    }

    public String getTargetNamespace() {
        return targetNamespace;
    }

    public void setTargetNamespace(String targetNamespace) {
        this.targetNamespace = targetNamespace;
    }

    public String getSchemaNamespace() {
        return schemaNamespace;
    }

    public void setSchemaNamespace(String schema) {
        this.schemaNamespace = schema;
    }

    public List getExcludeOperations() {
        return excludeOperations;
    }

    public void setExcludeOperations(List excludeOperations) {
        this.excludeOperations = excludeOperations;
    }

    public void addExcludeOperation(String operatin) {
        if (this.excludeOperations == null) {
            this.excludeOperations = new ArrayList();
        }

        this.excludeOperations.add(operatin);
    }

    public Map getMessageReceivers() {
        return messageReceivers;
    }

    public void addMessageReceiver(String mep, S2MessageReceiver receiver) {
        if (this.messageReceivers == null) {
            this.messageReceivers = new HashMap();
        }

        this.messageReceivers.put(mep, receiver);
    }

}
