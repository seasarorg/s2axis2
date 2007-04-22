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
package org.seasar.remoting.axis2.deployer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    protected static final Pattern META_NAME_PATTERN = Pattern.compile("(?:s2-axis:|axis-)(.+)");

    protected S2Container          container         = null;

    protected ConfigurationContext configCtx         = null;

    private ServiceDeployer        serviceDeployer   = null;

    /**
     * デフォルトコンストラクタです。
     */
    public AxisDeployer() {}

    public void deploy() {
        if (this.configCtx != null
                && this.configCtx.getAxisConfiguration() != null) {
            forEach(this.container.getRoot());
        }
    }

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

    protected void process(final ComponentDef componentDef) {
        final MetaDef serviceMetaDef = getMetaDef(componentDef,
                S2AxisConstants.META_SERVICE);
        if (serviceMetaDef != null) {
            this.serviceDeployer.deploy(this.configCtx, componentDef,
                    serviceMetaDef);
        }

        // TODO ハンドラのデプロイ
    }

    protected void process(final S2Container container) {
        final MetaDef[] metaDefs = getMetaDefs(container,
                S2AxisConstants.META_DEPLOY);
        for (int i = 0; metaDefs != null && i < metaDefs.length; ++i) {
            this.serviceDeployer.deploy(this.configCtx, null, metaDefs[i]);
        }
    }

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

    protected MetaDef[] getMetaDefs(final MetaDefAware metaDefSupport,
                                    final String localName) {
        final List result = new ArrayList();
        for (int i = 0; i < metaDefSupport.getMetaDefSize(); ++i) {
            final MetaDef metaDef = metaDefSupport.getMetaDef(i);
            if (localName.equals(getLocalName(metaDef))) {
                result.add(metaDef);
            }
        }
        return (MetaDef[]) result.toArray(new MetaDef[result.size()]);
    }

    protected String getLocalName(final MetaDef metaDef) {
        final Matcher matcher = META_NAME_PATTERN.matcher(metaDef.getName());
        return matcher.matches() ? matcher.group(1) : null;
    }

    public void setContainer(final S2Container container) {
        this.container = container;
    }

    public void setServletContext(final ServletContext servletContext) {
        this.configCtx = (ConfigurationContext) servletContext.getAttribute(S2AxisConstants.ATTR_CONFIGURATION_CONTEXT);
    }

    public ServiceDeployer getServiceDeployer() {
        return this.serviceDeployer;
    }

    public void setServiceDeployer(ServiceDeployer serviceDeployer) {
        this.serviceDeployer = serviceDeployer;
    }

    public ConfigurationContext getConfigurationContext() {
        return configCtx;
    }

}
