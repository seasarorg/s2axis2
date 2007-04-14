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

/**
 * AxisServiceに関するユーティリティです。
 * 
 * @author takanori
 */
public class AxisServiceUtil {

    /**
     * 指定されたインスタンス属性において、S2コンテナで指定可能なライフサイクルから、 Axis2のスコープを取得します。<br>
     * <br>
     * <table>
     * <tr>
     * <th>S2のinstance属性</th>
     * <th>Axis2のスコープ</th>
     * </tr>
     * <tr>
     * <td>singleton</td>
     * <td>application</td>
     * </tr>
     * <tr>
     * <td>application</td>
     * <td>application</td>
     * </tr>
     * <tr>
     * <td>session</td>
     * <td>transport session</td>
     * </tr>
     * <tr>
     * <td>request</td>
     * <td>request</td>
     * </tr>
     * <tr>
     * <td>prototype</td>
     * <td>request</td>
     * </tr>
     * </table> <br>
     * 上記以外のinstance属性が指定されている場合は、nullを返します。
     * 
     * @param instanceDef S2コンテナで管理されるコンポーネントのインスタンス属性
     * @return Axis2のスコープ
     */
    public static String getAxisScope(InstanceDef instanceDef) {

        if (instanceDef == null) {
            return null;
        }

        String axisScope;
        String scope = instanceDef.getName();

        if (InstanceDef.SINGLETON_NAME.equals(scope)) {
            axisScope = Constants.SCOPE_APPLICATION;
        } else if (InstanceDef.APPLICATION_NAME.equals(scope)) {
            axisScope = Constants.SCOPE_APPLICATION;
        } else if (InstanceDef.SESSION_NAME.equals(scope)) {
            axisScope = Constants.SCOPE_TRANSPORT_SESSION;
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
