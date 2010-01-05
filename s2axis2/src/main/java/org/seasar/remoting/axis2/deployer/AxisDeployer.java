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
package org.seasar.remoting.axis2.deployer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jws.WebService;
import javax.servlet.ServletContext;

import org.apache.axis2.context.ConfigurationContext;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.container.MetaDefAware;
import org.seasar.framework.container.S2Container;
import org.seasar.remoting.axis2.S2AxisConstants;

/**
 * S2コンテナで管理するコンポーネントを、Axis2にデプロイします。<br>
 * Axis2にデプロイするためには、<code>AxisServlet</code>がサーブレットとして、web.xmlに登録されていることが必要です。
 * 
 * @author takanori
 */
public class AxisDeployer {

    protected static final Pattern             META_NAME_PATTERN = Pattern.compile("(?:s2-axis:|axis-)(.+)");

    protected S2Container                      container         = null;

    protected ConfigurationContext             configCtx         = null;

    protected Map<String, AxisServiceDeployer> deployerMap       = new HashMap<String, AxisServiceDeployer>();

    /**
     * デフォルトコンストラクタです。
     */
    public AxisDeployer() {}

    /**
     * S2コンテナに登録されたサービスをデプロイします。
     */
    public void deploy() {
        if (this.configCtx != null
                && this.configCtx.getAxisConfiguration() != null) {
            forEach(this.container.getRoot());
        }
    }

    /**
     * S2コンテナに登録されたサービスをデプロイします。
     * 
     * @param container S2コンテナ
     */
    protected void forEach(final S2Container container) {
        final int componentDefSize = container.getComponentDefSize();
        for (int i = 0; i < componentDefSize; ++i) {
            process(container.getComponentDef(i));
        }

        process(container);

        final int childContainerSize = container.getChildSize();
        for (int i = 0; i < childContainerSize; ++i) {
            forEach(container.getChild(i));
        }
    }

    /**
     * コンポーネントのメタデータ定義を確認し、
     * Axis2のメタデータ定義が指定されている場合は、サービスとしてデプロイします。
     * 
     * @param componentDef コンポーネント定義
     */
    protected void process(final ComponentDef componentDef) {
        final MetaDef serviceMetaDef = getMetaDef(componentDef,
                S2AxisConstants.META_SERVICE);
        if (serviceMetaDef != null) {
            // WebServiceアノテーションの有無をチェックし、
            // 指定されている場合は、JAX-WSとしてのデプロイ
            // 指定されていない場合は、Axis2の通常のデプロイ
            Class serviceClass = componentDef.getComponentClass();
            WebService webService = (WebService)serviceClass.getAnnotation(WebService.class);

            AxisServiceDeployer serviceDeployer;
            if (webService != null) {
                serviceDeployer = this.deployerMap.get(S2AxisConstants.DEPLOYER_JWS);
            } else {
                serviceDeployer = this.deployerMap.get(S2AxisConstants.DEPLOYER_SERCIE_CLASS);
            }

            if (serviceDeployer == null) {
                throw new DeployFailedException("");
            }
            serviceDeployer.deploy(this.configCtx, componentDef, serviceMetaDef);
        }

        // TODO モジュールのデプロイ
    }

    /**
     * コンテナに<code>services.xml</code>での定義がなされているかどうかを確認し、
     * 定義が存在する場合は、サービスとしてデプロイします。
     * 
     * @param container コンテナ
     */
    protected void process(final S2Container container) {
        final MetaDef[] metaDefs = getMetaDefs(container,
                S2AxisConstants.META_DEPLOY);

        AxisServiceDeployer serviceDeployer = this.deployerMap.get(S2AxisConstants.DEPLOYER_SERICES_XML);

        for (int i = 0; metaDefs != null && i < metaDefs.length; ++i) {
            serviceDeployer.deploy(this.configCtx, null, metaDefs[i]);
        }
    }

    /**
     * 指定されたメタデータ定義を取得します。
     * 
     * @param metaDefSupport {@link MetaDefAware}
     * @param localName メタデータ定義の名前
     * @return メタデータ定義
     */
    protected MetaDef getMetaDef(final MetaDefAware metaDefSupport,
                                 final String localName) {
        for (int i = 0; i < metaDefSupport.getMetaDefSize(); ++i) {
            final MetaDef metaDef = metaDefSupport.getMetaDef(i);
            if (localName.equals(getLocalName(metaDef))) {
                return metaDef;
            }
        }
        return null;
    }

    /**
     * 指定されたメタデータ定義を取得します。
     * 
     * @param metaDefSupport {@link MetaDefAware}
     * @param localName メタデータ定義の名前
     * @return メタデータ定義の配列
     */
    protected MetaDef[] getMetaDefs(final MetaDefAware metaDefSupport,
                                    final String localName) {
        final List<MetaDef> result = new ArrayList<MetaDef>();
        for (int i = 0; i < metaDefSupport.getMetaDefSize(); ++i) {
            final MetaDef metaDef = metaDefSupport.getMetaDef(i);
            if (localName.equals(getLocalName(metaDef))) {
                result.add(metaDef);
            }
        }
        return result.toArray(new MetaDef[result.size()]);
    }

    /**
     * メタデータ定義のローカル名を取得します。
     * 
     * @param metaDef メタデータ定義
     * @return ローカル名を
     */
    protected String getLocalName(final MetaDef metaDef) {
        final Matcher matcher = META_NAME_PATTERN.matcher(metaDef.getName());
        return matcher.matches() ? matcher.group(1) : null;
    }

    public void setContainer(final S2Container container) {
        this.container = container;
    }

    public void setServletContext(final ServletContext servletContext) {
        this.configCtx = (ConfigurationContext)servletContext.getAttribute(S2AxisConstants.ATTR_CONFIGURATION_CONTEXT);
    }

    public ConfigurationContext getConfigurationContext() {
        return this.configCtx;
    }

    public void addServiceDeployer(String key, AxisServiceDeployer deployer) {
        this.deployerMap.put(key, deployer);
    }

}
