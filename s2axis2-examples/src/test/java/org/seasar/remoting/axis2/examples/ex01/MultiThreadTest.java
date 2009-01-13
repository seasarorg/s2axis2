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

import org.seasar.extension.unit.S2TestCase;

public class MultiThreadTest extends S2TestCase {

    private SimpleTypeService     simpleTypeService;

    private SimpleWrapTypeService simpleWrapTypeService;

    private boolean               hasException = false;

    public MultiThreadTest(String name) {
        super(name);
    }

    public void setUp() {
        include("SimpleTypeServiceTest.dicon");
        include("SimpleWrapTypeServiceTest.dicon");
    }

    public void testMultiThread() {
        int workerNum = 4;

        Runnable[] runnables = new Runnable[workerNum];
        for (int index = 0; index < runnables.length; index++) {
            if (index % 2 == 0) {
                runnables[index] = new SimpleTypeWorker(index,
                        simpleTypeService);
            } else {
                runnables[index] = new SimpleWrapTypeWorker(index,
                        simpleWrapTypeService);
            }
        }

        Thread[] threads = new Thread[runnables.length];
        for (int index = 0; index < threads.length; index++) {
            threads[index] = new Thread(runnables[index]);
            threads[index].start();
        }

        // 処理が終了してしまわないよう、スレッドが完了するまで待機。
        for (int index = 0; index < threads.length; index++) {
            try {
                threads[index].join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                fail();
            }
        }

        if (hasException) {
            fail("並行スレッドで競合が発生しています。");
        }
    }

    private class SimpleTypeWorker implements Runnable {

        private int               id;

        private SimpleTypeService service;

        private SimpleTypeWorker(int id, SimpleTypeService service) {
            this.id = id;
            this.service = service;
        }

        public void run() {
            int expected = this.id;
            int actual;

            try {
                synchronized (this.service) {
                    this.service.setIntParam(expected);
                    actual = this.service.getIntParam();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                hasException = true;

                // ダミー値のセット
                actual = Integer.MAX_VALUE;
            }

            assertEquals(expected, actual);
        }
    }

    private class SimpleWrapTypeWorker implements Runnable {

        private int                   id;

        private SimpleWrapTypeService service;

        private SimpleWrapTypeWorker(int id, SimpleWrapTypeService service) {
            this.id = id;
            this.service = service;
        }

        public void run() {
            int size = 3;
            String[] expected = new String[size];
            for (int i = 0; i < size; i++) {
                expected[i] = this.id + " : array" + i;
            }
            String[] actual = new String[size];
            try {
                synchronized (this.service) {
                    this.service.setArrayStringParam(expected);
                    actual = this.service.getArrayStringParam();
                }

                if (expected.length != actual.length) {
                    throw new IllegalStateException("unmatch size : [expected="
                            + expected.length + ", actual=" + actual.length
                            + "]");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                hasException = true;
            }

            for (int i = 0; i < size; i++) {
                assertEquals(expected[i], actual[i]);
            }
        }
    }
}
