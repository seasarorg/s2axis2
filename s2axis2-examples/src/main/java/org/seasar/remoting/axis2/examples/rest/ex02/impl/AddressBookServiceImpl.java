/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.remoting.axis2.examples.rest.ex02.impl;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.seasar.remoting.axis2.examples.rest.ex02.AddressBookService;
import org.seasar.remoting.axis2.examples.rest.ex02.Entry;

public class AddressBookServiceImpl implements AddressBookService {

    private Map<Integer, Entry> entries = new TreeMap<Integer, Entry>();

    public AddressBookServiceImpl() {}

    public void addEntry(Entry entry) {
        if (entry == null) {
            return;
        }
        this.entries.put(entry.getId(), entry);
    }

    public void addEntryByParams(Integer id, String name, String street) {
        Entry entry = new Entry();
        entry.setId(id);
        entry.setName(name);
        entry.setStreet(street);
        this.entries.put(entry.getId(), entry);
    }

    public void updateEntry(Entry entry) {
        if (entry == null) {
            return;
        }
        this.entries.put(entry.getId(), entry);

    }

    public void deleteEntry(Integer id) {
        if (id == null) {
            return;
        }

        this.entries.remove(id);
    }

    public Entry findEntry(Integer id) {
        if (id == null) {
            return null;
        }

        return (Entry)this.entries.get(id);
    }

    public Entry[] findAllEntry() {
        Collection<Entry> allEntry = this.entries.values();
        return allEntry.toArray(new Entry[0]);
    }

}
