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
package org.seasar.remoting.axis2.connector;

import java.util.Iterator;
import java.util.Properties;

import org.apache.axis2.client.Options;
import org.seasar.remoting.common.connector.impl.TargetSpecificURLBasedConnector;

/**
 * Webサービスを呼び出すコネクタです。
 * 
 * @author takanori
 */
public abstract class AbstractAxisConnector extends
        TargetSpecificURLBasedConnector {

    /** Axis2のシステムプロパティ */
    private Properties properties;

    /** Axis2のオプション */
    protected Options  options;

    /** タイムアウト値 */
    protected int      timeout = 0;

    /**
     * デフォルトのコンストラクタ。
     */
    public AbstractAxisConnector() {}

    public void setOptions(Options options) {
        this.options = options;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;

        Iterator ite = this.properties.keySet().iterator();
        while (ite.hasNext()) {
            String key = (String) ite.next();
            String value = this.properties.getProperty(key);

            System.setProperty(key, value);
        }
    }

}
