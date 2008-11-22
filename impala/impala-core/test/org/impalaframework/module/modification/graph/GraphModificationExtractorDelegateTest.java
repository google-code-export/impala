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

package org.impalaframework.module.modification.graph;

import junit.framework.TestCase;

import org.impalaframework.module.TransitionSet;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;

public class GraphModificationExtractorDelegateTest extends TestCase {

	public void testGetTransitions() {
		GraphModificationExtractorDelegate delegate = new GraphModificationExtractorDelegate();
		SimpleRootModuleDefinition rootDefinitionOld = rootDefinition("orig");
		SimpleRootModuleDefinition rootDefinitionNew = rootDefinition("new");
		TransitionSet transitions = delegate.getTransitions(rootDefinitionOld, rootDefinitionNew);
		assertEquals(2, transitions.getModuleTransitions().size());
		
		assertNotNull(delegate.getNewDependencyManager());
		assertNotNull(delegate.getOldDependencyManager());
	}

	private SimpleRootModuleDefinition rootDefinition(String name) {
		SimpleRootModuleDefinition rootDefinition = new SimpleRootModuleDefinition(name, 
				new String[] {"context.xml"}, 
				new String[0], 
				new ModuleDefinition[0]);
		return rootDefinition;
	}

}
