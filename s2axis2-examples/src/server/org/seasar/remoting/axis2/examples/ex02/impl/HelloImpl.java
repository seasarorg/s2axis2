/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.remoting.axis2.examples.ex02.impl;

import org.seasar.remoting.axis2.examples.ex02.MessageSettable;
import org.seasar.remoting.axis2.examples.ex02.Hello;

/**
 * @author takanori
 */
public class HelloImpl implements Hello, MessageSettable {
    
    private String message;
    
    public HelloImpl() {};

    public String say() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}