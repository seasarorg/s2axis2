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
package org.seasar.remoting.axis2.examples.ex03;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.remoting.axis2.examples.common.dto.Employee;

/**
 * @author takanori
 */
public class NestedBeanServiceTest extends S2TestCase {

    private NestedBeanService nestedBeanService;

    public NestedBeanServiceTest(String name) {
        super(name);
    }

    public void setUp() {
        include("NestedBeanServiceTest.dicon");
    }

    public void testFind_success() {

        Employee actual = (Employee)this.nestedBeanService.find(new Integer(1));

        assertEquals(new Integer(1), actual.getEmpno());
        assertEquals("ename1", actual.getEname());
        assertEquals(1, actual.getDepertment().getDeptno());
        assertEquals("dname1", actual.getDepertment().getDname());
        assertEquals(3, actual.getRoles().length);
        assertEquals("admin", actual.getRoles()[0].getRoleName());
        assertEquals("manager", actual.getRoles()[1].getRoleName());
        assertEquals("user", actual.getRoles()[2].getRoleName());
    }

    public void testFindAll_success() {

        Employee[] actual = this.nestedBeanService.findAll();

        assertEquals(3, actual.length);
        assertEquals(new Integer(1), actual[0].getEmpno());
        assertEquals(1, actual[0].getDepertment().getDeptno());
        assertEquals(3, actual[0].getRoles().length);
        assertEquals(new Integer(2), actual[1].getEmpno());
        assertEquals(1, actual[1].getDepertment().getDeptno());
        assertEquals(1, actual[1].getRoles().length);
        assertEquals(new Integer(3), actual[2].getEmpno());
        assertEquals(2, actual[2].getDepertment().getDeptno());
        assertEquals(null, actual[2].getRoles());
    }

}
