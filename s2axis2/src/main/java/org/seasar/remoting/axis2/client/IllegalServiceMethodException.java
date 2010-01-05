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

import java.lang.reflect.Method;

import org.seasar.framework.util.ArrayUtil;
import org.seasar.framework.util.MethodUtil;

/**
 * Webサービスのメソッドの指定に誤りがある場合に発生する例外です。
 * 
 * @author takanori
 *
 */
public class IllegalServiceMethodException extends S2AxisClientException {

    private static final long serialVersionUID = 1L;

    /**
     * この例外を生成します。
     * 
     * @param method メソッド
     * @param args 引数
     */
    public IllegalServiceMethodException(Method method, Object[] args) {
        this(method, args, null);
    }

    /**
     * この例外を生成します。
     * 
     * @param method メソッド
     * @param args 引数
     * @param cause 原因
     */
    public IllegalServiceMethodException(Method method, Object[] args,
            Exception cause) {
        super("EAXS1004",
                new Object[] {
                        method.getClass(),
                        MethodUtil.getSignature(method.getName(),
                                method.getParameterTypes()),
                        ArrayUtil.toString(args) }, cause);
    }

}
