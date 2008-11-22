package org.impalaframework.module.modification.graph;

import static org.impalaframework.classloader.graph.GraphTestUtils.cloneAndMarkStale;
import static org.impalaframework.classloader.graph.GraphTestUtils.newDefinition;
import static org.impalaframework.classloader.graph.GraphTestUtils.printTransitions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.module.ModuleStateChange;
import org.impalaframework.module.Transition;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;

public class GraphModificationExtractorTest extends TestCase {
	
	private GraphModificationExtractorDelegate graphModificationExtractor;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		graphModificationExtractor = new GraphModificationExtractorDelegate();
	}
	
	public void testNullToRoot1() throws Exception {
		SimpleRootModuleDefinition root1 = definitionSet1();
		assertTransitions(null, root1, null, "d,a,c,b,root,e,f,g");
	}
	
	public void testNullToRoot2() throws Exception {
		SimpleRootModuleDefinition root2 = definitionSet2();
		assertTransitions(null, root2, null, "a,c,root,e,f");
	}
	
	public void testRoot1ToNull() throws Exception {
		SimpleRootModuleDefinition root1 = definitionSet1();
		assertTransitions(root1, null, "g,f,e,root,b,c,a,d", null);
	}
	
	public void testRoot2ToNull() throws Exception {
		SimpleRootModuleDefinition root2 = definitionSet2();
		assertTransitions(root2, null, "f,e,root,c,a", null);
	}

	public void testRoot1ToRoot2() throws Exception {
		SimpleRootModuleDefinition root1 = definitionSet1();
		SimpleRootModuleDefinition root2 = definitionSet2();
		
		assertTransitions(root1, root2, "g,f,e,root,b,d", "root,e,f");
	}
	
	public void testRoot2ToRoot1() throws Exception {
		SimpleRootModuleDefinition root1 = definitionSet1();
		SimpleRootModuleDefinition root2 = definitionSet2();
		
		assertTransitions(root2, root1, "f,e,root", "d,b,root,e,f,g");
	}
	
	public void testRoot2ToRoot3() throws Exception {
		SimpleRootModuleDefinition root2 = definitionSet2();
		SimpleRootModuleDefinition root3 = definitionSet3();
		
		assertTransitions(root2, root3, null, "d,g");
	}
	
	public void testRoot3ToRoot2() throws Exception {
		SimpleRootModuleDefinition root2 = definitionSet2();
		SimpleRootModuleDefinition root3 = definitionSet3();
		
		assertTransitions(root3, root2, "g,d", null);
	}
	
	public void testRoot3ToRoot4() throws Exception {
		SimpleRootModuleDefinition root3 = definitionSet3();
		SimpleRootModuleDefinition root4 = definitionSet4();
		
		assertTransitions(root3, root4, "g", "g");
	}
	
	public void testRoot4ToRoot3() throws Exception {
		SimpleRootModuleDefinition root3 = definitionSet3();
		SimpleRootModuleDefinition root4 = definitionSet4();
		
		assertTransitions(root4, root3, "g", "g");
	}
	
	public void testReloadA() throws Exception {
		
		SimpleRootModuleDefinition root1 = definitionSet1();
		SimpleRootModuleDefinition reload = cloneAndMarkStale(root1, "a");
		assertTransitions(root1, reload, "g,f,e,root,a", "a,root,e,f,g");
	}
	
	public void testReloadB() throws Exception {
		
		SimpleRootModuleDefinition root1 = definitionSet1();
		SimpleRootModuleDefinition reload = cloneAndMarkStale(root1, "b");
		assertTransitions(root1, reload, "g,f,e,root,b", "b,root,e,f,g");
	}
	
	public void testReloadC() throws Exception {
		
		SimpleRootModuleDefinition root1 = definitionSet1();
		SimpleRootModuleDefinition reload = cloneAndMarkStale(root1, "c");
		assertTransitions(root1, reload, "g,f,c", "c,f,g");
	}
	
	public void testReloadD() throws Exception {
		
		SimpleRootModuleDefinition root1 = definitionSet1();
		SimpleRootModuleDefinition reload = cloneAndMarkStale(root1, "d");
		assertTransitions(root1, reload, "g,f,e,root,b,d", "d,b,root,e,f,g");
	}
	
	public void testReloadRoot() throws Exception {
		
		SimpleRootModuleDefinition root1 = definitionSet1();
		SimpleRootModuleDefinition reload = cloneAndMarkStale(root1, "root");
		assertTransitions(root1, reload, "g,f,e,root", "root,e,f,g");
	}
	
	public void testReloadE() throws Exception {
		
		SimpleRootModuleDefinition root1 = definitionSet1();
		SimpleRootModuleDefinition reload = cloneAndMarkStale(root1, "e");
		assertTransitions(root1, reload, "g,f,e", "e,f,g");
	}
	
	public void testReloadF() throws Exception {
		
		SimpleRootModuleDefinition root1 = definitionSet1();
		SimpleRootModuleDefinition reload = cloneAndMarkStale(root1, "f");
		assertTransitions(root1, reload, "g,f", "f,g");
	}
	
	public void testReloadG() throws Exception {
		
		SimpleRootModuleDefinition root1 = definitionSet1();
		SimpleRootModuleDefinition reload = cloneAndMarkStale(root1, "g");
		assertTransitions(root1, reload, "g", "g");
	}
	
	private SimpleRootModuleDefinition definitionSet1() {
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
		SimpleRootModuleDefinition root = new SimpleRootModuleDefinition("root", 
				new String[] {"root.xml"}, 
				new String[] {"a", "b"}, 
				new ModuleDefinition[] {a, b, c, d});
		//e has parent root, and depends on b an d
		ModuleDefinition e = newDefinition(definitions, root, "e", "b,d");
		
		//has parent e, and depends on c
		newDefinition(definitions, e, "f", "c");

		//has parent e, depends on f
		newDefinition(definitions, e, "g", "f");
		return root;
	}
	
	private SimpleRootModuleDefinition definitionSet2() {
		List<ModuleDefinition> definitions = new ArrayList<ModuleDefinition>();

		//a has no parent or dependencies
		ModuleDefinition a = newDefinition(definitions, null, "a", null);

		//c has no parent or dependencies
		ModuleDefinition c = newDefinition(definitions, null, "c", null);
		
		//root has siblings a and c, and depends on a
		SimpleRootModuleDefinition root = new SimpleRootModuleDefinition("root", 
				new String[] {"root.xml"}, 
				new String[] {"a"}, 
				new ModuleDefinition[] {a, c});
		
		//e has parent root, and depends on a and c
		ModuleDefinition e = newDefinition(definitions, root, "e", "a,c");
		
		//f has parent e, but no other dependencies
		newDefinition(definitions, e, "f", null);
		return root;
	}
	
	//same as 3, but adds d, and adds adds g, which depends on d
	private SimpleRootModuleDefinition definitionSet3() {
		List<ModuleDefinition> definitions = new ArrayList<ModuleDefinition>();

		//a has no parent or dependencies
		ModuleDefinition a = newDefinition(definitions, null, "a", null);

		//c has no parent or dependencies
		ModuleDefinition c = newDefinition(definitions, null, "c", null);
		
		ModuleDefinition d = newDefinition(definitions, null, "d", null);
		
		//root has siblings a and c, and depends on a
		SimpleRootModuleDefinition root = new SimpleRootModuleDefinition("root", 
				new String[] {"root.xml"}, 
				new String[] {"a"}, 
				new ModuleDefinition[] {a, c, d});
		
		//e has parent root, and depends on a and c
		ModuleDefinition e = newDefinition(definitions, root, "e", "a,c");
		
		//f has parent e, but no other dependencies
		newDefinition(definitions, e, "f", null);

		//has parent e, depends on d
		newDefinition(definitions, e, "g", "d");
		return root;
	}
	

	//same as 4, but g does not depend on d
	private SimpleRootModuleDefinition definitionSet4() {
		List<ModuleDefinition> definitions = new ArrayList<ModuleDefinition>();

		//a has no parent or dependencies
		ModuleDefinition a = newDefinition(definitions, null, "a", null);

		//c has no parent or dependencies
		ModuleDefinition c = newDefinition(definitions, null, "c", null);
		
		ModuleDefinition d = newDefinition(definitions, null, "d", null);
		
		//root has siblings a and c, and depends on a
		SimpleRootModuleDefinition root = new SimpleRootModuleDefinition("root", 
				new String[] {"root.xml"}, 
				new String[] {"a"}, 
				new ModuleDefinition[] {a, c, d});
		
		//e has parent root, and depends on a and c
		ModuleDefinition e = newDefinition(definitions, root, "e", "a,c");
		
		//f has parent e, but no other dependencies
		newDefinition(definitions, e, "f", null);

		//has parent e, depends on d
		newDefinition(definitions, e, "g", null);
		return root;
	}


	private void assertTransitions(SimpleRootModuleDefinition root1,
			SimpleRootModuleDefinition root2, String expectedUnloads, String expectedLoads) {
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
}
