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
package org.seasar.remoting.axis2.transport.http;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMOutputFormat;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.WSDL20DefaultValueHolder;
import org.apache.axis2.description.WSDL2Constants;
import org.apache.axis2.transport.http.XFormURLEncodedFormatter;
import org.apache.axis2.transport.http.util.URIEncoderDecoder;

/**
 * 
 * @author takanori
 *
 */
public class S2XFormURLEncodedFormatter extends XFormURLEncodedFormatter {

    /**
     * コンストラクタ。
     *
     */
    public S2XFormURLEncodedFormatter() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getBytes(MessageContext messageContext, OMOutputFormat format)
            throws AxisFault {

        String queryParameterSeparator = (String)messageContext.getProperty(WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR);
        // In case queryParameterSeparator is null we better use the default value

        if (queryParameterSeparator == null) {
            queryParameterSeparator = WSDL20DefaultValueHolder.getDefaultValue(WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR);
        }

        OMElement omElement = messageContext.getEnvelope().getBody().getFirstElement();

        if (omElement != null) {
            Iterator it = omElement.getChildElements();
            String paraString = "";

            String legalCharacters = WSDL2Constants.LEGAL_CHARACTERS_IN_QUERY.replaceAll(
                    queryParameterSeparator, "");

            while (it.hasNext()) {
                OMElement ele1 = (OMElement)it.next();
                String parameter;

                try {
                    parameter = URIEncoderDecoder.quoteIllegal(
                            ele1.getLocalName(), legalCharacters)
                            + "="
                            + URIEncoderDecoder.quoteIllegal(ele1.getText(),
                                    legalCharacters);
                    paraString = "".equals(paraString) ? parameter
                            : (paraString + queryParameterSeparator + parameter);
                } catch (UnsupportedEncodingException ex) {
                    throw AxisFault.makeFault(ex);
                }
            }

            return paraString.getBytes();
        }

        return new byte[0];
    }
}
