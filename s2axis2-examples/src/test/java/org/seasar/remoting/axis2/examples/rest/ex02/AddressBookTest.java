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
package org.seasar.remoting.axis2.examples.rest.ex02;

import org.seasar.extension.unit.S2TestCase;

public class AddressBookTest extends S2TestCase {

    public void setUp() throws Exception {
        super.setUp();
        include("AddressBookTest.dicon");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void test() {

        AddressBookService service = (AddressBookService)getComponent(AddressBookService.class);

        Entry findEntry;
        Entry[] entries;

        findEntry = service.findEntry(Integer.valueOf(0));
        assertNull(findEntry);

        for (int i = 0; i < 5; i++) {
            Entry entry = new Entry();

            entry.setId(Integer.valueOf(i));
            entry.setName("Abby Cadabby : " + i);
            entry.setStreet("Sesame Street : " + i);
            entry.setCity("Sesame City : " + i);
            entry.setState("Sesame State : " + i);
            entry.setPostalCode("123-" + i);

            service.addEntry(entry);
        }

        findEntry = service.findEntry(Integer.valueOf(0));
        assertNotNull(findEntry);
        System.out.println(findEntry);

        entries = service.findAllEntry();
        assertEquals(5, entries.length);

        service.deleteEntry(Integer.valueOf(3));

        entries = service.findAllEntry();
        assertEquals(4, entries.length);
    }
}
