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

import interfaces.EntryDAO;

import java.util.Collection;

import org.impalaframework.facade.Impala;
import org.impalaframework.interactive.InteractiveTestRunner;
import org.impalaframework.interactive.definition.source.TestDefinitionSource;
import org.impalaframework.module.RootModuleDefinition;

import test.BaseDataTest;
import classes.Entry;
import classes.EntryDAOImpl;

public class InProjectEntryDAOTest extends BaseDataTest {

    public static void main(String[] args) {
        InteractiveTestRunner.run(InProjectEntryDAOTest.class);
    }

    public void testDAO() {     
        EntryDAO dao = Impala.getBean("entryDAO", EntryDAO.class);

        //this relies on setting SuiteOperationFacade when running as JUnit test
        EntryDAOImpl impl = Impala.getModuleBean("example-dao", "entryDAO", EntryDAOImpl.class);
        System.out.println(impl.getHibernateTemplate());
        
        Entry entry = new Entry();
        entry.setTitle("Cabernet");
        entry.setCount(1996);
        dao.save(entry);

        Collection<Entry> entriesOfVintage = dao.getEntriesWithCount(1996);
        System.out.println("Entries of count 1996: " + entriesOfVintage.size());
        assertEquals(1, entriesOfVintage.size());

        entry.setCount(2000);
        dao.update(entry);

        Entry updated = dao.findById(entry.getId());
        assertEquals(2000, updated.getCount());

    }

    public RootModuleDefinition getModuleDefinition() {
        return new TestDefinitionSource("example-dao", "example-hibernate").getModuleDefinition();
    }

}
