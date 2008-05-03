package org.seasar.remoting.axis2.examples.ex01;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.remoting.axis2.examples.ex01.JDK15Service.ColorType;

public class JDK15ServiceTest extends S2TestCase {

    JDK15Service service;

    public JDK15ServiceTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        include("JDK15ServiceTest.dicon");
    }

    public void testGetColorType() {
        String colorTypeValue = this.service.getColorType();
        ColorType actual = ColorType.valueOf(colorTypeValue);

        assertEquals(ColorType.RED, actual);
    }

}
