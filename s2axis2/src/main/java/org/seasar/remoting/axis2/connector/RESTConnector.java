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
package org.seasar.remoting.axis2.connector;

import java.lang.reflect.Method;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.remoting.axis2.client.RequestBuilder;
import org.seasar.remoting.axis2.client.RestRequestBuilder;
import org.seasar.remoting.axis2.util.RestUtil;

/**
 * RESTをサポートしているWebサービスと通信するためのコネクタです。<br>
 * <br>
 * このコネクタを利用する場合は、サービスの引数は1つに限ります。<br>
 * その引数のパラメータより、サービスを実行するためのRESTパラメータを生成します。<br>
 * 
 * @author takanori
 */
public class RESTConnector extends AbstractRPCConnector {

    /** HTTP POST として実行するメソッドの接頭文字列 */
    protected String[]       httpPostPrefixArray   = new String[] { "post",
            "create", "insert", "add"             };

    /** HTTP PUT として実行するメソッドの接頭文字列 */
    protected String[]       httpPutPrefixArray    = new String[] { "put",
            "update", "modify", "store"           };

    /** HTTP DELETE として実行するメソッドの接頭文字列 */
    protected String[]       httpDeletePrefixArray = new String[] { "delete",
            "remove"                              };

    /** リクエストのエンコードを行うかどうか */
    protected boolean        isForceEncode;

    /** RequestBuilder */
    protected RequestBuilder requestBuilder;

    /**
     * デフォルトのコンストラクタ。
     */
    public RESTConnector() {}

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object execute(Method method, Object[] args) throws Exception {

        init(method);

        // エンドポイントの設定
        // REST形式での通信用に設定し直す。 
        String targetUrl = getTargetUrl(method);
        EndpointReference targetEPR = new EndpointReference(targetUrl);
        super.options.setTo(targetEPR);

        OMElement request = this.requestBuilder.create(method, args);
        OMElement response = super.client.sendReceive(request);

        Class returnType = method.getReturnType();
        Object result = deserialize(returnType, response);

        return result;
    }

    /**
     * このオブジェクトの初期化を行います。 <br>
     * Optionsに、 REST形式の呼び出しを行う際のパラメータを設定します。
     * 
     * @param method サービスのメソッド
     * @throws AxisFault
     */
    @Override
    protected void init(Method method) throws AxisFault {

        super.init(method);

        super.options.setTransportInProtocol(Constants.TRANSPORT_HTTP);

        String keyEnableRest = Constants.Configuration.ENABLE_REST;
        if (super.options.getProperty(keyEnableRest) == null) {
            super.options.setProperty(keyEnableRest, Constants.VALUE_TRUE);
        }

        String keyEnableRestThroughGet = Constants.Configuration.ENABLE_REST_THROUGH_GET;
        if (super.options.getProperty(keyEnableRestThroughGet) == null) {
            super.options.setProperty(keyEnableRestThroughGet,
                    Constants.VALUE_TRUE);
        }

        String keyMessageType = Constants.Configuration.MESSAGE_TYPE;
        if (super.options.getProperty(keyMessageType) == null) {
            super.options.setProperty(
                    keyMessageType,
                    org.apache.axis2.transport.http.HTTPConstants.MEDIA_TYPE_X_WWW_FORM);
        }

        // HttpMethodの指定
        String httpMethod = getHttpMethod(method.getName());
        super.options.setProperty(Constants.Configuration.HTTP_METHOD,
                httpMethod);

        if (this.requestBuilder == null) {
            this.requestBuilder = getRequestBuilder();
        }
    }

    /**
     * 指定されたメソッドから、対象サービスのリソースパスであるURIを取得します。
     * 
     * @param method メソッド
     * @param prefix メソッドの接頭文字列
     * @return エンドポイントのURL
     */
    protected String getTargetUrl(Method method) {
        String targetUrl = this.baseURL.toString();
        if (targetUrl.endsWith(RestUtil.URI_SEPARATOR)) {
            targetUrl = targetUrl.substring(0,
                    targetUrl.lastIndexOf(RestUtil.URI_SEPARATOR));
        }

        String uriTemplate = RestUtil.getUriTemplate(method);
        return targetUrl + uriTemplate;
    }

    /**
     * 指定されたメソッド名から、適合するHTTP METHODを返します。
     * 
     * @param methodName
     * @return POST/PUT/DELETE/GETのいずれか
     */
    protected String getHttpMethod(String methodName) {

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
     * このオブジェクトに関連するRequestBuilderを取得します。
     * 
     * @return RequestBuilder
     */
    protected RequestBuilder getRequestBuilder() {
        S2Container container = SingletonS2ContainerFactory.getContainer().getRoot();
        RequestBuilder requestBuilder = (RequestBuilder)container.getComponent(RestRequestBuilder.class);

        return requestBuilder;
    }

    /**
     * HTTP POST処理を行うメソッドの接頭文字列の配列を取得します。
     * 
     * @return 接頭文字列の配列
     */
    public String[] getHttpPostPrefixArray() {
        return httpPostPrefixArray;
    }

    /**
     * HTTP POST処理を行うメソッドの接頭文字列の配列を設定します。
     * 
     * @param httpPostPrefixArray 接頭文字列の配列
     */
    public void setHttpPostPrefixArray(String[] httpPostPrefixArray) {
        this.httpPostPrefixArray = httpPostPrefixArray;
    }

    /**
     * HTTP PUT処理を行うメソッドの接頭文字列の配列を取得します。
     * 
     * @return 接頭文字列の配列
     */
    public String[] getHttpPutPrefixArray() {
        return httpPutPrefixArray;
    }

    /**
     * HTTP PUT処理を行うメソッドの接頭文字列の配列を設定します。
     * 
     * @param httpPutPrefixArray 接頭文字列の配列
     */
    public void setHttpPutPrefixArray(String[] httpPutPrefixArray) {
        this.httpPutPrefixArray = httpPutPrefixArray;
    }

    /**
     * HTTP DELETE処理を行うメソッドの接頭文字列の配列を取得します。
     * 
     * @return 接頭文字列の配列
     */
    public String[] getHttpDeletePrefixArray() {
        return httpDeletePrefixArray;
    }

    /**
     * HTTP DELETE処理を行うメソッドの接頭文字列の配列を設定します。
     * 
     * @param httpDeletePrefixArray 接頭文字列の配列
     */
    public void setHttpDeletePrefixArray(String[] httpDeletePrefixArray) {
        this.httpDeletePrefixArray = httpDeletePrefixArray;
    }

    /**
     * リクエストのエンコードを行うかどうかを返します。
     * 
     * @return エンコードを行う場合はtrue
     */
    public boolean isForceEncode() {
        return isForceEncode;
    }

    /**
     * リクエストのエンコードを行うかどうかを設定します。
     *  
     * @param isForceEncode エンコードを行う場合はtrue
     */
    public void setForceEncode(boolean isForceEncode) {
        this.isForceEncode = isForceEncode;
    }

}
