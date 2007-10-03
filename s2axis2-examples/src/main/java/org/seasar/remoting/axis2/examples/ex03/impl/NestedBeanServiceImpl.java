package org.seasar.remoting.axis2.examples.ex03.impl;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.seasar.remoting.axis2.examples.common.dto.Department;
import org.seasar.remoting.axis2.examples.common.dto.Employee;
import org.seasar.remoting.axis2.examples.common.dto.Role;
import org.seasar.remoting.axis2.examples.ex03.NestedBeanService;

public class NestedBeanServiceImpl implements NestedBeanService {

    private Map empMap = new TreeMap();

    public NestedBeanServiceImpl() {
        Department dept1 = new Department();
        dept1.setDeptno(1);
        dept1.setDname("dname1");

        Department dept2 = new Department();
        dept2.setDeptno(2);
        dept2.setDname("dname2");

        Role role1 = new Role();
        role1.setRoleId(1);
        role1.setRoleName("admin");

        Role role2 = new Role();
        role2.setRoleId(2);
        role2.setRoleName("manager");

        Role role3 = new Role();
        role3.setRoleId(3);
        role3.setRoleName("user");

        {
            Employee emp = new Employee();
            emp.setEmpno(new Integer(1));
            emp.setEname("ename1");
            emp.setDepertment(dept1);
            emp.setRoles(new Role[] { role1, role2, role3 });
            this.empMap.put(emp.getEmpno(), emp);
        }
        {
            Employee emp = new Employee();
            emp.setEmpno(new Integer(2));
            emp.setEname("ename2");
            emp.setDepertment(dept1);
            emp.setRoles(new Role[] { role3 });
            this.empMap.put(emp.getEmpno(), emp);
        }
        {
            Employee emp = new Employee();
            emp.setEmpno(new Integer(3));
            emp.setEname("ename3");
            emp.setDepertment(dept2);
            this.empMap.put(emp.getEmpno(), emp);
        }
    }

    public Employee find(Integer empNo) {
        Employee emp = (Employee)this.empMap.get(empNo);
        return emp;
    }

    public Employee[] findAll() {
        Collection collection = this.empMap.values();
        Employee[] empArray = (Employee[])collection.toArray(new Employee[0]);
        return empArray;
    }

}
