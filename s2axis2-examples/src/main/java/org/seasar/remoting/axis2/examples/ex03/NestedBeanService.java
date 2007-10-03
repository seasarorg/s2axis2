package org.seasar.remoting.axis2.examples.ex03;

import org.seasar.remoting.axis2.examples.common.dto.Employee;

public interface NestedBeanService {

    Employee find(Integer empNo);

    Employee[] findAll();
}
