package org.apache.axis2.receivers;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.OperationContext;
import org.apache.axis2.context.ServiceContext;


public class ServiceMaker {
    
    AbstractMessageReceiver receiver;

    public ServiceMaker() {}
    
    public void make() {
        
        OperationContext opeContext = new OperationContext(null);
        opeContext.setParent(new ServiceContext(null, null));
        
        MessageContext msgContext = new MessageContext();
        msgContext.setOperationContext(opeContext);
        try {
            this.receiver.getTheImplementationObject(new MessageContext());
        }
        catch (AxisFault ex) {
            ex.printStackTrace();
        }
    }

    public void setReceiver(AbstractMessageReceiver receiver) {
        this.receiver = receiver;
    }

}
