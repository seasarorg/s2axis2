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
package org.seasar.remoting.axis2.deployer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.engine.AxisConfiguration;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.container.MetaDefAware;
import org.seasar.framework.container.S2Container;
import org.seasar.remoting.axis2.S2AxisConstants;

/**
 * 
 * @author takanori
 * 
 */
public class AxisDeployer {

    protected static final Pattern META_NAME_PATTERN = Pattern.compile("(?:s2-axis:|axis-)(.+)");

    protected S2Container          container         = null;

    protected AxisConfiguration    axisConfig        = null;

    private ServiceDeployer        serviceDeployer   = new ServiceDeployer(this);

    public AxisDeployer() {}

    public void deploy() {
        if (this.axisConfig != null) {
            forEach(container.getRoot());
        }
    }

    protected void forEach(final S2Container container) {
        //TODO WSDLからのデプロイ
        
        final int componentDefSize = container.getComponentDefSize();
        for (int i = 0; i < componentDefSize; ++i) {
            process(container.getComponentDef(i));
        }

        final int childContainerSize = container.getChildSize();
        for (int i = 0; i < childContainerSize; ++i) {
            forEach(container.getChild(i));
        }
    }

    protected void process(final ComponentDef componentDef) {
        final MetaDef serviceMetaDef = getMetaDef(componentDef,
                                                  S2AxisConstants.META_SERVICE);
        if (serviceMetaDef != null) {
            serviceDeployer.deploy(componentDef, serviceMetaDef);
        }
        
        //TODO ハンドラのデプロイ
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

    protected String getLocalName(final MetaDef metaDef) {
        final Matcher matcher = META_NAME_PATTERN.matcher(metaDef.getName());
        return matcher.matches() ? matcher.group(1) : null;
    }

    public void setContainer(final S2Container container) {
        this.container = container;
    }

    public void setServletContext(final ServletContext servletContext) {
        ConfigurationContext configContext;

        configContext = (ConfigurationContext) servletContext.getAttribute(S2AxisConstants.ATTR_CONFIGURATION_CONTEXT);
        this.axisConfig = configContext.getAxisConfiguration();
    }

    public AxisConfiguration getAxisConfig() {
        return axisConfig;
    }

}
