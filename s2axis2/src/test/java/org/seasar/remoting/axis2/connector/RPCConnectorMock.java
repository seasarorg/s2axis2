package org.seasar.remoting.axis2.connector;

import java.lang.reflect.Method;

public class RPCConnectorMock extends AbstractRPCConnector {

    @Override
    protected Object execute(Method method, Object[] args) throws Exception {
        return null;
    }

}
