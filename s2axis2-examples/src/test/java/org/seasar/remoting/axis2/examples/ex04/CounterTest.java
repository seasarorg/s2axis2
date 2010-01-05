/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.remoting.axis2.examples.ex04;

import org.seasar.extension.unit.S2TestCase;

/**
 * @author takanori
 */
public class CounterTest extends S2TestCase {

    private Counter service;

    public CounterTest(String name) {
        super(name);
    }

    public void setUp() {
        include("CounterTest.dicon");
        this.service = (Counter) getComponent(Counter.class);
    }

    public void testIncrease() {

        assertEquals(0, this.service.getCount());

        this.service.increase();
        assertEquals(1, this.service.getCount());

        this.service.increase();
        assertEquals(2, this.service.getCount());
    }

    public void testDecrease() {

        assertEquals(0, this.service.getCount());

        this.service.decrease();
        assertEquals(-1, this.service.getCount());

        this.service.decrease();
        assertEquals(-2, this.service.getCount());
    }

}
