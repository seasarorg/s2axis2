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

/**
 * S2Axis2で利用する定数です。
 * 
 * @author takanori
 * 
 */
public interface S2AxisConstants {

    /**
     * オペレーション名の名前空間URI
     */
    String OPERATION_NAMESPACE_URI    = "http://soapinterop.org/";

    /**
     * diconファイルで、使用するAxisエンジンを指示するために <code>&lt;meta&gt;</code> 要素に指定する
     * <code>name</code> 属性値のローカル名(接頭辞 <code>"axis-"</code> の後ろ)です。
     */
    String META_ENGINE                = "engine";

    /**
     * diconファイルで、WSDDファイルをデプロイするを指示するために <code>&lt;meta&gt;</code> 要素に指定する
     * <code>name</code> 属性値のローカル名(接頭辞 <code>"axis-"</code> の後ろ)です。
     */
    String META_DEPLOY                = "deploy";

    /**
     * diconファイルで、コンポーネントがAxisサービスであることを示すために <code>&lt;meta&gt;</code>
     * 要素に指定する <code>name</code> 属性値のローカル名(接頭辞 <code>"axis-"</code> の後ろ)です。
     */
    String META_SERVICE               = "service";

    /**
     * diconファイルで、コンポーネントがAxisハンドラであることを示すために <code>&lt;meta&gt;</code>
     * 要素に指定する <code>name</code> 属性値のローカル名(接頭辞 <code>"axis-"</code> の後ろ)です。
     */
    String META_HANDLER               = "handler";

    /**
     * S2Axisがインスタンス管理を行うRPCプロバイダのローカル名です。
     */
    String PROVIDER_S2RPC             = "S2RPC";

    /**
     * S2Axisがインスタンス管理を行うMSGプロバイダのローカル名です。
     */
    String PROVIDER_S2MSG             = "S2MSG";

    /**
     * デフォルトのAxisエンジンを使用することを示します。
     */
    String ENGINE_DEFAULT             = "default";

    /**
     * デフォルトのAxisクライアントエンジンを使用することを示します。
     */
    String ENGINE_DEFAULT_CLIENT      = "default-client";

    /**
     * デフォルトのAxisサーバエンジンを使用することを示します。
     */
    String ENGINE_DEFAULT_SERVER      = "default-server";

    /**
     * サーブレットコンテキストからAxisエンジンを取得することを示します。
     */
    String ENGINE_FROM_SERVLET        = "servlet:";

    /** S2コンテナからAxisエンジンを取得することを示します。 */
    String ENGINE_FROM_S2CONTAINER    = "s2:";

    /** AxisServletのサーブレット名 */
    String AXIS_SERVLET               = "AxisServlet";

    /** サーブレットコンテキストからConfigurationContextを取得するためのキー */
    String ATTR_CONFIGURATION_CONTEXT = "CONFIGURATION_CONTEXT";
}
