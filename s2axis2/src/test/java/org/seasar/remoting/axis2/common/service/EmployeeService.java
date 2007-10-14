package org.seasar.remoting.axis2.common.service;

import org.seasar.remoting.axis2.common.dto.Employee;

public interface EmployeeService {

    public void insertEmployee(Employee employee);

    public void updateEmployee(Employee employee);

    public void deleteEmployee(Integer id, Integer versionNo);

    public void findEmployee(Integer id);
}
