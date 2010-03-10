/*
 * Copyright 2007-2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package classes;

import interfaces.EntryDAO;
import interfaces.EntryService;

import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

public class EntryServiceImpl implements EntryService {

    private EntryDAO entryDAO;

    @Transactional()
    public void addEntry(Entry entry) {
        entryDAO.save(entry);
    }

    public Collection<Entry> getEntriesOfCount(int count) {
        return entryDAO.getEntriesWithCount(count);
    }

    public void setEntryDAO(EntryDAO entryDAO) {
        this.entryDAO = entryDAO;
    }

}
