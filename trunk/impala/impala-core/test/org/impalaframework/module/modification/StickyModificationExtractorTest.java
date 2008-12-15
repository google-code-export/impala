/*
 * Copyright 2007-2008 the original author or authors.
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

import org.impalaframework.module.ModificationExtractor;
import org.impalaframework.module.ModuleStateChange;
import org.impalaframework.module.Transition;
import org.impalaframework.module.TransitionSet;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionCallback;
import org.impalaframework.module.definition.ModuleDefinitionWalker;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.modification.StickyModificationExtractor;
import org.impalaframework.module.modification.StrictModificationExtractor;

public class StickyModificationExtractorTest extends TestCase {
	
	/**
	 * This test demonstrates that after getting the transitions, the {@link RootModuleDefinition} is frozen
	 */
	public final void testCheckFreezeUnfreeze() {
		RootModuleDefinition parentSpec1 = ModificationTestUtils.spec("app-context1.xml", "plugin1, plugin2, plugin3");
		RootModuleDefinition parentSpec2 = ModificationTestUtils.spec("app-context1.xml", "plugin1, plugin2");

		ModificationExtractor calculator = new StrictModificationExtractor();
		TransitionSet transitions = calculator.getTransitions(parentSpec1, parentSpec2);
		RootModuleDefinition newRoot = transitions.getNewRootModuleDefinition();
		
		ModuleDefinitionWalker.walkRootDefinition(newRoot, new ModuleDefinitionCallback(){

			public boolean matches(ModuleDefinition moduleDefinition) {
				assertTrue(moduleDefinition.isFrozen());
				return false;
			}
			
		});
		calculator.getTransitions(newRoot, parentSpec2);
	}	
	
	
	public final void testCheckOriginal() {
		RootModuleDefinition parentSpec1 = ModificationTestUtils.spec("app-context1.xml", "plugin1, plugin2, plugin3");
		RootModuleDefinition parentSpec2 = ModificationTestUtils.spec("app-context1.xml", "plugin1, plugin2");

		ModuleDefinition plugin2 = parentSpec2.findChildDefinition("plugin2", true);
		ModuleDefinition plugin4 = new SimpleModuleDefinition(plugin2, "plugin4");
		new SimpleModuleDefinition(plugin4, "plugin5");
		new SimpleModuleDefinition(plugin4, "plugin6");

		ModificationExtractor calculator = new StrictModificationExtractor();
		TransitionSet transitions = calculator.getTransitions(parentSpec1, parentSpec2);
		
		Iterator<? extends ModuleStateChange> iterator = doAssertions(transitions, 4);
		
		ModuleStateChange change3 = iterator.next();
		assertEquals("plugin3", change3.getModuleDefinition().getName());
		assertEquals(Transition.LOADED_TO_UNLOADED, change3.getTransition());
		
		//now show that the sticky calculator has the same set of changes, but omits the last one
		ModificationExtractor stickyCalculator = new StickyModificationExtractor();
		TransitionSet stickyTransitions = stickyCalculator.getTransitions(parentSpec1, parentSpec2);
		doAssertions(stickyTransitions, 3);
	}
	
	public final void testAddParentLocations() {
		RootModuleDefinition parentSpec1 = ModificationTestUtils.spec("app-context1.xml", "plugin1, plugin2, plugin4");
		RootModuleDefinition parentSpec2 = ModificationTestUtils.spec("app-context1.xml,extra-context.xml", "plugin1, plugin2, plugin3");

		//now show that the sticky calculator has the same set of changes, but omits the last one
		ModificationExtractor stickyCalculator = new StickyModificationExtractor();
		TransitionSet stickyTransitions = stickyCalculator.getTransitions(parentSpec1, parentSpec2);
		
		Collection<? extends ModuleStateChange> moduleTransitions = stickyTransitions.getModuleTransitions();
		assertEquals(2, moduleTransitions.size());
		
		Iterator<? extends ModuleStateChange> iterator = moduleTransitions.iterator();
		ModuleStateChange first = iterator.next();
		assertEquals(Transition.CONTEXT_LOCATIONS_ADDED, first.getTransition());
		assertEquals("project1", first.getModuleDefinition().getName());

		ModuleStateChange second = iterator.next();
		assertEquals(Transition.UNLOADED_TO_LOADED, second.getTransition());
		assertEquals("plugin3", second.getModuleDefinition().getName());
		
		RootModuleDefinition newSpec = stickyTransitions.getNewRootModuleDefinition();
		Collection<String> moduleNames = newSpec.getModuleNames();
		assertEquals(4, moduleNames.size());
		assertNotNull(newSpec.getModule("plugin1"));
		assertNotNull(newSpec.getModule("plugin2"));
		assertNotNull(newSpec.getModule("plugin3"));
		assertNotNull(newSpec.getModule("plugin4"));
		assertEquals(2, newSpec.getContextLocations().size());
	}
	
	public final void testAddParentLocationsInReverse() {
		RootModuleDefinition parentSpec1 = ModificationTestUtils.spec("app-context1.xml,extra-context.xml", "plugin1, plugin2, plugin3");
		RootModuleDefinition parentSpec2 = ModificationTestUtils.spec("app-context1.xml", "plugin1, plugin2, plugin4");

		//now show that the sticky calculator has the same set of changes, but omits the last one
		ModificationExtractor stickyCalculator = new StickyModificationExtractor();
		TransitionSet stickyTransitions = stickyCalculator.getTransitions(parentSpec1, parentSpec2);
		
		Collection<? extends ModuleStateChange> moduleTransitions = stickyTransitions.getModuleTransitions();
		assertEquals(1, moduleTransitions.size());
		
		Iterator<? extends ModuleStateChange> iterator = moduleTransitions.iterator();
		ModuleStateChange second = iterator.next();
		assertEquals(Transition.UNLOADED_TO_LOADED, second.getTransition());
		assertEquals("plugin4", second.getModuleDefinition().getName());
		
		RootModuleDefinition newDefinition = stickyTransitions.getNewRootModuleDefinition();
		Collection<String> moduleNames = newDefinition.getModuleNames();
		assertEquals(4, moduleNames.size());
		assertNotNull(newDefinition.getModule("plugin1"));
		assertNotNull(newDefinition.getModule("plugin2"));
		assertNotNull(newDefinition.getModule("plugin3"));
		assertNotNull(newDefinition.getModule("plugin4"));
		assertEquals(2, newDefinition.getContextLocations().size());
	}
	
	private Iterator<? extends ModuleStateChange> doAssertions(TransitionSet transitions, int expectedSize) {
		Collection<? extends ModuleStateChange> moduleTransitions = transitions.getModuleTransitions();
		assertEquals(expectedSize, moduleTransitions.size());
		Iterator<? extends ModuleStateChange> iterator = moduleTransitions.iterator();

		ModuleStateChange change1 = iterator.next();
		assertEquals("plugin4", change1.getModuleDefinition().getName());
		assertEquals(Transition.UNLOADED_TO_LOADED, change1.getTransition());
		
		ModuleStateChange change4 = iterator.next();
		assertEquals("plugin5", change4.getModuleDefinition().getName());
		assertEquals(Transition.UNLOADED_TO_LOADED, change4.getTransition());
		
		ModuleStateChange change5 = iterator.next();
		assertEquals("plugin6", change5.getModuleDefinition().getName());
		assertEquals(Transition.UNLOADED_TO_LOADED, change5.getTransition());
		
		return iterator;
	}

}
