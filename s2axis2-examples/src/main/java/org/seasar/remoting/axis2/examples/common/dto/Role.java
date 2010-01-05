/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.remoting.axis2.examples.common.dto;

public class Role {

    private int    roleId;

    private String roleName;

    public Role() {}

    public int getRoleId() {
        return this.roleId;
    }

    public void setRoleId(int deptno) {
        this.roleId = deptno;
    }

    public java.lang.String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(java.lang.String dname) {
        this.roleName = dname;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer("[");
        buf.append(roleId).append(", ");
        buf.append(roleName).append(", ");
        buf.append("]");
        return buf.toString();
    }

    public int hashCode() {
        return this.getRoleId();
    }
}
