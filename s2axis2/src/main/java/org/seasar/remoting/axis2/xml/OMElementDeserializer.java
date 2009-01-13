/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.remoting.axis2.xml;

import org.apache.axiom.om.OMElement;

/**
 * XMLデータである OMElement オブジェクトを JavaBean にバインドするインタフェースです。
 * 
 * @author takanori
 */
public interface OMElementDeserializer {

    /**
     * 指定されたOMElementをオブジェクトにデシアライズします。
     * 
     * @param om OMElement
     * @return オブジェクト
     * @throws XMLBindException デシアライズに失敗した場合
     */
    Object deserialize(OMElement om) throws XMLBindException;

}
