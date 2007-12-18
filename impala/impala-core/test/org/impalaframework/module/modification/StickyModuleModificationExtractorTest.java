package org.impalaframework.module.modification;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.modification.ModuleModificationExtractor;
import org.impalaframework.module.modification.ModuleStateChange;
import org.impalaframework.module.modification.ModuleTransition;
import org.impalaframework.module.modification.ModuleTransitionSet;
import org.impalaframework.module.modification.StickyModuleModificationExtractor;
import org.impalaframework.module.modification.StrictModuleModificationExtractor;

public class StickyModuleModificationExtractorTest extends TestCase {

	public final void testCheckOriginal() {
		RootModuleDefinition parentSpec1 = ModuleModificationTestUtils.spec("app-context1.xml", "plugin1, plugin2, plugin3");
		RootModuleDefinition parentSpec2 = ModuleModificationTestUtils.spec("app-context1.xml", "plugin1 (myPlugins:one), plugin2");

		ModuleDefinition plugin2 = parentSpec2.findPlugin("plugin2", true);
		new SimpleModuleDefinition(plugin2, "plugin4");

		ModuleModificationExtractor calculator = new StrictModuleModificationExtractor();
		ModuleTransitionSet transitions = calculator.getTransitions(parentSpec1, parentSpec2);
		
		Iterator<? extends ModuleStateChange> iterator = doAssertions(transitions, 4);
		ModuleStateChange change4 = iterator.next();
		assertEquals("plugin3", change4.getPluginSpec().getName());
		assertEquals(ModuleTransition.LOADED_TO_UNLOADED, change4.getTransition());
		
		//now show that the sticky calculator has the same set of changes, but omits the last one
		ModuleModificationExtractor stickyCalculator = new StickyModuleModificationExtractor();
		ModuleTransitionSet stickyTransitions = stickyCalculator.getTransitions(parentSpec1, parentSpec2);
		doAssertions(stickyTransitions, 3);
	}
	
	public final void testAddParentLocations() {
		RootModuleDefinition parentSpec1 = ModuleModificationTestUtils.spec("app-context1.xml", "plugin1, plugin2, plugin4");
		RootModuleDefinition parentSpec2 = ModuleModificationTestUtils.spec("app-context1.xml,extra-context.xml", "plugin1, plugin2, plugin3");

		//now show that the sticky calculator has the same set of changes, but omits the last one
		ModuleModificationExtractor stickyCalculator = new StickyModuleModificationExtractor();
		ModuleTransitionSet stickyTransitions = stickyCalculator.getTransitions(parentSpec1, parentSpec2);
		
		Collection<? extends ModuleStateChange> pluginTransitions = stickyTransitions.getPluginTransitions();
		assertEquals(2, pluginTransitions.size());
		
		Iterator<? extends ModuleStateChange> iterator = pluginTransitions.iterator();
		ModuleStateChange first = iterator.next();
		assertEquals(ModuleTransition.CONTEXT_LOCATIONS_ADDED, first.getTransition());
		assertEquals(RootModuleDefinition.NAME, first.getPluginSpec().getName());

		ModuleStateChange second = iterator.next();
		assertEquals(ModuleTransition.UNLOADED_TO_LOADED, second.getTransition());
		assertEquals("plugin3", second.getPluginSpec().getName());
		
		RootModuleDefinition newSpec = stickyTransitions.getNewSpec();
		Collection<String> pluginNames = newSpec.getPluginNames();
		assertEquals(4, pluginNames.size());
		assertNotNull(newSpec.getPlugin("plugin1"));
		assertNotNull(newSpec.getPlugin("plugin2"));
		assertNotNull(newSpec.getPlugin("plugin3"));
		assertNotNull(newSpec.getPlugin("plugin4"));
		assertEquals(2, newSpec.getContextLocations().size());
	}
	
	public final void testAddParentLocationsInReverse() {
		RootModuleDefinition parentSpec1 = ModuleModificationTestUtils.spec("app-context1.xml,extra-context.xml", "plugin1, plugin2, plugin3");
		RootModuleDefinition parentSpec2 = ModuleModificationTestUtils.spec("app-context1.xml", "plugin1, plugin2, plugin4");

		//now show that the sticky calculator has the same set of changes, but omits the last one
		ModuleModificationExtractor stickyCalculator = new StickyModuleModificationExtractor();
		ModuleTransitionSet stickyTransitions = stickyCalculator.getTransitions(parentSpec1, parentSpec2);
		
		Collection<? extends ModuleStateChange> pluginTransitions = stickyTransitions.getPluginTransitions();
		assertEquals(1, pluginTransitions.size());
		
		Iterator<? extends ModuleStateChange> iterator = pluginTransitions.iterator();
		ModuleStateChange second = iterator.next();
		assertEquals(ModuleTransition.UNLOADED_TO_LOADED, second.getTransition());
		assertEquals("plugin4", second.getPluginSpec().getName());
		
		RootModuleDefinition newSpec = stickyTransitions.getNewSpec();
		Collection<String> pluginNames = newSpec.getPluginNames();
		assertEquals(4, pluginNames.size());
		assertNotNull(newSpec.getPlugin("plugin1"));
		assertNotNull(newSpec.getPlugin("plugin2"));
		assertNotNull(newSpec.getPlugin("plugin3"));
		assertNotNull(newSpec.getPlugin("plugin4"));
		assertEquals(2, newSpec.getContextLocations().size());
	}
	
	private Iterator<? extends ModuleStateChange> doAssertions(ModuleTransitionSet transitions, int expectedSize) {
		Collection<? extends ModuleStateChange> pluginTransitions = transitions.getPluginTransitions();
		assertEquals(expectedSize, pluginTransitions.size());
		Iterator<? extends ModuleStateChange> iterator = pluginTransitions.iterator();

		ModuleStateChange change1 = iterator.next();
		assertEquals("plugin1", change1.getPluginSpec().getName());
		ModuleStateChange change2 = iterator.next();
		assertEquals("plugin1", change2.getPluginSpec().getName());
		ModuleStateChange change3 = iterator.next();
		assertEquals("plugin4", change3.getPluginSpec().getName());
		return iterator;
	}

}
