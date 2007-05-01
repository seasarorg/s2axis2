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
package org.seasar.remoting.axis2.deployment;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisService;
import org.seasar.framework.container.ComponentDef;
import org.seasar.remoting.axis2.ServiceDef;

/**
 * AxisServiceを構築するクラスです。
 * 
 * @author takanori
 * 
 */
public interface ComponentBasedServiceBuilder {

    /**
     * Axis2およびS2コンテナのコンポーネント定義を指定して、 AxisServiceを構築します。
     * 
     * @param configCtx Axis2の設定情報
     * @param componentDef S2コンテナのコンポーネント定義
     * @return AxisService
     */
    public AxisService populateService(ConfigurationContext configCtx,
                                       ComponentDef componentDef);

    /**
     * Axis2、S2コンテナのコンポーネント定義、 およびS2Axis2のサービス定義を指定して、AxisServiceを構築します。
     * 
     * @param configCtx Axis2の設定情報
     * @param componentDef S2コンテナのコンポーネント定義
     * @param serviceDef S2Axis2のサービス定義
     * @return AxisService
     */
    public AxisService populateService(ConfigurationContext configCtx,
                                       ComponentDef componentDef,
                                       ServiceDef serviceDef);

}
