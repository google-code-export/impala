package org.impalaframework.module.modification.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.module.ModuleStateChange;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.graph.GraphModuleDefinition;
import org.impalaframework.module.definition.graph.SimpleGraphModuleDefinition;
import org.impalaframework.module.definition.graph.SimpleGraphRootModuleDefinition;

public class GraphModificationExtractorTest extends TestCase {
	
	//FIXME need to test this to death!
	
	private GraphModificationExtractor graphModificationExtractor;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		graphModificationExtractor = new GraphModificationExtractor();
	}

	public void testExtraction() throws Exception {
		SimpleGraphRootModuleDefinition root1 = definitionSet1();
		
		printTransitions(graphModificationExtractor.getTransitions(null, root1).getModuleTransitions());
		
		SimpleGraphRootModuleDefinition root2 = definitionSet2();
		
		//FIXME should remove b and d
		printTransitions(graphModificationExtractor.getTransitions(root1, root2).getModuleTransitions());
		
		//FIXME should add b and d
		printTransitions(graphModificationExtractor.getTransitions(root2, root1).getModuleTransitions());
	}

	private void printTransitions(
			final Collection<? extends ModuleStateChange> moduleTransitions) {
		for (ModuleStateChange moduleStateChange : moduleTransitions) {
			System.out.println(moduleStateChange.getTransition() + " - " + moduleStateChange.getModuleDefinition().getName());
		}
	}

	private SimpleGraphRootModuleDefinition definitionSet1() {
		List<ModuleDefinition> definitions = new ArrayList<ModuleDefinition>();
		
		ModuleDefinition a = newDefinition(definitions, null, "a", null);
		ModuleDefinition b = newDefinition(definitions, null, "b", "d");
		ModuleDefinition c = newDefinition(definitions, null, "c", null);
		ModuleDefinition d = newDefinition(definitions, null, "d", null);
		SimpleGraphRootModuleDefinition root = new SimpleGraphRootModuleDefinition("root", 
				Collections.singletonList("root.xml"), 
				Arrays.asList("a", "b"), 
				Arrays.asList(a, b, c, d));
		newDefinition(definitions, root, "e", "b,d");
		return root;
	}
	
	private SimpleGraphRootModuleDefinition definitionSet2() {
		List<ModuleDefinition> definitions = new ArrayList<ModuleDefinition>();
		
		ModuleDefinition a = newDefinition(definitions, null, "a", null);
		ModuleDefinition c = newDefinition(definitions, null, "c", null);
		SimpleGraphRootModuleDefinition root = new SimpleGraphRootModuleDefinition("root", 
				Collections.singletonList("root.xml"), 
				Arrays.asList("a"), 
				Arrays.asList(a, c));
		ModuleDefinition e = newDefinition(definitions, root, "e", "a,c");
		newDefinition(definitions, e, "f", null);
		return root;
	}
	
	private ModuleDefinition newDefinition(List<ModuleDefinition> list, ModuleDefinition parent, final String name, final String dependencies) {
		final String[] split = dependencies != null ? dependencies.split(",") : new String[0];
		final List<String> dependencyList = Arrays.asList(split);
		GraphModuleDefinition definition = new SimpleGraphModuleDefinition(parent, name, dependencyList);
		list.add(definition);
		return definition;
	}
}
