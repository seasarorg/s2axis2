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
package org.seasar.remoting.axis2.common.dto;

public class Employee {

    private Integer        empno;

    private String         ename;

    private String         job;

    private Integer        mgr;

    private java.util.Date hiredate;

    private Department     depertment;

    private Role[]         roles;

    public Employee() {}

    public Integer getEmpno() {
        return this.empno;
    }

    public void setEmpno(Integer empno) {
        this.empno = empno;
    }

    public java.lang.String getEname() {
        return this.ename;
    }

    public void setEname(java.lang.String ename) {
        this.ename = ename;
    }

    public java.lang.String getJob() {
        return this.job;
    }

    public void setJob(java.lang.String job) {
        this.job = job;
    }

    public Integer getMgr() {
        return this.mgr;
    }

    public void setMgr(Integer mgr) {
        this.mgr = mgr;
    }

    public java.util.Date getHiredate() {
        return this.hiredate;
    }

    public void setHiredate(java.util.Date hiredate) {
        this.hiredate = hiredate;
    }

    public Department getDepertment() {
        return depertment;
    }

    public void setDepertment(Department depertmentDto) {
        this.depertment = depertmentDto;
    }

    public Role[] getRoles() {
        return roles;
    }

    public void setRoles(Role[] roles) {
        this.roles = roles;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer("[");
        buf.append(empno).append(", ");
        buf.append(ename).append(", ");
        buf.append(job).append(", ");
        buf.append(mgr).append(", ");
        buf.append(hiredate).append(", ");
        buf.append(depertment).append(", ");
        buf.append(roles).append(", ");
        buf.append("]");
        return buf.toString();
    }

    public int hashCode() {
        if (empno != null) {
            return empno.intValue();
        }
        return 0;
    }

}
