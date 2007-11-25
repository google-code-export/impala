package org.impalaframework.plugin.modification;

import java.util.Collection;
import java.util.Iterator;

import org.impalaframework.plugin.modification.StrictPluginModificationCalculator;
import org.impalaframework.plugin.modification.PluginStateChange;
import org.impalaframework.plugin.modification.PluginTransition;
import org.impalaframework.plugin.modification.PluginTransitionSet;
import org.impalaframework.plugin.modification.StickyPluginModificationCalculator;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.plugin.spec.SimplePluginSpec;

import junit.framework.TestCase;

public class StickyPluginModificationCalculatorTest extends TestCase {

	public final void testCheckOriginal() {
		ParentSpec parentSpec1 = PluginModificationTestUtils.spec("app-context1.xml", "plugin1, plugin2, plugin3");
		ParentSpec parentSpec2 = PluginModificationTestUtils.spec("app-context1.xml", "plugin1 (myPlugins:one), plugin2");

		PluginSpec plugin2 = parentSpec2.findPlugin("plugin2", true);
		new SimplePluginSpec(plugin2, "plugin4");

		StrictPluginModificationCalculator calculator = new StrictPluginModificationCalculator();
		PluginTransitionSet transitions = calculator.getTransitions(parentSpec1, parentSpec2);
		
		Iterator<? extends PluginStateChange> iterator = doAssertions(transitions, 4);
		PluginStateChange change4 = iterator.next();
		assertEquals("plugin3", change4.getPluginSpec().getName());
		assertEquals(PluginTransition.LOADED_TO_UNLOADED, change4.getTransition());
		
		//now show that the sticky calculator has the same set of changes, but omits the last one
		StrictPluginModificationCalculator stickyCalculator = new StickyPluginModificationCalculator();
		PluginTransitionSet stickyTransitions = stickyCalculator.getTransitions(parentSpec1, parentSpec2);
		doAssertions(stickyTransitions, 3);
	}
	
	public final void testAddParentLocations() {
		ParentSpec parentSpec1 = PluginModificationTestUtils.spec("app-context1.xml", "plugin1, plugin2, plugin4");
		ParentSpec parentSpec2 = PluginModificationTestUtils.spec("app-context1.xml,extra-context.xml", "plugin1, plugin2, plugin3");

		//now show that the sticky calculator has the same set of changes, but omits the last one
		StrictPluginModificationCalculator stickyCalculator = new StickyPluginModificationCalculator();
		PluginTransitionSet stickyTransitions = stickyCalculator.getTransitions(parentSpec1, parentSpec2);
		
		Collection<? extends PluginStateChange> pluginTransitions = stickyTransitions.getPluginTransitions();
		assertEquals(2, pluginTransitions.size());
		
		Iterator<? extends PluginStateChange> iterator = pluginTransitions.iterator();
		PluginStateChange first = iterator.next();
		assertEquals(PluginTransition.CONTEXT_LOCATIONS_ADDED, first.getTransition());
		assertEquals(ParentSpec.NAME, first.getPluginSpec().getName());

		PluginStateChange second = iterator.next();
		assertEquals(PluginTransition.UNLOADED_TO_LOADED, second.getTransition());
		assertEquals("plugin3", second.getPluginSpec().getName());
		
		ParentSpec newSpec = stickyTransitions.getNewSpec();
		Collection<String> pluginNames = newSpec.getPluginNames();
		assertEquals(4, pluginNames.size());
		assertNotNull(newSpec.getPlugin("plugin1"));
		assertNotNull(newSpec.getPlugin("plugin2"));
		assertNotNull(newSpec.getPlugin("plugin3"));
		assertNotNull(newSpec.getPlugin("plugin4"));
		assertEquals(2, newSpec.getContextLocations().size());
	}
	
	public final void testAddParentLocationsInReverse() {
		ParentSpec parentSpec1 = PluginModificationTestUtils.spec("app-context1.xml,extra-context.xml", "plugin1, plugin2, plugin3");
		ParentSpec parentSpec2 = PluginModificationTestUtils.spec("app-context1.xml", "plugin1, plugin2, plugin4");

		//now show that the sticky calculator has the same set of changes, but omits the last one
		StrictPluginModificationCalculator stickyCalculator = new StickyPluginModificationCalculator();
		PluginTransitionSet stickyTransitions = stickyCalculator.getTransitions(parentSpec1, parentSpec2);
		
		Collection<? extends PluginStateChange> pluginTransitions = stickyTransitions.getPluginTransitions();
		assertEquals(1, pluginTransitions.size());
		
		Iterator<? extends PluginStateChange> iterator = pluginTransitions.iterator();
		PluginStateChange second = iterator.next();
		assertEquals(PluginTransition.UNLOADED_TO_LOADED, second.getTransition());
		assertEquals("plugin4", second.getPluginSpec().getName());
		
		ParentSpec newSpec = stickyTransitions.getNewSpec();
		Collection<String> pluginNames = newSpec.getPluginNames();
		assertEquals(4, pluginNames.size());
		assertNotNull(newSpec.getPlugin("plugin1"));
		assertNotNull(newSpec.getPlugin("plugin2"));
		assertNotNull(newSpec.getPlugin("plugin3"));
		assertNotNull(newSpec.getPlugin("plugin4"));
		assertEquals(2, newSpec.getContextLocations().size());
	}
	
	private Iterator<? extends PluginStateChange> doAssertions(PluginTransitionSet transitions, int expectedSize) {
		Collection<? extends PluginStateChange> pluginTransitions = transitions.getPluginTransitions();
		assertEquals(expectedSize, pluginTransitions.size());
		Iterator<? extends PluginStateChange> iterator = pluginTransitions.iterator();

		PluginStateChange change1 = iterator.next();
		assertEquals("plugin1", change1.getPluginSpec().getName());
		PluginStateChange change2 = iterator.next();
		assertEquals("plugin1", change2.getPluginSpec().getName());
		PluginStateChange change3 = iterator.next();
		assertEquals("plugin4", change3.getPluginSpec().getName());
		return iterator;
	}

}
