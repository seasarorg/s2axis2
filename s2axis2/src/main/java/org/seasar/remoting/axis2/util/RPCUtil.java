package org.seasar.remoting.axis2.util;

import java.lang.reflect.Method;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.databinding.utils.BeanUtil;
import org.apache.axis2.description.java2wsdl.Java2WSDLUtils;

public class RPCUtil {

    /**
     * Webサービスの実行メソッドのQNameを生成します。
     * 
     * @param method Webサービスの実行メソッド
     * @return QName
     * @throws AxisFault
     */
    public static QName createOperationQName(Method method) throws Exception {

        String className = method.getDeclaringClass().getName();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        StringBuffer nsBuff;
        try {
            nsBuff = Java2WSDLUtils.schemaNamespaceFromClassName(className,
                    loader);
        } catch (Exception ex) {
            // TODO 例外処理の見直し
            throw ex;
        }
        String schemaTargetNameSpace = nsBuff.toString();

        QName qName = new QName(schemaTargetNameSpace, method.getName());

        return qName;
    }

    /**
     * OMElement型のリクエストを生成します。
     * 
     * @param method Webサービスの実行メソッド
     * @param args Webサービスの実行メソッドの引数
     * @return リクエスト
     * @throws AxisFault
     */
    public static OMElement createRequest(Method method, Object[] args)
            throws Exception {

        QName qName = createOperationQName(method);

        // see org.apache.axis2.rpc.client.RPCServiceClient
        OMElement request = BeanUtil.getOMElement(qName, args, null, false,
                null);

        return request;
    }

}
