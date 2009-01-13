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
package org.seasar.remoting.axis2.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * REST形式のWebサービスのメソッドに指定するアノテーションです。
 * 
 * @author takanori
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RestMethod {

    /** GET */
    String HTTP_METHOD_GET    = "GET";

    /** POST */
    String HTTP_METHOD_POST   = "POST";

    /** PUT */
    String HTTP_METHOD_PUT    = "PUT";

    /** DELETE */
    String HTTP_METHOD_DELETE = "DELETE";

    /** サービスの呼び出し名（URIに利用される） */
    String name() default "";

    /** HTTPのメソッドタイプ */
    String httpMethod() default "";

    /** Content-Type */
    String contentType() default "";

}
