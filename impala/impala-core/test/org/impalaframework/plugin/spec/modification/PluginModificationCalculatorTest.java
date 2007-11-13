package org.impalaframework.plugin.spec.modification;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

import org.impalaframework.plugin.builder.SingleStringPluginSpecBuilder;
import org.impalaframework.plugin.spec.BeansetPluginSpec;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.plugin.spec.SimpleParentSpec;
import org.impalaframework.plugin.spec.SimplePluginSpec;

public class PluginModificationCalculatorTest extends TestCase {

	private PluginModificationCalculator calculator;

	public void setUp() {
		calculator = new PluginModificationCalculator();
	}
	
	public void testGetSimpleTransitions() {
		ParentSpec parentSpec = spec("app-context.xml", "plugin1, plugin2");
		PluginTransitionSet transitionsFromOriginal = calculator.getTransitions(parentSpec, null);
		assertSame(null, transitionsFromOriginal.getNewSpec());
		
		PluginTransitionSet transitionsToNew = calculator.getTransitions(null, parentSpec);
		assertSame(parentSpec, transitionsToNew.getNewSpec());
		
		PluginTransitionSet nullTransitions = calculator.getTransitions(null, null);
		assertNull(nullTransitions.getNewSpec());
	}
	
	public void testModifiedParent() {
		ParentSpec parentSpec1 = spec("app-context1.xml", "plugin1, plugin2");
		ParentSpec parentSpec2 = spec("app-context2.xml", "plugin1, plugin2");
		
		PluginTransitionSet transitions = calculator.getTransitions(parentSpec1, parentSpec2);
		assertSame(parentSpec2, transitions.getNewSpec());
		
		Collection<? extends PluginStateChange> pluginTransitions = transitions.getPluginTransitions();
		assertEquals(6, pluginTransitions.size());

		Iterator<? extends PluginStateChange> iterator = pluginTransitions.iterator();
		PluginStateChange change1 = iterator.next();
		PluginStateChange change2 = iterator.next();
		PluginStateChange change3 = iterator.next();
		PluginStateChange change4 = iterator.next();
		PluginStateChange change5 = iterator.next();
		PluginStateChange change6 = iterator.next();
		
		assertEquals("plugin1", change1.getPluginSpec().getName());
		assertEquals(PluginTransition.LOADED_TO_UNLOADED, change1.getTransition());
		assertEquals("plugin2", change2.getPluginSpec().getName());
		assertEquals("root-plugin", change3.getPluginSpec().getName());
		assertEquals(PluginTransition.LOADED_TO_UNLOADED, change3.getTransition());
		
		assertEquals(PluginTransition.UNLOADED_TO_LOADED, change4.getTransition());
		assertEquals("root-plugin", change4.getPluginSpec().getName());
		assertEquals("plugin1", change5.getPluginSpec().getName());
		assertEquals("plugin2", change6.getPluginSpec().getName());
		assertEquals(PluginTransition.UNLOADED_TO_LOADED, change6.getTransition());
	}	
	
	public void testReloadSame() {
		ParentSpec parentSpec1 = spec("app-context1.xml", "plugin1, plugin2");
		ParentSpec parentSpec2 = spec("app-context1.xml", "plugin1, plugin2");
		
		PluginTransitionSet transitions = calculator.reload(parentSpec1, parentSpec2, "plugin1");
		assertSame(parentSpec2, transitions.getNewSpec());
		
		Collection<? extends PluginStateChange> pluginTransitions = transitions.getPluginTransitions();
		assertEquals(2, pluginTransitions.size());

		Iterator<? extends PluginStateChange> iterator = pluginTransitions.iterator();
		PluginStateChange change1 = iterator.next();
		PluginStateChange change2 = iterator.next();
		
		assertEquals("plugin1", change1.getPluginSpec().getName());
		assertEquals(PluginTransition.LOADED_TO_UNLOADED, change1.getTransition());
		assertEquals("plugin1", change2.getPluginSpec().getName());
		assertEquals(PluginTransition.UNLOADED_TO_LOADED, change2.getTransition());
	}	
	
	public void testReloadLike() {
		ParentSpec parentSpec1 = spec("app-context1.xml", "plugin1, plugin2");
		ParentSpec parentSpec2 = spec("app-context1.xml", "plugin1, plugin2");
		
		PluginTransitionSet transitions = calculator.reloadLike(parentSpec1, parentSpec2, "in1");
		assertSame(parentSpec2, transitions.getNewSpec());
		
		Collection<? extends PluginStateChange> pluginTransitions = transitions.getPluginTransitions();
		assertEquals(2, pluginTransitions.size());

		Iterator<? extends PluginStateChange> iterator = pluginTransitions.iterator();
		PluginStateChange change1 = iterator.next();
		PluginStateChange change2 = iterator.next();
		
		assertEquals("plugin1", change1.getPluginSpec().getName());
		assertEquals(PluginTransition.LOADED_TO_UNLOADED, change1.getTransition());
		assertEquals("plugin1", change2.getPluginSpec().getName());
		assertEquals(PluginTransition.UNLOADED_TO_LOADED, change2.getTransition());
	}	
	
	public void testReloadLikeDifferentName() {
		//this test will only unload because there is not an exact match in the plugin to load
		ParentSpec parentSpec1 = spec("app-context1.xml", "in1, plugin2");
		ParentSpec parentSpec2 = spec("app-context1.xml", "plugin1, plugin2");
		
		PluginTransitionSet transitions = calculator.reloadLike(parentSpec1, parentSpec2, "in1");
		assertSame(parentSpec2, transitions.getNewSpec());
		
		Collection<? extends PluginStateChange> pluginTransitions = transitions.getPluginTransitions();
		assertEquals(1, pluginTransitions.size());

		Iterator<? extends PluginStateChange> iterator = pluginTransitions.iterator();
		PluginStateChange change1 = iterator.next();
		
		assertEquals("plugin1", change1.getPluginSpec().getName());
		assertEquals(PluginTransition.UNLOADED_TO_LOADED, change1.getTransition());
	}		
	
	public void testReloadChanged() {
		ParentSpec parentSpec1 = spec("app-context1.xml", "plugin1, plugin2, plugin3");
		ParentSpec parentSpec2 = spec("app-context1.xml", "plugin1 (myPlugins:one), plugin2");
		
		PluginTransitionSet transitions = calculator.reload(parentSpec1, parentSpec2, "plugin1");
		assertSame(parentSpec2, transitions.getNewSpec());
		
		Collection<? extends PluginStateChange> pluginTransitions = transitions.getPluginTransitions();
		assertEquals(2, pluginTransitions.size());

		Iterator<? extends PluginStateChange> iterator = pluginTransitions.iterator();
		PluginStateChange change1 = iterator.next();
		PluginStateChange change2 = iterator.next();
		
		PluginSpec pluginSpec1 = change1.getPluginSpec();
		assertEquals("plugin1", pluginSpec1.getName());
		assertEquals(PluginTransition.LOADED_TO_UNLOADED, change1.getTransition());
		assertFalse(pluginSpec1 instanceof BeansetPluginSpec);
		
		PluginSpec pluginSpec2 = change2.getPluginSpec();
		assertEquals("plugin1", pluginSpec2.getName());
		assertEquals(PluginTransition.UNLOADED_TO_LOADED, change2.getTransition());
		assertTrue(pluginSpec2 instanceof BeansetPluginSpec);
		BeansetPluginSpec b = (BeansetPluginSpec) pluginSpec2;
		assertFalse(b.getOverrides().isEmpty());
	}	
	
	public void testAddedChild() {
		ParentSpec parentSpec1 = spec("app-context1.xml", "plugin1, plugin2");
		ParentSpec parentSpec2 = spec("app-context1.xml", "plugin1, plugin3, plugin2, plugin4");
		
		PluginTransitionSet transitions = calculator.getTransitions(parentSpec1, parentSpec2);
		assertSame(parentSpec2, transitions.getNewSpec());
		
		Collection<? extends PluginStateChange> pluginTransitions = transitions.getPluginTransitions();
		assertEquals(2, pluginTransitions.size());

		Iterator<? extends PluginStateChange> iterator = pluginTransitions.iterator();
		
		PluginStateChange change1 = iterator.next();
		assertEquals("plugin3", change1.getPluginSpec().getName());
		assertEquals(PluginTransition.UNLOADED_TO_LOADED, change1.getTransition());
		
		PluginStateChange change2 = iterator.next();
		assertEquals("plugin4", change2.getPluginSpec().getName());
		assertEquals(PluginTransition.UNLOADED_TO_LOADED, change2.getTransition());
	}	
	
	public void testChangeChild() {
		ParentSpec parentSpec1 = spec("app-context1.xml", "plugin1, plugin2, plugin3");
		ParentSpec parentSpec2 = spec("app-context1.xml", "plugin1 (myPlugins:one), plugin2");
		
		PluginSpec plugin2 = parentSpec2.findPlugin("plugin2", true);
		new SimplePluginSpec(plugin2, "plugin4");
		
		PluginTransitionSet transitions = calculator.getTransitions(parentSpec1, parentSpec2);
		assertSame(parentSpec2, transitions.getNewSpec());
		
		Collection<? extends PluginStateChange> pluginTransitions = transitions.getPluginTransitions();
		assertEquals(3, pluginTransitions.size());

		Iterator<? extends PluginStateChange> iterator = pluginTransitions.iterator();
		
		PluginStateChange change1 = iterator.next();
		assertEquals("plugin1", change1.getPluginSpec().getName());
		assertEquals(PluginTransition.LOADED_TO_UNLOADED, change1.getTransition());
		
		PluginStateChange change2 = iterator.next();
		assertEquals("plugin1", change2.getPluginSpec().getName());
		assertEquals(PluginTransition.UNLOADED_TO_LOADED, change2.getTransition());
		
		PluginStateChange change3 = iterator.next();
		assertEquals("plugin4", change3.getPluginSpec().getName());
		assertEquals(PluginTransition.UNLOADED_TO_LOADED, change3.getTransition());
	}	

	private ParentSpec spec(String contextString, String pluginString) {
		String[] locations = contextString.split(",");
		SingleStringPluginSpecBuilder builder = new SingleStringPluginSpecBuilder(new SimpleParentSpec(locations),
				pluginString);
		ParentSpec parentSpec = builder.getParentSpec();
		return parentSpec;
	}

}
