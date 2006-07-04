package org.seasar.remoting.axis2.examples.ex03.impl;

import org.seasar.remoting.axis2.examples.ex03.BeanEcho;
import org.seasar.remoting.axis2.examples.ex03.EchoDto;

public class BeanEchoImpl implements BeanEcho {

    public BeanEchoImpl() {}

    public EchoDto echo(EchoDto echoDto) {
        return echoDto;
    }

}
