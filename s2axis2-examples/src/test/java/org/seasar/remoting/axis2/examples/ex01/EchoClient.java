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
