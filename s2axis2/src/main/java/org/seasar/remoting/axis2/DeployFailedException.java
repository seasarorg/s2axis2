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
package org.seasar.remoting.axis2;

import org.seasar.framework.exception.SRuntimeException;

/**
 * 
 * @author takanori
 * 
 */
public class DeployFailedException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    public DeployFailedException(String code) {
        super(code);
    }

    public DeployFailedException(String code, Object[] args) {
        super(code, args);
    }

    public DeployFailedException(String code, Object[] args, Throwable cause) {
        super(code, args, cause);
    }
}