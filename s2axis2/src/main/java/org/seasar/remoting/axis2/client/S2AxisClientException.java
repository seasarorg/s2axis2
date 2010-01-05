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
package org.seasar.remoting.axis2.client;

import org.seasar.framework.exception.SRuntimeException;

/**
 * S2Axis2のクライアントでエラーが発生した際にスローされる例外です。
 * 
 * @author takanori
 */
public class S2AxisClientException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 例外を生成します。
     * 
     * @param messageCode メッセージコード
     */
    public S2AxisClientException(String messageCode) {
        super(messageCode);
    }

    /**
     * 例外を生成します。
     * 
     * @param messageCode メッセージコード
     * @param args メッセージ引数
     */
    public S2AxisClientException(String messageCode, Object[] args) {
        super(messageCode, args);
    }

    /**
     * 例外を生成します。
     * 
     * @param messageCode メッセージコード
     * @param args メッセージ引数
     * @param cause 原因
     */
    public S2AxisClientException(String messageCode, Object[] args,
            Throwable cause) {
        super(messageCode, args, cause);
    }
}
