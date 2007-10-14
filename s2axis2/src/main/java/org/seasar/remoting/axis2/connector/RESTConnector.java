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
import org.seasar.framework.util.StringUtil;
import org.seasar.remoting.axis2.client.RESTContext;
import org.seasar.remoting.axis2.client.RESTRequestBuilder;
import org.seasar.remoting.axis2.client.RequestBuilder;
import org.seasar.remoting.axis2.util.RESTUtil;

/**
 * RESTをサポートしているWebサービスと通信するためのコネクタです。<br>
 * <br>
 * このコネクタを利用する場合は、サービスの引数は1つに限ります。<br>
 * その引数のパラメータより、サービスを実行するためのRESTパラメータを生成します。<br>
 * 
 * @author takanori
 */
public class RESTConnector extends AbstractRPCConnector {

    /** Action名（メソッド名）を付加するかどうかの指定 */
    protected boolean        appendAction = false;

    /** RESTのコンテキスト */
    protected RESTContext    restContext;

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

        Class returnType = method.getReturnType();

        OMElement request = this.requestBuilder.create(method, args,
                super.options);

        OMElement response;
        if (returnType.equals(void.class)) {
            super.client.sendRobust(request);
            response = null;
        } else {
            response = super.client.sendReceive(request);
        }

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

        if (this.restContext == null) {
            this.restContext = getRestContext();
        }

        if (this.requestBuilder == null) {
            this.requestBuilder = getRequestBuilder();
        }

        super.options.setTransportInProtocol(Constants.TRANSPORT_HTTP);

        String keyEnableRest = Constants.Configuration.ENABLE_REST;
        if (super.options.getProperty(keyEnableRest) == null) {
            super.options.setProperty(keyEnableRest, Constants.VALUE_TRUE);
        }

        // Content-Typeの指定
        String contentType = this.restContext.getContentType(method);
        if (!StringUtil.isEmpty(contentType)) {
            super.options.setProperty(Constants.Configuration.MESSAGE_TYPE,
                    contentType);
        }

        // HttpMethodの指定
        String httpMethod = this.restContext.getHttpMethod(method);
        super.options.setProperty(Constants.Configuration.HTTP_METHOD,
                httpMethod);
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
        if (targetUrl.endsWith(RESTUtil.URI_SEPARATOR)) {
            targetUrl = targetUrl.substring(0,
                    targetUrl.lastIndexOf(RESTUtil.URI_SEPARATOR));
        }

        String uriTemplate = RESTUtil.getUriTemplate(method, this.appendAction);
        return targetUrl + uriTemplate;
    }

    /**
     * RequestBuilderを取得します。
     * 
     * @return RequestBuilder
     */
    protected RequestBuilder getRequestBuilder() {
        S2Container container = SingletonS2ContainerFactory.getContainer().getRoot();
        RequestBuilder requestBuilder = null;
        if (requestBuilder == null) {
            requestBuilder = (RequestBuilder)container.getComponent("RESTRequestBuilder");
        }
        if (requestBuilder == null) {
            requestBuilder = (RequestBuilder)container.getComponent(RESTRequestBuilder.class);
        }

        return requestBuilder;
    }

    /**
     * RESTのコンテキストを取得します。
     * 
     * @return RESTContext
     */
    protected RESTContext getRestContext() {
        S2Container container = SingletonS2ContainerFactory.getContainer().getRoot();
        RESTContext context = null;
        if (context == null) {
            context = (RESTContext)container.getComponent("RESTContext");
        }
        if (context == null) {
            context = (RESTContext)container.getComponent(RESTContext.class);
        }

        return context;
    }

    /**
     * URIにアクション名（メソッド名）を含めるかどうかを取得する。
     * 
     * @return 含める場合はture
     */
    public boolean isAppendAction() {
        return appendAction;
    }

    /**
     * URIにアクション名を含めるかどうかを指定する。
     * 
     * @param appendAction 含める場合はture
     */
    public void setAppendAction(boolean appendAction) {
        this.appendAction = appendAction;
    }

}
