package org.impalaframework.module.modification;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

import org.impalaframework.module.modification.ModuleStateChange;
import org.impalaframework.module.modification.ModuleTransition;
import org.impalaframework.module.modification.ModuleTransitionSet;
import org.impalaframework.module.modification.StrictModuleModificationCalculator;
import org.impalaframework.module.spec.BeansetModuleDefinition;
import org.impalaframework.module.spec.RootModuleDefinition;
import org.impalaframework.module.spec.ModuleDefinition;
import org.impalaframework.module.spec.SimpleModuleDefinition;

public class ModuleModificationCalculatorTest extends TestCase {

	private StrictModuleModificationCalculator calculator;

	public void setUp() {
		calculator = new StrictModuleModificationCalculator();
	}

	public void testNull() {
		try {
			calculator.getTransitions(null, null);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Either originalSpec or newSpec must be non-null", e.getMessage());
		}
	}

	public void testGetSimpleTransitions() {
		RootModuleDefinition rootModuleDefinition = ModuleModificationTestUtils.spec("app-context.xml", "plugin1, plugin2");
		ModuleTransitionSet transitionsFromOriginal = calculator.getTransitions(rootModuleDefinition, null);
		assertSame(null, transitionsFromOriginal.getNewSpec());

		ModuleTransitionSet transitionsToNew = calculator.getTransitions(null, rootModuleDefinition);
		assertSame(rootModuleDefinition, transitionsToNew.getNewSpec());
	}

	public void testModifiedParent() {
		RootModuleDefinition parentSpec1 = ModuleModificationTestUtils.spec("app-context1.xml", "plugin1, plugin2");
		RootModuleDefinition parentSpec2 = ModuleModificationTestUtils.spec("app-context2.xml", "plugin1, plugin2");

		ModuleTransitionSet transitions = calculator.getTransitions(parentSpec1, parentSpec2);
		assertSame(parentSpec2, transitions.getNewSpec());

		Collection<? extends ModuleStateChange> pluginTransitions = transitions.getPluginTransitions();
		assertEquals(6, pluginTransitions.size());

		Iterator<? extends ModuleStateChange> iterator = pluginTransitions.iterator();
		ModuleStateChange change1 = iterator.next();
		ModuleStateChange change2 = iterator.next();
		ModuleStateChange change3 = iterator.next();
		ModuleStateChange change4 = iterator.next();
		ModuleStateChange change5 = iterator.next();
		ModuleStateChange change6 = iterator.next();

		assertEquals("plugin1", change1.getPluginSpec().getName());
		assertEquals(ModuleTransition.LOADED_TO_UNLOADED, change1.getTransition());
		assertEquals("plugin2", change2.getPluginSpec().getName());
		assertEquals("root-plugin", change3.getPluginSpec().getName());
		assertEquals(ModuleTransition.LOADED_TO_UNLOADED, change3.getTransition());

		assertEquals(ModuleTransition.UNLOADED_TO_LOADED, change4.getTransition());
		assertEquals("root-plugin", change4.getPluginSpec().getName());
		assertEquals("plugin1", change5.getPluginSpec().getName());
		assertEquals("plugin2", change6.getPluginSpec().getName());
		assertEquals(ModuleTransition.UNLOADED_TO_LOADED, change6.getTransition());
	}

	public void testReloadSame() {
		RootModuleDefinition parentSpec1 = ModuleModificationTestUtils.spec("app-context1.xml", "plugin1, plugin2");
		RootModuleDefinition parentSpec2 = ModuleModificationTestUtils.spec("app-context1.xml", "plugin1, plugin2");

		ModuleTransitionSet transitions = calculator.reload(parentSpec1, parentSpec2, "plugin1");
		assertSame(parentSpec2, transitions.getNewSpec());

		Collection<? extends ModuleStateChange> pluginTransitions = transitions.getPluginTransitions();
		assertEquals(2, pluginTransitions.size());

		Iterator<? extends ModuleStateChange> iterator = pluginTransitions.iterator();
		ModuleStateChange change1 = iterator.next();
		ModuleStateChange change2 = iterator.next();

		assertEquals("plugin1", change1.getPluginSpec().getName());
		assertEquals(ModuleTransition.LOADED_TO_UNLOADED, change1.getTransition());
		assertEquals("plugin1", change2.getPluginSpec().getName());
		assertEquals(ModuleTransition.UNLOADED_TO_LOADED, change2.getTransition());
	}

	public void testReloadLike() {
		RootModuleDefinition parentSpec1 = ModuleModificationTestUtils.spec("app-context1.xml", "plugin1, plugin2");
		RootModuleDefinition parentSpec2 = ModuleModificationTestUtils.spec("app-context1.xml", "plugin1, plugin2");

		ModuleTransitionSet transitions = calculator.reloadLike(parentSpec1, parentSpec2, "in1");
		assertSame(parentSpec2, transitions.getNewSpec());

		Collection<? extends ModuleStateChange> pluginTransitions = transitions.getPluginTransitions();
		assertEquals(2, pluginTransitions.size());

		Iterator<? extends ModuleStateChange> iterator = pluginTransitions.iterator();
		ModuleStateChange change1 = iterator.next();
		ModuleStateChange change2 = iterator.next();

		assertEquals("plugin1", change1.getPluginSpec().getName());
		assertEquals(ModuleTransition.LOADED_TO_UNLOADED, change1.getTransition());
		assertEquals("plugin1", change2.getPluginSpec().getName());
		assertEquals(ModuleTransition.UNLOADED_TO_LOADED, change2.getTransition());
	}

	public void testReloadLikeDifferentName() {
		// this test will only unload because there is not an exact match in the
		// plugin to load
		RootModuleDefinition parentSpec1 = ModuleModificationTestUtils.spec("app-context1.xml", "in1, plugin2");
		RootModuleDefinition parentSpec2 = ModuleModificationTestUtils.spec("app-context1.xml", "plugin1, plugin2");

		ModuleTransitionSet transitions = calculator.reloadLike(parentSpec1, parentSpec2, "in1");
		assertSame(parentSpec2, transitions.getNewSpec());

		Collection<? extends ModuleStateChange> pluginTransitions = transitions.getPluginTransitions();
		assertEquals(1, pluginTransitions.size());

		Iterator<? extends ModuleStateChange> iterator = pluginTransitions.iterator();
		ModuleStateChange change1 = iterator.next();

		assertEquals("plugin1", change1.getPluginSpec().getName());
		assertEquals(ModuleTransition.UNLOADED_TO_LOADED, change1.getTransition());
	}

	public void testReloadChanged() {
		RootModuleDefinition parentSpec1 = ModuleModificationTestUtils.spec("app-context1.xml", "plugin1, plugin2, plugin3");
		RootModuleDefinition parentSpec2 = ModuleModificationTestUtils.spec("app-context1.xml", "plugin1 (myPlugins:one), plugin2");

		ModuleTransitionSet transitions = calculator.reload(parentSpec1, parentSpec2, "plugin1");
		assertSame(parentSpec2, transitions.getNewSpec());

		Collection<? extends ModuleStateChange> pluginTransitions = transitions.getPluginTransitions();
		assertEquals(2, pluginTransitions.size());

		Iterator<? extends ModuleStateChange> iterator = pluginTransitions.iterator();
		ModuleStateChange change1 = iterator.next();
		ModuleStateChange change2 = iterator.next();

		ModuleDefinition pluginSpec1 = change1.getPluginSpec();
		assertEquals("plugin1", pluginSpec1.getName());
		assertEquals(ModuleTransition.LOADED_TO_UNLOADED, change1.getTransition());
		assertFalse(pluginSpec1 instanceof BeansetModuleDefinition);

		ModuleDefinition pluginSpec2 = change2.getPluginSpec();
		assertEquals("plugin1", pluginSpec2.getName());
		assertEquals(ModuleTransition.UNLOADED_TO_LOADED, change2.getTransition());
		assertTrue(pluginSpec2 instanceof BeansetModuleDefinition);
		BeansetModuleDefinition b = (BeansetModuleDefinition) pluginSpec2;
		assertFalse(b.getOverrides().isEmpty());
	}

	public void testAddedChild() {
		RootModuleDefinition parentSpec1 = ModuleModificationTestUtils.spec("app-context1.xml", "plugin1, plugin2");
		RootModuleDefinition parentSpec2 = ModuleModificationTestUtils.spec("app-context1.xml", "plugin1, plugin3, plugin2, plugin4");

		ModuleTransitionSet transitions = calculator.getTransitions(parentSpec1, parentSpec2);
		assertSame(parentSpec2, transitions.getNewSpec());

		Collection<? extends ModuleStateChange> pluginTransitions = transitions.getPluginTransitions();
		assertEquals(2, pluginTransitions.size());

		Iterator<? extends ModuleStateChange> iterator = pluginTransitions.iterator();

		ModuleStateChange change1 = iterator.next();
		assertEquals("plugin3", change1.getPluginSpec().getName());
		assertEquals(ModuleTransition.UNLOADED_TO_LOADED, change1.getTransition());

		ModuleStateChange change2 = iterator.next();
		assertEquals("plugin4", change2.getPluginSpec().getName());
		assertEquals(ModuleTransition.UNLOADED_TO_LOADED, change2.getTransition());
	}

	public void testChangeChild() {
		RootModuleDefinition parentSpec1 = ModuleModificationTestUtils.spec("app-context1.xml", "plugin1, plugin2, plugin3");
		RootModuleDefinition parentSpec2 = ModuleModificationTestUtils.spec("app-context1.xml", "plugin1 (myPlugins:one), plugin2");

		ModuleDefinition plugin2 = parentSpec2.findPlugin("plugin2", true);
		new SimpleModuleDefinition(plugin2, "plugin4");

		ModuleTransitionSet transitions = calculator.getTransitions(parentSpec1, parentSpec2);
		assertSame(parentSpec2, transitions.getNewSpec());

		Collection<? extends ModuleStateChange> pluginTransitions = transitions.getPluginTransitions();
		assertEquals(4, pluginTransitions.size());

		Iterator<? extends ModuleStateChange> iterator = pluginTransitions.iterator();

		ModuleStateChange change1 = iterator.next();
		assertEquals("plugin1", change1.getPluginSpec().getName());
		assertEquals(ModuleTransition.LOADED_TO_UNLOADED, change1.getTransition());

		ModuleStateChange change2 = iterator.next();
		assertEquals("plugin1", change2.getPluginSpec().getName());
		assertEquals(ModuleTransition.UNLOADED_TO_LOADED, change2.getTransition());

		ModuleStateChange change3 = iterator.next();
		assertEquals("plugin4", change3.getPluginSpec().getName());
		assertEquals(ModuleTransition.UNLOADED_TO_LOADED, change3.getTransition());
		
		ModuleStateChange change4 = iterator.next();
		assertEquals("plugin3", change4.getPluginSpec().getName());
		assertEquals(ModuleTransition.LOADED_TO_UNLOADED, change4.getTransition());
	}

}
