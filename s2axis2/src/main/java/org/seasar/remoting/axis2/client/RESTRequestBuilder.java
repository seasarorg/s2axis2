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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axis2.databinding.typemapping.SimpleTypeMapper;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.remoting.axis2.annotation.RestUriParam;
import org.seasar.remoting.axis2.util.RestUtil;

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
public class RestRequestBuilder implements RequestBuilder {

    /** エンコード */
    private String encode = "UTF-8";

    /** 日付フォーマット */
    private String dateFormat;

    /**
     * デフォルトコンストラクタ。
     */
    public RestRequestBuilder() {}

    /**
     * {@inheritDoc}
     */
    public OMElement create(Method method, Object[] args) {

        OMElement request;

        // 指定されている引数によって、処理を分ける。
        // ・Beanが1つだけ指定されている場合は、クエリ情報を保持するBeanとみなしてリクエストを生成する。
        // ・シンプルタイプのパラメータのみの場合は、そのパラメータからリクエストを生成する。
        // ・それ以外の場合は、例外をスローする。
        if (args == null || args.length <= 0) {
            request = createRequestByBean(method, null);
        } else if (args.length == 1 && !SimpleTypeMapper.isSimpleType(args[0])) {
            request = createRequestByBean(method, args[0]);
        } else if (checkParameterTypes(method)) {
            request = createRequestByParameters(method, args);
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
     * 
     * @param method
     * @param bean
     * @return
     */
    protected OMElement createRequestByBean(Method method, Object bean) {

        String opeName = RestUtil.getOperationName(method);

        OMFactory omFactory = OMAbstractFactory.getOMFactory();
        OMElement request = omFactory.createOMElement(opeName, null);

        if (bean == null) {
            return request;
        }

        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(bean.getClass());

        for (int i = 0; i < beanDesc.getPropertyDescSize(); i++) {
            // PropertyDescから取得しようとすると、
            // Maven2でテストがパスしないため、Fieldを取得して処理
            Field field = beanDesc.getField(i);
            PropertyDesc propDesc = beanDesc.getPropertyDesc(field.getName());

            String key = getParameterName(field);
            Object value = propDesc.getValue(bean);

            // TODO 配列の場合
            OMElement query = omFactory.createOMElement(key, null, request);
            String text = getQueryText(value);
            query.setText(text);
        }

        return request;
    }

    /**
     * 
     * @param method
     * @param args
     * @return
     */
    protected OMElement createRequestByParameters(Method method, Object[] args) {

        String opeName = RestUtil.getOperationName(method);

        OMFactory omFactory = OMAbstractFactory.getOMFactory();
        OMElement request = omFactory.createOMElement(opeName, null);

        if (args == null || args.length <= 0) {
            return request;
        }

        String[] paramNameArray = getParameterName(method);
        for (int i = 0; i < args.length; i++) {
            String paramName = paramNameArray[i];
            Object value = args[i];

            // TODO 配列の場合
            if (paramName != null) {
                OMElement query = omFactory.createOMElement(paramName, null,
                        request);
                String text = getQueryText(value);
                query.setText(text);
            } else {
                throw new IllegalServiceMethodException(method, args);
            }
        }

        return request;
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

        String text;
        if (this.dateFormat != null) {
            DateFormat format = new SimpleDateFormat(this.dateFormat);
            if (value instanceof Date) {
                text = format.format((Date)value);
            } else if (value instanceof Calendar) {
                text = format.format(((Calendar)value).getTime());
            } else {
                text = SimpleTypeMapper.getStringValue(value);
            }
        } else {
            text = SimpleTypeMapper.getStringValue(value);
        }

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

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

}
