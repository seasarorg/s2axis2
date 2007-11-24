package org.seasar.remoting.axis2.examples.ex01;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

public class EchoClient {

    private Echo service;

    public void execute() {
        int id = 1;
        String msg = "echo message";
        String result = service.echo(id, msg);

        System.out.println(result);
    }

    public void setEcho(Echo echo) {
        this.service = echo;
    }

    public static void main(String[] args) {
        SingletonS2ContainerFactory.setConfigPath("org/seasar/remoting/axis2/examples/ex01/EchoTest.dicon");
        SingletonS2ContainerFactory.init();

        S2Container container = SingletonS2ContainerFactory.getContainer();
        Echo echo = (Echo)container.getComponent(Echo.class);

        EchoClient client = new EchoClient();
        client.setEcho(echo);
        client.execute();
    }
}
