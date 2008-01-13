/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the
 * License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package org.seasar.remoting.axis2.deployer;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.engine.AxisConfiguration;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.log.Logger;

/**
 * コンポーネントをAxisにデプロイする抽象クラスです。
 * 
 * @author takanori
 */
public abstract class AxisServiceDeployer {

    private static final Logger logger = Logger.getLogger(AxisServiceDeployer.class);

    /**
     * 指定されたコンポーネントをデプロイします。
     * 
     * @param configCtx Axisの設定情報
     * @param componentDef コンポーネント定義
     * @param metaDef メタデータ
     */
    public abstract void deploy(ConfigurationContext configCtx,
                                ComponentDef componentDef,
                                MetaDef metaDef);

    /**
     * 指定されたAxisSerivceをデプロイします。
     * 
     * @param axisConfig AxisConfiguration
     * @param axisService サービス
     */
    protected void deployAxisService(AxisConfiguration axisConfig,
                                     AxisService axisService) {
        try {
            axisService.setWsdlFound(true);
            axisConfig.addService(axisService);

            logger.log("IAXS0001", new Object[] { axisService.getName() });
        } catch (AxisFault ex) {
            throw new DeployFailedException("EAXS0002",
                    new Object[] { axisService.getName() }, ex);
        }
    }
}
