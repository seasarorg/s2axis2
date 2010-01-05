/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
    private Class               serviceType       = null;

    /** スキーマ定義をする際に対象となる名前空間 */
    private String              targetNamespace   = null;

    /** スキーマの名前空間 */
    private String              schemaNamespace   = null;

    /** サービスで公開しないメソッドのリスト */
    private List<String>        excludeOperations = new ArrayList<String>();

    /** サービスがデフォルトで使用するMessageReceiverのマップ */
    private Map<String, Class>  messageReceivers  = new HashMap<String, Class>();

    /** サービスに適用するパラメータのマップ */
    private Map<String, Object> parameterMap      = new HashMap<String, Object>();

    /**
     * コンストラクタ。
     */
    public ServiceDef() {}

    /**
     * サービスとして公開するクラス／インタフェースの型を取得します。
     * 
     * @return サービスとして公開するクラス／インタフェースの型
     */
    public Class getServiceType() {
        return this.serviceType;
    }

    /**
     * サービスとして公開するクラス／インタフェースの型を設定します。
     * 
     * @param serviceType サービスとして公開するクラス／インタフェースの型
     */
    public void setServiceType(Class serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * スキーマ定義をする際に対象となる名前空間を取得します。
     * 
     * @return スキーマ定義をする際に対象となる名前空間
     */
    public String getTargetNamespace() {
        return this.targetNamespace;
    }

    /**
     * スキーマ定義をする際に対象となる名前空間を設定します。
     * 
     * @param targetNamespace スキーマ定義をする際に対象となる名前空間
     */
    public void setTargetNamespace(String targetNamespace) {
        this.targetNamespace = targetNamespace;
    }

    /**
     * スキーマの名前空間を取得します。
     * 
     * @return スキーマの名前空間
     */
    public String getSchemaNamespace() {
        return this.schemaNamespace;
    }

    /**
     * スキーマの名前空間を設定します。
     * 
     * @param schema スキーマの名前空間
     */
    public void setSchemaNamespace(String schema) {
        this.schemaNamespace = schema;
    }

    /**
     * サービスで公開しないメソッドのリストを取得します。
     * 
     * @return サービスで公開しないメソッドのリスト
     */
    public List<String> getExcludeOperations() {
        return this.excludeOperations;
    }

    /**
     * サービスで公開しないメソッドのリストを設定します。
     * 
     * @param excludeOperations サービスで公開しないメソッドのリスト
     */
    public void setExcludeOperations(List<String> excludeOperations) {
        this.excludeOperations = excludeOperations;
    }

    /**
     * サービスで公開しないメソッドのリストを追加します。
     * 
     * @param operatin サービスで公開しないメソッド
     */
    public void addExcludeOperation(String operatin) {
        if (this.excludeOperations == null) {
            this.excludeOperations = new ArrayList<String>();
        }

        this.excludeOperations.add(operatin);
    }

    /**
     * サービスがデフォルトで使用するMessageReceiverを取得します。
     * 
     * @return サービスがデフォルトで使用するMessageReceiver
     */
    public Map<String, Class> getMessageReceivers() {
        return this.messageReceivers;
    }

    /**
     * サービスがデフォルトで使用するMessageReceiverを追加します。
     * 
     * @param mep MEP
     * @param receiverClass MessageReceiverのクラス
     */
    public void addMessageReceiver(String mep, Class receiverClass) {
        if (this.messageReceivers == null) {
            this.messageReceivers = new HashMap<String, Class>();
        }

        this.messageReceivers.put(mep, receiverClass);
    }

    /**
     * サービスに適用するパラメータのマップを取得します。
     * 
     * @return パラメータのマップ
     */
    public Map<String, Object> getParameterMap() {
        return this.parameterMap;
    }

    /**
     * サービスに適用するパラメータを追加します。
     * 
     * @param name パラメータ名
     * @param value パラメータ値
     */
    public void addParameter(String name, Object value) {
        if (this.parameterMap == null) {
            this.parameterMap = new HashMap<String, Object>();
        }

        this.parameterMap.put(name, value);
    }

}
