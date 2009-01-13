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
package org.seasar.remoting.axis2.receivers;

import org.seasar.framework.exception.SRuntimeException;

/**
 * 指定されたサービスが見つからない場合に発生する例外です。
 * 
 * @author takanori
 */
public class NotFoundServiceException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * この例外を作成します。
     * 
     * @param serviceName サービス名
     */
    public NotFoundServiceException(String serviceName) {
        this(serviceName, null);
    }

    /**
     * この例外を作成します。
     * 
     * @param serviceName サービス名
     * @param cause 原因
     */
    public NotFoundServiceException(String serviceName, Throwable cause) {
        super("EAXS0006", new Object[] { serviceName }, cause);
    }

}
