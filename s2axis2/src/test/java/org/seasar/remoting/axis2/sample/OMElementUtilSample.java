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
package org.seasar.remoting.axis2.sample;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.databinding.utils.BeanUtil;
import org.apache.axis2.engine.DefaultObjectSupplier;

/**
 * 
 * @author takanori
 * 
 */
public class OMElementUtilSample {

    public OMElementUtilSample() {}

    public OMElement createOMElement(Object[] params) {
        OMFactory fac = OMAbstractFactory.getOMFactory();

        OMNamespace omNs = fac.createOMNamespace("http://sample/services/Service", "Service");
        OMElement om;

        QName qName = new QName("method");
        om = BeanUtil.getOMElement(qName, params, null, false, null);
        om.setNamespace(omNs);

        Iterator ite = om.getChildren();
        int index = 0;
        while (ite.hasNext() == true) {
            OMElement arg = (OMElement) ite.next();
            String className = params[index].getClass().getName();
            arg.addAttribute("class", className, null);

            index++;
        }

        return om;
    }

    public Object[] deserializeOMElement(OMElement om)
            throws ClassNotFoundException, AxisFault {
        List list = new ArrayList();

        Iterator ite = om.getChildren();
        while (ite.hasNext() == true) {
            OMElement arg = (OMElement) ite.next();
            String className = arg.getAttribute(new QName("class")).getAttributeValue();
            Class clazz = Class.forName(className);

            Object value = BeanUtil.deserialize(clazz, arg, new DefaultObjectSupplier(), null);

            list.add(value);
        }

        return list.toArray();
    }

    public static void main(String[] args) {

        Object[] params = new Object[] { new String("test"),
                new Integer(1), new Long(2), new Float(0.1), new Double(0.2) };

        OMElementUtilSample sample = new OMElementUtilSample();

        // OMElement の生成
        OMElement om = sample.createOMElement(params);
        System.out.println(om);

        // OMElement のデシアライズ
        try {
            Object[] paramsResult = sample.deserializeOMElement(om);

            for (int index = 0; index < paramsResult.length; index++) {
                System.out.println("arg" + index + " = " + paramsResult[index]);
            }
        }
        catch (AxisFault ex) {
            ex.printStackTrace();
        }
        catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        

    }

}
