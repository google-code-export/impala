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

package org.impalaframework.module.modification;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.ModuleState;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ApplicationManager;
import org.impalaframework.module.spi.ModuleStateChange;
import org.impalaframework.module.spi.TestApplicationManager;
import org.impalaframework.module.spi.Transition;
import org.impalaframework.module.spi.TransitionSet;

public class ModificationExtractorTest extends TestCase {

    private StrictModificationExtractor calculator;
    private Application application;

    public void setUp() {
        calculator = new StrictModificationExtractor();

        ApplicationManager applicationManager = TestApplicationManager.newApplicationManager();
        application = applicationManager.getCurrentApplication();
    }

    public void testNull() {
        try {
            calculator.getTransitions(application, null, null);
            fail();
        }
        catch (IllegalArgumentException e) {
            assertEquals("Either originalDefinition or newDefinition must be non-null", e.getMessage());
        }
    }

    public void testGetSimpleTransitions() {
        RootModuleDefinition rootModuleDefinition = ModificationTestUtils.spec("app-context.xml", "plugin1, plugin2");
        TransitionSet transitionsFromOriginal = calculator.getTransitions(application, rootModuleDefinition, null);
        assertEquals(null, transitionsFromOriginal.getNewRootModuleDefinition());

        TransitionSet transitionsToNew = calculator.getTransitions(application, null, rootModuleDefinition);
        assertEquals(rootModuleDefinition, transitionsToNew.getNewRootModuleDefinition());
    }

    public void testModifiedParent() {
        RootModuleDefinition parentSpec1 = ModificationTestUtils.spec("app-context1.xml", "plugin1, plugin2");
        RootModuleDefinition parentSpec2 = ModificationTestUtils.spec("app-context2.xml", "plugin1, plugin2");

        TransitionSet transitions = calculator.getTransitions(application, parentSpec1, parentSpec2);
        assertEquals(parentSpec2, transitions.getNewRootModuleDefinition());

        Collection<? extends ModuleStateChange> moduleTransitions = transitions.getModuleTransitions();
        assertEquals(6, moduleTransitions.size());

        Iterator<? extends ModuleStateChange> iterator = moduleTransitions.iterator();
        ModuleStateChange change1 = iterator.next();
        ModuleStateChange change2 = iterator.next();
        ModuleStateChange change3 = iterator.next();
        ModuleStateChange change4 = iterator.next();
        ModuleStateChange change5 = iterator.next();
        ModuleStateChange change6 = iterator.next();

        assertEquals("plugin1", change1.getModuleDefinition().getName());
        assertEquals(Transition.LOADED_TO_UNLOADED, change1.getTransition());
        assertEquals("plugin2", change2.getModuleDefinition().getName());
        assertEquals("project1", change3.getModuleDefinition().getName());
        assertEquals(Transition.LOADED_TO_UNLOADED, change3.getTransition());

        assertEquals(Transition.UNLOADED_TO_LOADED, change4.getTransition());
        assertEquals("project1", change4.getModuleDefinition().getName());
        assertEquals("plugin1", change5.getModuleDefinition().getName());
        assertEquals("plugin2", change6.getModuleDefinition().getName());
        assertEquals(Transition.UNLOADED_TO_LOADED, change6.getTransition());
    }

    public void testReloadSame() {
        RootModuleDefinition parentSpec1 = ModificationTestUtils.spec("app-context1.xml", "plugin1, plugin2");
        RootModuleDefinition parentSpec2 = ModificationTestUtils.spec("app-context1.xml", "plugin1, plugin2");
        parentSpec2.findChildDefinition("plugin1", true).setState(ModuleState.STALE);

        TransitionSet transitions = calculator.getTransitions(application, parentSpec1, parentSpec2);
        assertEquals(parentSpec2, transitions.getNewRootModuleDefinition());

        Collection<? extends ModuleStateChange> moduleTransitions = transitions.getModuleTransitions();
        assertEquals(2, moduleTransitions.size());

        Iterator<? extends ModuleStateChange> iterator = moduleTransitions.iterator();
        ModuleStateChange change1 = iterator.next();
        ModuleStateChange change2 = iterator.next();

        assertEquals("plugin1", change1.getModuleDefinition().getName());
        assertEquals(Transition.LOADED_TO_UNLOADED, change1.getTransition());
        assertEquals("plugin1", change2.getModuleDefinition().getName());
        assertEquals(Transition.UNLOADED_TO_LOADED, change2.getTransition());
    }

    public void testReloadChanged() {
        RootModuleDefinition parentSpec1 = ModificationTestUtils.spec("app-context1.xml", "plugin1, plugin2, plugin3");
        RootModuleDefinition parentSpec2 = ModificationTestUtils.spec("app-context1.xml", "plugin1, plugin2");
        parentSpec2.findChildDefinition("plugin1", true).setState(ModuleState.STALE);

        TransitionSet transitions = calculator.getTransitions(application, parentSpec1, parentSpec2);
        assertEquals(parentSpec2, transitions.getNewRootModuleDefinition());

        Collection<? extends ModuleStateChange> moduleTransitions = transitions.getModuleTransitions();
        assertEquals(3, moduleTransitions.size());

        Iterator<? extends ModuleStateChange> iterator = moduleTransitions.iterator();
        ModuleStateChange change1 = iterator.next();
        ModuleStateChange change2 = iterator.next();
        ModuleStateChange change3 = iterator.next();

        ModuleDefinition moduleDefinition1Unloaded = change1.getModuleDefinition();
        assertEquals("plugin1", moduleDefinition1Unloaded.getName());
        assertEquals(Transition.LOADED_TO_UNLOADED, change1.getTransition());

        ModuleDefinition moduleDefinition1loaded = change2.getModuleDefinition();
        assertEquals("plugin1", moduleDefinition1loaded.getName());
        assertEquals(Transition.UNLOADED_TO_LOADED, change2.getTransition());
        
        ModuleDefinition moduleDefinition3Unloaded = change3.getModuleDefinition();
        assertEquals("plugin3", moduleDefinition3Unloaded.getName());
    }

    public void testAddedChild() {
        RootModuleDefinition parentSpec1 = ModificationTestUtils.spec("app-context1.xml", "plugin1, plugin2");
        RootModuleDefinition parentSpec2 = ModificationTestUtils.spec("app-context1.xml", "plugin1, plugin3, plugin2, plugin4");

        TransitionSet transitions = calculator.getTransitions(application, parentSpec1, parentSpec2);
        assertEquals(parentSpec2, transitions.getNewRootModuleDefinition());

        Collection<? extends ModuleStateChange> pluginTransitions = transitions.getModuleTransitions();
        assertEquals(2, pluginTransitions.size());

        Iterator<? extends ModuleStateChange> iterator = pluginTransitions.iterator();

        ModuleStateChange change1 = iterator.next();
        assertEquals("plugin3", change1.getModuleDefinition().getName());
        assertEquals(Transition.UNLOADED_TO_LOADED, change1.getTransition());

        ModuleStateChange change2 = iterator.next();
        assertEquals("plugin4", change2.getModuleDefinition().getName());
        assertEquals(Transition.UNLOADED_TO_LOADED, change2.getTransition());
    }

    public void testChangeChild() {
        RootModuleDefinition parentSpec1 = ModificationTestUtils.spec("app-context1.xml", "plugin1, plugin2, plugin3");
        RootModuleDefinition parentSpec2 = ModificationTestUtils.spec("app-context1.xml", "plugin1, plugin2");

        ModuleDefinition plugin2 = parentSpec2.findChildDefinition("plugin2", true);
        new SimpleModuleDefinition(plugin2, "plugin4");

        TransitionSet transitions = calculator.getTransitions(application, parentSpec1, parentSpec2);
        assertEquals(parentSpec2, transitions.getNewRootModuleDefinition());

        Collection<? extends ModuleStateChange> pluginTransitions = transitions.getModuleTransitions();
        assertEquals(2, pluginTransitions.size());

        Iterator<? extends ModuleStateChange> iterator = pluginTransitions.iterator();

        ModuleStateChange change1 = iterator.next();
        assertEquals("plugin4", change1.getModuleDefinition().getName());
        assertEquals(Transition.UNLOADED_TO_LOADED, change1.getTransition());

        ModuleStateChange change2 = iterator.next();
        assertEquals("plugin3", change2.getModuleDefinition().getName());
        assertEquals(Transition.LOADED_TO_UNLOADED, change2.getTransition());
    }

}
