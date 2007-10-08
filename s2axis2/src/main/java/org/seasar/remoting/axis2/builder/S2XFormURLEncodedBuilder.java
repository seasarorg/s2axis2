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
package org.seasar.remoting.axis2.builder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.axis2.AxisFault;
import org.apache.axis2.builder.XFormURLEncodedBuilder;
import org.apache.axis2.description.WSDL20DefaultValueHolder;
import org.apache.axis2.transport.http.util.URIEncoderDecoder;
import org.apache.axis2.util.MultipleEntryHashMap;

/**
 * 
 * @author takanori
 *
 */
public class S2XFormURLEncodedBuilder extends XFormURLEncodedBuilder {

    /**
     * コンストラクタ。
     *
     */
    public S2XFormURLEncodedBuilder() {}

    /**
     * {@inheritDoc}
     */
    @Override
    protected void extractParametersFromRequest(MultipleEntryHashMap parameterMap,
                                                String query,
                                                String queryParamSeparator,
                                                String charsetEncoding,
                                                InputStream inputStream)
            throws AxisFault {

        // for GET
        if (query != null && !"".equals(query)) {

            String parts[] = query.split(queryParamSeparator);
            for (int i = 0; i < parts.length; i++) {
                int separator = parts[i].indexOf("=");
                if (separator > 0) {
                    parameterMap.put(parts[i].substring(0, separator),
                            parts[i].substring(separator + 1));
                }
            }

        }

        // for POST
        if (inputStream != null) {
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(
                        inputStream, charsetEncoding);
                BufferedReader bufferedReader = new BufferedReader(
                        inputStreamReader);
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line != null) {
                        String parts[] = line.split(WSDL20DefaultValueHolder.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR_DEFAULT);
                        for (int i = 0; i < parts.length; i++) {
                            int separator = parts[i].indexOf("=");

                            String key = parts[i].substring(0, separator);
                            String value = parts[i].substring(separator + 1);
                            parameterMap.put(key,
                                    URIEncoderDecoder.decode(value));
                        }
                    } else {
                        break;
                    }
                }
            } catch (IOException e) {
                throw AxisFault.makeFault(e);
            }
        }
    }

}
