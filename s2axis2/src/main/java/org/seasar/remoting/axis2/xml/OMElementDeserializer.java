package org.seasar.remoting.axis2.xml;

import org.apache.axiom.om.OMElement;

/**
 * XMLデータである OMElement オブジェクトを JavaBean にバインドするインタフェースです。
 * 
 * @author takanori
 */
public interface OMElementDeserializer {

    Object deserialize(OMElement om) throws XMLBindException;

}
