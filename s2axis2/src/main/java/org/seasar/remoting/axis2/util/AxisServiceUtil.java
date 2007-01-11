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
package org.seasar.remoting.axis2.util;

import org.apache.axis2.Constants;
import org.seasar.framework.container.InstanceDef;

public class AxisServiceUtil {

    public static String getAxisScope(InstanceDef instanceDef) {

        if (instanceDef == null) {
            return null;
        }

        String axisScope;
        String scope = instanceDef.getName();

        // session は transportsession としてマッピング。サービスグループ間で共有可。
        // soapsession とした場合は、サービスグループ内でのみ共有可。
        // prototype は request としてマッピング。
        if (InstanceDef.SINGLETON_NAME.equals(scope)) {
            axisScope = Constants.SCOPE_APPLICATION;
        } else if (InstanceDef.SESSION_NAME.equals(scope)) {
            axisScope = Constants.SCOPE_SOAP_SESSION;
        } else if (InstanceDef.REQUEST_NAME.equals(scope)) {
            axisScope = Constants.SCOPE_REQUEST;
        } else if (InstanceDef.PROTOTYPE_NAME.equals(scope)) {
            axisScope = Constants.SCOPE_REQUEST;
        } else {
            axisScope = null;
        }

        return axisScope;
    }

}
