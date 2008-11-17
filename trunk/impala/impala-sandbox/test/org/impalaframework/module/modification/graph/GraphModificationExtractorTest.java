package org.impalaframework.module.modification.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.module.ModuleStateChange;
import org.impalaframework.module.Transition;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleState;
import org.impalaframework.module.definition.graph.GraphModuleDefinition;
import org.impalaframework.module.definition.graph.SimpleGraphModuleDefinition;
import org.impalaframework.module.definition.graph.SimpleGraphRootModuleDefinition;
import org.impalaframework.util.SerializationUtils;

public class GraphModificationExtractorTest extends TestCase {
	
	//FIXME need to test this to death!
	
	private GraphModificationExtractor graphModificationExtractor;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		graphModificationExtractor = new GraphModificationExtractor();
	}
	
	public void testNullToRoot1() throws Exception {
		SimpleGraphRootModuleDefinition root1 = definitionSet1();
		assertTransitions(null, root1, null, "d,a,c,b,root,e,f");
	}
	
	public void testNullToRoot2() throws Exception {
		SimpleGraphRootModuleDefinition root2 = definitionSet2();
		assertTransitions(null, root2, null, "a,c,root,e,f");
	}
	
	public void testRoot1ToNull() throws Exception {
		SimpleGraphRootModuleDefinition root1 = definitionSet1();
		assertTransitions(root1, null, "f,e,root,b,c,a,d", null);
	}
	
	public void testRoot2ToNull() throws Exception {
		SimpleGraphRootModuleDefinition root2 = definitionSet2();
		assertTransitions(root2, null, "f,e,root,c,a", null);
	}

	public void testRoot1ToRoot2() throws Exception {
		SimpleGraphRootModuleDefinition root1 = definitionSet1();
		SimpleGraphRootModuleDefinition root2 = definitionSet2();
		
		//FIXME should remove b and d
		assertTransitions(root1, root2, "f,e,root,b,d", "root,e,f");
	}
	
	public void testRoot2ToRoot1() throws Exception {
		SimpleGraphRootModuleDefinition root1 = definitionSet1();
		SimpleGraphRootModuleDefinition root2 = definitionSet2();
		
		assertTransitions(root2, root1, "f,e,root", "d,b,root,e,f");
	}
	
	public void testRoot1ToRoot3() throws Exception {
		
		SimpleGraphRootModuleDefinition root1 = definitionSet1();
		SimpleGraphRootModuleDefinition root3 = cloneAndMarkStale(root1, "a");
		assertTransitions(root1, root3, "f,e,root,a", "a,root,e,f");
	}
	
	private SimpleGraphRootModuleDefinition definitionSet1() {
		List<ModuleDefinition> definitions = new ArrayList<ModuleDefinition>();
		
		//a has no parent or dependencies
		ModuleDefinition a = newDefinition(definitions, null, "a", null);
		
		//b depends on d but has no parent
		ModuleDefinition b = newDefinition(definitions, null, "b", "d");
		
		//c has no parent or dependencies
		ModuleDefinition c = newDefinition(definitions, null, "c", null);
		
		//d has no parent or dependencies
		ModuleDefinition d = newDefinition(definitions, null, "d", null);
		
		//root has siblings a to d, and depends on a and b
		SimpleGraphRootModuleDefinition root = new SimpleGraphRootModuleDefinition("root", 
				Collections.singletonList("root.xml"), 
				Arrays.asList("a", "b"), 
				Arrays.asList(a, b, c, d));
		//e has parent root, and depends on b an d
		ModuleDefinition e = newDefinition(definitions, root, "e", "b,d");
		
		//has parent e, and depends on c
		newDefinition(definitions, e, "f", "c");
		return root;
	}
	
	private SimpleGraphRootModuleDefinition definitionSet2() {
		List<ModuleDefinition> definitions = new ArrayList<ModuleDefinition>();

		//a has no parent or dependencies
		ModuleDefinition a = newDefinition(definitions, null, "a", null);

		//c has no parent or dependencies
		ModuleDefinition c = newDefinition(definitions, null, "c", null);
		
		//root has siblings a and c, and depends on a
		SimpleGraphRootModuleDefinition root = new SimpleGraphRootModuleDefinition("root", 
				Collections.singletonList("root.xml"), 
				Arrays.asList("a"), 
				Arrays.asList(a, c));
		
		//e has parent root, and depends on a and c
		ModuleDefinition e = newDefinition(definitions, root, "e", "a,c");
		
		//f has parent e, but no other dependencies
		newDefinition(definitions, e, "f", null);
		return root;
	}


	private void assertTransitions(SimpleGraphRootModuleDefinition root1,
			SimpleGraphRootModuleDefinition root2, String expectedUnloads, String expectedLoads) {
		final Collection<? extends ModuleStateChange> transitions = graphModificationExtractor.getTransitions(root1, root2).getModuleTransitions();
		
		List<String> loads = new ArrayList<String>();
		List<String> unloads = new ArrayList<String>();
		for (ModuleStateChange moduleStateChange : transitions) {
			if (moduleStateChange.getTransition().equals(Transition.UNLOADED_TO_LOADED)) loads.add(moduleStateChange.getModuleDefinition().getName());
			if (moduleStateChange.getTransition().equals(Transition.LOADED_TO_UNLOADED)) unloads.add(moduleStateChange.getModuleDefinition().getName());
		}

		printTransitions(transitions);
		
		//now do expectations
		if (expectedUnloads != null) {
			List<String> expectedUnLoadsList = Arrays.asList(expectedUnloads.split(","));
			assertEquals("Failure comparing expected unload ordering", expectedUnLoadsList, unloads);
		} else {
			assertTrue("Unloads expected to be empty", unloads.isEmpty());
		}
		
		if (expectedLoads != null) {
			List<String> expectedLoadsList = Arrays.asList(expectedLoads.split(","));
			assertEquals("Failure comparing expected unload ordering", expectedLoadsList, loads);
		} else {
			assertTrue("Loads expected to be empty", loads.isEmpty());
		}
	}
	private void printTransitions(
			final Collection<? extends ModuleStateChange> moduleTransitions) {
		for (ModuleStateChange moduleStateChange : moduleTransitions) {
			System.out.println(moduleStateChange.getTransition() + " - " + moduleStateChange.getModuleDefinition().getName());
		}
	}

	private SimpleGraphRootModuleDefinition cloneAndMarkStale(SimpleGraphRootModuleDefinition root1,
			final String toReload) {
		final SimpleGraphRootModuleDefinition clone = (SimpleGraphRootModuleDefinition) SerializationUtils.clone(root1);
		final ModuleDefinition child = clone.findChildDefinition(toReload, true);
		System.out.println("Marking " + toReload + " as stale");
		child.setState(ModuleState.STALE);
		return clone;
	}
	
	private ModuleDefinition newDefinition(List<ModuleDefinition> list, ModuleDefinition parent, final String name, final String dependencies) {
		final String[] split = dependencies != null ? dependencies.split(",") : new String[0];
		final List<String> dependencyList = Arrays.asList(split);
		GraphModuleDefinition definition = new SimpleGraphModuleDefinition(parent, name, dependencyList);
		list.add(definition);
		return definition;
	}
}
