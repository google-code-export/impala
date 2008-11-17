package org.impalaframework.module.modification.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.module.ModuleStateChange;
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
		printTransitions(graphModificationExtractor.getTransitions(null, root1).getModuleTransitions());
	}
	
	public void testNullToRoot2() throws Exception {
		SimpleGraphRootModuleDefinition root2 = definitionSet2();
		
		//FIXME should remove b and d
		printTransitions(graphModificationExtractor.getTransitions(null, root2).getModuleTransitions());
	}

	public void testRoot1ToRoot2() throws Exception {
		SimpleGraphRootModuleDefinition root1 = definitionSet1();
		SimpleGraphRootModuleDefinition root2 = definitionSet2();
		
		//FIXME should remove b and d
		printTransitions(graphModificationExtractor.getTransitions(root1, root2).getModuleTransitions());
	}
	
	public void testRoot2ToRoot1() throws Exception {
		SimpleGraphRootModuleDefinition root1 = definitionSet1();
		SimpleGraphRootModuleDefinition root2 = definitionSet2();
		
		//FIXME should add b and d
		printTransitions(graphModificationExtractor.getTransitions(root2, root1).getModuleTransitions());
	}
	
	public void testRoot1ToRoot3() throws Exception {
		
		SimpleGraphRootModuleDefinition root1 = definitionSet1();
		SimpleGraphRootModuleDefinition root3 = cloneAndMarkStale(root1, "a");
		printTransitions(graphModificationExtractor.getTransitions(root1, root3).getModuleTransitions());
	}
	
	public void testExtraction() throws Exception {
		SimpleGraphRootModuleDefinition root1 = definitionSet1();
		
		printTransitions(graphModificationExtractor.getTransitions(null, root1).getModuleTransitions());
		
		SimpleGraphRootModuleDefinition root2 = definitionSet2();
		
		//FIXME should remove b and d
		printTransitions(graphModificationExtractor.getTransitions(root1, root2).getModuleTransitions());
		
		//FIXME should add b and d
		printTransitions(graphModificationExtractor.getTransitions(root2, root1).getModuleTransitions());

		SimpleGraphRootModuleDefinition root3 = cloneAndMarkStale(root1, "a");
		printTransitions(graphModificationExtractor.getTransitions(root1, root3).getModuleTransitions());
		
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
		newDefinition(definitions, root, "e", "b,d");
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
