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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.Constants;
import org.apache.axis2.client.Options;
import org.apache.axis2.databinding.typemapping.SimpleTypeMapper;
import org.apache.axis2.transport.http.HTTPConstants;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.remoting.axis2.annotation.RestUriParam;
import org.seasar.remoting.axis2.util.RESTUtil;
import org.seasar.remoting.axis2.util.RPCUtil;

/**
 * REST形式のリクエストを構築するクラスです。<br>
 * リクエストは、以下のパラメータを用いて生成します。
 * <ul>
 * <li>パラメータを保持する1つのBean</li>
 * <li>プリミティブ型、そのラッパ型、日付型（{@link Date}, {@link Calendar}）からなるパラメータ</li>
 * </ul>
 * 
 * これらのパラメータは、サービスのメソッド引数として指定されます。
 * 
 * @author takanori
 *
 */
public class RESTRequestBuilder implements RequestBuilder {

    /**
     * デフォルトコンストラクタ。
     */
    public RESTRequestBuilder() {}

    /**
     * {@inheritDoc}
     */
    public OMElement create(Method method, Object[] args, Options options) {

        OMElement request;

        // 指定されている引数によって、処理を分ける。
        // ・Beanが1つだけ指定されている場合は、クエリ情報を保持するBeanとみなしてリクエストを生成する。
        // ・シンプルタイプのパラメータのみの場合は、そのパラメータからリクエストを生成する。
        // ・それ以外の場合は、例外をスローする。
        if (args == null || args.length <= 0) {
            request = createRequestByBean(method, null, options);
        } else if (args.length == 1 && !SimpleTypeMapper.isSimpleType(args[0])) {
            request = createRequestByBean(method, args[0], options);
        } else if (checkParameterTypes(method)) {
            request = createRequestByParameters(method, args, options);
        } else {
            throw new IllegalServiceMethodException(method, args);
        }

        return request;
    }

    /**
     * 呼び出し対象のメソッド引数が、Webサービスでサポートされるパラメータかどうかをチェックします。<br>
     * 全てサポート対象のパラメータの場合はtrue、
     * 1つでもサポート対象でない場合は、falseを返します。
     * 
     * @param method 呼び出し対象のメソッド
     * @return 全てサポート対象のパラメータの場合はtrue
     */
    protected boolean checkParameterTypes(Method method) {

        boolean isSympleType = true;

        Class[] paramTypes = method.getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            if (!SimpleTypeMapper.isSimpleType(paramTypes[i])) {
                isSympleType = false;
                break;
            }
        }

        return isSympleType;
    }

    /**
     * JavaBeanから、リクエスト情報を生成します。
     * 
     * @param method 呼び出し対象のメソッド
     * @param bean リクエストの情報となるJavaBean
     * @param options Axis2のオプション
     * @return リクエスト情報
     */
    protected OMElement createRequestByBean(Method method,
                                            Object bean,
                                            Options options) {

        OMElement request;
        try {
            request = createRootElement(method);
        } catch (Exception ex) {
            throw new IllegalServiceMethodException(method,
                    new Object[] { bean }, ex);
        }

        if (bean == null) {
            return request;
        }

        addBeanElement(bean, request.getNamespace(), request);

        if (options != null) {
            String contentType = (String)options.getProperty(Constants.Configuration.MESSAGE_TYPE);
            if (HTTPConstants.MEDIA_TYPE_X_WWW_FORM.equals(contentType)) {
                request = request.getFirstElement();
            }
        }

        return request;
    }

    /**
     * 指定されたOMElementに、JavaBeanの要素を追加します。
     * 
     * @param bean JavaBean
     * @param ns 名前空間
     * @param parent 親のOMElement
     */
    protected void addBeanElement(Object bean, OMNamespace ns, OMElement parent) {

        if (bean == null) {
            return;
        }

        OMFactory fac = OMAbstractFactory.getOMFactory();

        String beanName = ClassUtil.getShortClassName(bean.getClass());
        String localName = StringUtil.decapitalize(beanName);
        OMElement beanElement = fac.createOMElement(localName, null, parent);
        beanElement.addAttribute("type", bean.getClass().getName(), null);

        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(bean.getClass());

        for (int i = 0; i < beanDesc.getPropertyDescSize(); i++) {
            // PropertyDescから取得しようとすると、
            // Maven2でテストがパスしないため、Fieldを取得して処理
            Field field = beanDesc.getField(i);
            PropertyDesc propDesc = beanDesc.getPropertyDesc(field.getName());

            String key = getParameterName(field);
            Object value = propDesc.getValue(bean);

            // TODO 配列の場合
            if (value == null || SimpleTypeMapper.isSimpleType(value)) {
                OMElement query = fac.createOMElement(key, null, beanElement);
                String text = getQueryText(value);
                query.setText(text);
            } else {
                addBeanElement(value, ns, beanElement);
            }
        }

    }

    /**
     * パラメータから、リクエスト情報を生成します。
     * 
     * @param method 呼び出し対象のメソッド
     * @param args リクエストの情報となるパラメータ
     * @param options Axis2のオプション
     * @return リクエスト情報
     */
    protected OMElement createRequestByParameters(Method method,
                                                  Object[] args,
                                                  Options options) {

        OMElement request;
        try {
            request = createRootElement(method);
        } catch (Exception ex) {
            throw new IllegalServiceMethodException(method, args, ex);
        }

        if (args == null || args.length <= 0) {
            return request;
        }

        OMFactory fac = OMAbstractFactory.getOMFactory();

        String[] paramNameArray = getParameterName(method);
        for (int i = 0; i < args.length; i++) {
            String paramName = paramNameArray[i];
            Object value = args[i];

            // TODO 配列の場合
            if (paramName != null) {
                OMElement query = fac.createOMElement(paramName, null, request);
                String text = getQueryText(value);
                query.setText(text);
            } else {
                throw new IllegalServiceMethodException(method, args);
            }
        }

        return request;
    }

    /**
     * メソッドを指定して、リクエストのルートとなるOMElementを生成します。
     * 
     * @param method 呼び出し対象のメソッド
     * @return OMElement
     * @throws Exception OMElementの生成に失敗した場合
     */
    protected OMElement createRootElement(Method method) throws Exception {

        OMFactory fac = OMAbstractFactory.getOMFactory();

        QName nsQname = RPCUtil.createOperationQName(method);
        OMNamespace ns = fac.createOMNamespace(nsQname.getNamespaceURI(),
                nsQname.getPrefix());

        String opeName = RESTUtil.getOperationName(method);
        OMElement rootElement = fac.createOMElement(opeName, ns);

        return rootElement;
    }

    /**
     * 指定されたパラメータから、URLのクエリ値を取得します。
     * 返される文字列は、URLエンコードされます。<br>
     * 
     * @param value RESTで送信するパラメータ値
     * @return クエリ値
     */
    protected String getQueryText(Object value) {
        if (value == null) {
            return "";
        }

        String text = SimpleTypeMapper.getStringValue(value);

        return text;
    }

    /**
     * RESTリクエストで指定するパラメータ名を取得します。 <br>
     * {@link RestUriParam}アノテーションが指定されている場合は、そのパラメータを取得します。
     * 
     * @param field フィールド
     * @return パラメータ名
     */
    protected String getParameterName(Field field) {

        RestUriParam paramAnnotation = field.getAnnotation(RestUriParam.class);

        String paramName;
        if (paramAnnotation != null) {
            paramName = paramAnnotation.value();
        } else {
            paramName = field.getName();
        }
        return paramName;
    }

    /**
     * RESTリクエストで指定するパラメータ名を取得します。 <br>
     * 指定されたメソッド引数に対して、
     * {@link RestUriParam}アノテーションが指定されていることを期待してパラメータ名を取得します。
     * {@link RestUriParam}アノテーションが指定されていない場合は、配列にnullを設定します。
     * 
     * @param method メソッド
     * @return パラメータ名のリスト
     */
    protected String[] getParameterName(Method method) {

        List<String> paramNameList = new ArrayList<String>();

        Annotation[][] paramAnnotations = method.getParameterAnnotations();

        // 全パラメータをチェック
        for (int i = 0; i < paramAnnotations.length; i++) {
            Annotation[] annotationArray = paramAnnotations[i];

            // 1つのパラメータに定義された全てのアノテーションをチェック
            // RestUriParamが定義されている場合は、パラメータ名を1回だけ取得し、
            // そうでない場合は、パラメータ名にnullを設定する。
            String paramName = null;
            for (int j = 0; j < annotationArray.length; j++) {
                Annotation annotation = annotationArray[j];
                if (annotation != null && annotation instanceof RestUriParam) {
                    RestUriParam uriParamAnnotation = (RestUriParam)annotation;
                    paramName = uriParamAnnotation.value();

                    // 複数定義されていても、最初の1つ以外は無視する。
                    break;
                }
            }

            paramNameList.add(paramName);
        }

        return paramNameList.toArray(new String[0]);
    }

}
