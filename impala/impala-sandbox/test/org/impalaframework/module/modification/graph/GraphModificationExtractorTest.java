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
	
	private GraphModificationExtractor graphModificationExtractor;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		graphModificationExtractor = new GraphModificationExtractor();
	}

	public void testExtraction() throws Exception {
		List<ModuleDefinition> definitions = new ArrayList<ModuleDefinition>();
		
		ModuleDefinition a = newDefinition(definitions, null, "a", null);
		ModuleDefinition b = newDefinition(definitions, null, "b", null);
		ModuleDefinition c = newDefinition(definitions, null, "c", null);
		ModuleDefinition d = newDefinition(definitions, null, "d", null);
		SimpleGraphRootModuleDefinition root = new SimpleGraphRootModuleDefinition("root", 
				Collections.singletonList("root.xml"), 
				Arrays.asList("a", "b"), 
				Arrays.asList(a, b, c, d));
		newDefinition(definitions, root, "e", "b,d");
		
		final Collection<? extends ModuleStateChange> moduleTransitions = graphModificationExtractor.getTransitions(null, root).getModuleTransitions();
		for (ModuleStateChange moduleStateChange : moduleTransitions) {
			System.out.println(moduleStateChange.getTransition() + " - " + moduleStateChange.getModuleDefinition().getName());
		}
	}
	
	private ModuleDefinition newDefinition(List<ModuleDefinition> list, ModuleDefinition parent, final String name, final String dependencies) {
		final String[] split = dependencies != null ? dependencies.split(",") : new String[0];
		final List<String> dependencyList = Arrays.asList(split);
		GraphModuleDefinition definition = new SimpleGraphModuleDefinition(parent, name, dependencyList);
		list.add(definition);
		return definition;
	}
}
