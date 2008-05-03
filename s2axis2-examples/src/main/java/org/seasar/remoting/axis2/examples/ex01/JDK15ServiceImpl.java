package org.seasar.remoting.axis2.examples.ex01;

public class JDK15ServiceImpl implements JDK15Service {

    public JDK15ServiceImpl() {}

    public String getColorType() {
        return ColorType.RED.toString();
    }

}
