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
package org.seasar.remoting.axis2.client;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.axis2.Constants;
import org.apache.axis2.transport.http.HTTPConstants;
import org.seasar.framework.util.StringUtil;
import org.seasar.remoting.axis2.annotation.RestMethod;

public class RESTContext {

    /** HTTP POST として実行するメソッドの接頭文字列 */
    protected String[]            httpPostPrefixArray   = new String[] {
            "post", "create", "insert", "add"          };

    /** HTTP PUT として実行するメソッドの接頭文字列 */
    protected String[]            httpPutPrefixArray    = new String[] { "put",
            "update", "modify", "store"                };

    /** HTTP DELETE として実行するメソッドの接頭文字列 */
    protected String[]            httpDeletePrefixArray = new String[] {
            "delete", "remove"                         };

    /** Content-Typeのマップ*/
    protected Map<String, String> contentTypeMap        = new HashMap<String, String>();

    /**
     * デフォルトコンストラクタ。
     */
    public RESTContext() {

        // Content-Typeのデフォルト指定
        this.contentTypeMap.put(Constants.Configuration.HTTP_METHOD_POST,
                HTTPConstants.MEDIA_TYPE_APPLICATION_XML);
        this.contentTypeMap.put(Constants.Configuration.HTTP_METHOD_PUT,
                HTTPConstants.MEDIA_TYPE_APPLICATION_XML);
        this.contentTypeMap.put(Constants.Configuration.HTTP_METHOD_DELETE,
                HTTPConstants.MEDIA_TYPE_X_WWW_FORM);
        this.contentTypeMap.put(Constants.Configuration.HTTP_METHOD_GET,
                HTTPConstants.MEDIA_TYPE_X_WWW_FORM);
    }

    /**
     * 指定されたメソッド名から、適合するHTTP METHODを返します。
     * 
     * @param method メソッド
     * @return POST/PUT/DELETE/GETのいずれか
     */
    public String getHttpMethod(Method method) {

        RestMethod restMethod = method.getAnnotation(RestMethod.class);

        String httpMethod;
        if (restMethod != null && !StringUtil.isEmpty(restMethod.httpMethod())) {
            httpMethod = restMethod.httpMethod();
            return httpMethod;
        }

        String methodName = method.getName();

        if (matchMethodPrefix(methodName, this.httpPostPrefixArray) >= 0) {
            return Constants.Configuration.HTTP_METHOD_POST;
        } else if (matchMethodPrefix(methodName, this.httpPutPrefixArray) >= 0) {
            return Constants.Configuration.HTTP_METHOD_PUT;
        } else if (matchMethodPrefix(methodName, this.httpDeletePrefixArray) >= 0) {
            return Constants.Configuration.HTTP_METHOD_DELETE;
        } else {
            return Constants.Configuration.HTTP_METHOD_GET;
        }
    }

    /**
     * メソッド名が、指定されたメソッド接頭文字列の配列要素のいずれかから開始される場合、
     * 配列のインデックスを返します。
     * いずれの要素からも開始されない場合は-1を返します。
     * 
     * @param methodName メソッド名
     * @param prefixArray 接頭文字列の配列
     * @return 配列要素のインデックス。該当しない場合は-1
     */
    protected int matchMethodPrefix(String methodName, String[] prefixArray) {
        for (int i = 0; i < prefixArray.length; i++) {
            if (methodName.startsWith(prefixArray[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 指定されたメソッドから、Content-Typeを取得します。
     * 設定されていない場合は、このオブジェクトに登録されたContent-Typeを取得します。
     * 
     * @param method メソッド
     * @return Content-Type
     */
    public String getContentType(Method method) {

        RestMethod restMethod = method.getAnnotation(RestMethod.class);

        String contentType;
        if (restMethod != null && !StringUtil.isEmpty(restMethod.contentType())) {
            contentType = restMethod.contentType();
            return contentType;
        }

        String httpMethod = getHttpMethod(method);
        contentType = this.contentTypeMap.get(httpMethod);

        return contentType;
    }

    /**
     * HTTP POST処理を行うメソッドの接頭文字列の配列を設定します。
     * 
     * @param httpPostPrefixArray 接頭文字列の配列
     */
    protected void setHttpPostPrefixArray(String[] httpPostPrefixArray) {
        this.httpPostPrefixArray = httpPostPrefixArray;
    }

    /**
     * HTTP PUT処理を行うメソッドの接頭文字列の配列を設定します。
     * 
     * @param httpPutPrefixArray 接頭文字列の配列
     */
    protected void setHttpPutPrefixArray(String[] httpPutPrefixArray) {
        this.httpPutPrefixArray = httpPutPrefixArray;
    }

    /**
     * HTTP DELETE処理を行うメソッドの接頭文字列の配列を設定します。
     * 
     * @param httpDeletePrefixArray 接頭文字列の配列
     */
    protected void setHttpDeletePrefixArray(String[] httpDeletePrefixArray) {
        this.httpDeletePrefixArray = httpDeletePrefixArray;
    }

    /**
     * このオブジェクトに、Content-Typeを追加します。
     * Content-Typeは、HTTPメソッドに対して関連付けられます。
     * 
     * @param httpMethod HTTPメソッド
     * @param contentType Content-Type
     */
    protected void addContentType(String httpMethod, String contentType) {
        this.contentTypeMap.put(httpMethod, contentType);
    }

}
