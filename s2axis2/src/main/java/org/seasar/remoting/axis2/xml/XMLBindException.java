/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

/**
 * XMLのバインドに失敗した際にスローされる例外です。
 * 
 * @author takanori
 *
 */
public class XMLBindException extends Exception {

    private static final long serialVersionUID = -5327653989591900577L;

    /**
     * 例外を生成します。
     */
    public XMLBindException() {
        super();
    }

    /**
     * 例外を生成します。
     * 
     * @param message メッセージ
     */
    public XMLBindException(String message) {
        super(message);
    }

    /**
     * 例外を生成します。
     * 
     * @param cause 原因
     */
    public XMLBindException(Throwable cause) {
        super(cause);
    }

    /**
     * 例外を生成します。
     * 
     * @param message メッセージ
     * @param cause 原因
     */
    public XMLBindException(String message, Throwable cause) {
        super(message, cause);
    }

}
