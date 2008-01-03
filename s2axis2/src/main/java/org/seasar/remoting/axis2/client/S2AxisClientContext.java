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
package org.seasar.remoting.axis2.client;

import java.util.HashMap;
import java.util.Map;

/**
 * クライアントで利用されるコンテキスト情報です。
 * サービスを呼び出すクラスで、ユーザが動的にパラメータを指定したい場合に利用します。<br>
 * このクラスが保持するパラメータは、ThreadLocalな変数として扱われます。
 * 
 * @author takanori
 */
public class S2AxisClientContext {

    /** パラメータのキー : エンドポイントのURL */
    private static final String     KEY_ENDPOINT = "endpoint";

    /** スレッドローカルな変数を格納するオブジェクト */
    private static ThreadLocal<Map> threadLocal  = new ThreadLocal<Map>();

    private S2AxisClientContext() {}

    /**
     * 指定された変数を、ThreadLocalな変数としてこのクラスに追加します。
     * 
     * @param key キー
     * @param value 値
     */
    private static final void addLocalParameter(String key, Object value) {
        Map<String, Object> parameterMap = threadLocal.get();
        if (parameterMap == null) {
            parameterMap = new HashMap<String, Object>();
            threadLocal.set(parameterMap);
        }
        parameterMap.put(key, value);
    }

    /**
     * 指定されたキーにひも付く変数を取得します。
     * 
     * @param key キー
     * @return 値
     */
    private static final Object getLoaclParameter(String key) {
        Map<String, Object> parameterMap = threadLocal.get();
        Object value;
        if (parameterMap != null) {
            value = parameterMap.get(key);
        } else {
            value = null;
        }

        return value;
    }

    /**
     * このクラスに格納されているパラメータをクリアします。
     */
    public static void cleanup() {
        threadLocal.set(new HashMap<String, Object>());
    }

    /**
     * サービスのエンドポイントのURLを設定します。
     * 
     * @param url サービスのエンドポイント
     */
    public static void setEndpointURL(String url) {
        addLocalParameter(KEY_ENDPOINT, url);
    }

    /**
     * サービスのエンドポイントを取得します。
     * 
     * @return サービスのエンドポイント
     */
    public static String getEndpointURL() {
        return (String)getLoaclParameter(KEY_ENDPOINT);
    }
}
