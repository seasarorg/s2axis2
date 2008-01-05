/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.remoting.axis2.util;

import java.lang.reflect.Method;

import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.remoting.axis2.annotation.RestMethod;
import org.seasar.remoting.axis2.annotation.RestUriTemplate;

/**
 * REST形式のサービスを利用する際のユーティリティです。
 * 
 * @author takanori
 *
 */
public class RESTUtil {

    /** URIのセパレータ */
    public static final String URI_SEPARATOR = "/";

    /**
     * コンストラクタ。
     */
    private RESTUtil() {}

    /**
     * 指定されたメソッドから、そのメソッドが含まれるクラスのリソースに対するURIを取得します。<br>
     * URIは、以下のような形式を返します。
     * <ul>
     * <li>&#47;&lt;クラス名&gt;&#47;&lt;メソッド名&gt;</li>
     * <li>&#47;&lt;RestUriTemplate&gt;&#47;&lt;RestMethod&gt;</li>
     * </ul>
     * 
     * @param method メソッド
     * @return URIテンプレート
     */
    public static String getUriTemplate(Method method, boolean appendAction) {

        String serviceName = getServiceName(method);
        if (!serviceName.startsWith(URI_SEPARATOR)) {
            serviceName = URI_SEPARATOR + serviceName;
        }

        String opeName = getOperationName(method);
        if (appendAction) {
            if (!opeName.startsWith(URI_SEPARATOR)) {
                opeName = URI_SEPARATOR + opeName;
            }
        } else {
            opeName = "";
        }

        String uriTemplate = serviceName + opeName;
        return uriTemplate;
    }

    /**
     * 指定されたメソッドから、そのメソッドが含まれるクラスのサービス名を取得します。<br>
     * {@link RestUriTemplate}アノテーションが指定されている場合は、その情報を利用しますが、
     * 指定がない場合は、クラス名となります。
     * 
     * @param method メソッド
     * @return サービス名
     */
    public static String getServiceName(Method method) {

        RestUriTemplate restUriTemplate = method.getDeclaringClass().getAnnotation(
                RestUriTemplate.class);

        String uriTemplate;
        if (restUriTemplate != null
                && !StringUtil.isEmpty(restUriTemplate.value())) {
            uriTemplate = restUriTemplate.value();
        } else {
            uriTemplate = ClassUtil.getShortClassName(method.getDeclaringClass());
        }

        return uriTemplate;
    }

    /**
     * 指定されたメソッドから、サービスのオペレーション名を取得します。<br>
     * {@link RestMethod}アノテーションが指定されている場合は、その情報を利用します。<br>
     * 
     * @param method メソッド
     * @return メソッド名
     */
    public static String getOperationName(Method method) {

        RestMethod restMethod = method.getAnnotation(RestMethod.class);

        String operationName;
        if (restMethod != null && !StringUtil.isEmpty(restMethod.name())) {
            operationName = restMethod.name();
        } else {
            operationName = method.getName();
        }

        return operationName;
    }

}
