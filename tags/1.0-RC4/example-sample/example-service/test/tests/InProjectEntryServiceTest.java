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

package tests;

import interfaces.EntryService;

import java.util.Collection;

import org.impalaframework.facade.Impala;
import org.impalaframework.interactive.InteractiveTestRunner;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.source.SimpleModuleDefinitionSource;

import classes.AutowiredClass;
import classes.Entry;

public class InProjectEntryServiceTest extends BaseProjectEntryServiceTest {

    public static void main(String[] args) {
        InteractiveTestRunner.run(InProjectEntryServiceTest.class);
    }

    public void testService() {

        baseClassOperation();
        
        EntryService merchant = Impala.getBean("entryService", EntryService.class);

        Entry entry = new Entry();
        entry.setId(1L);
        entry.setTitle("Cabernet");
        entry.setCount(1996);
        merchant.addEntry(entry);

        Collection<Entry> entries = merchant.getEntriesOfCount(1996);
        assertEquals(1, entries.size());

    }

    public void testAutowired() throws Exception {
        final AutowiredClass autowiredClass = Impala.getModuleBean("example-service", "autowiredClass", AutowiredClass.class);
        autowiredClass.useMessage();
    }
    
    public RootModuleDefinition getModuleDefinition() {
        SimpleModuleDefinitionSource definition = new SimpleModuleDefinitionSource("example-main", 
                        new String[] { "parent-context.xml", "extra-context.xml" }, new String[] {
                        "example-hibernate", "example-dao", "example-service" });
        
        return definition.getModuleDefinition();
    }

}
