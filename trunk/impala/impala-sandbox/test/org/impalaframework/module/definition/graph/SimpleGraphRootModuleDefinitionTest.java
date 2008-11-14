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

package org.impalaframework.module.definition.graph;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;

import junit.framework.TestCase;

public class SimpleGraphRootModuleDefinitionTest extends TestCase {

	public void testSimpleGraphRootModuleDefinition() {
		final List<String> locations = Arrays.asList("c1.xml", "c2.xml");
		final List<String> dependencies = Arrays.asList("mod-a", "mod-b");
		
		final ModuleDefinition moduleDefinition = new SimpleModuleDefinition("mod-c");
		final List<ModuleDefinition> siblings = Collections.singletonList(moduleDefinition);
		
		SimpleGraphRootModuleDefinition definition = 
			new SimpleGraphRootModuleDefinition("prpject1",
					locations, 
					dependencies, 
					siblings);
		
		assertEquals(Arrays.asList(definition.getSiblings()), siblings);
		assertEquals(Arrays.asList(definition.getDependentModuleNames()), dependencies);
	}

}
