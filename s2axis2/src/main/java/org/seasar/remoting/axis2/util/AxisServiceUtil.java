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
