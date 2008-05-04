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
package org.seasar.remoting.axis2;

import org.apache.axis2.deployment.DeploymentConstants;

/**
 * S2Axis2で利用する定数です。
 * 
 * @author takanori
 * 
 */
public interface S2AxisConstants {

    /**
     * diconファイルで、services.xmlをデプロイするを指示するために <code>&lt;meta&gt;</code> 要素に指定する
     * <code>name</code> 属性値のローカル名(接頭辞 <code>"axis-"</code> の後ろ)です。
     */
    public static final String META_DEPLOY                 = "deploy";

    /**
     * diconファイルで、コンポーネントがAxisサービスであることを示すために <code>&lt;meta&gt;</code>
     * 要素に指定する <code>name</code> 属性値のローカル名(接頭辞 <code>"axis-"</code> の後ろ)です。
     */
    public static final String META_SERVICE                = "service";

    /** サーブレットコンテキストからConfigurationContextを取得するためのキー */
    public static final String ATTR_CONFIGURATION_CONTEXT  = "CONFIGURATION_CONTEXT";

    /** サービスのDeployerのキー : service class */
    public static final String DEPLOYER_SERCIE_CLASS       = "serviceClass";

    /** サービスのDeployerのキー ： services.xml */
    public static final String DEPLOYER_SERICES_XML        = "servicesXml";

    /** サービスのDeployerのキー ： jax-ws */
    public static final String DEPLOYER_JWS                = "jws";

    /** WSDLファイルを配置するパス  */
    public static final String WSDL_DIR                    = DeploymentConstants.META_INF;

    /** AxisServiceに適用するパラメータキー ： カスタムWSDLの適用 */
    public static final String PARAM_KEY_USE_ORIGINAL_WSDL = "useOriginalwsdl";
}
