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

package org.impalaframework.command.interactive;

import junit.framework.TestCase;

import org.impalaframework.facade.DynamicContextHolder;
import org.impalaframework.module.builder.SimpleModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.resolver.LocationConstants;

public class Test1 extends TestCase implements ModuleDefinitionSource {
	
	public static final String plugin1 = "impala-sample-dynamic-plugin1";

	ModuleDefinitionSource source = new SimpleModuleDefinitionSource("parentTestContext.xml", new String[] { plugin1 });

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty(LocationConstants.ROOT_PROJECTS_PROPERTY, "impala-core");
		DynamicContextHolder.init(this);
	}
	
	public void testMyMethod() throws Exception {
		System.out.println("Running test method with " + DynamicContextHolder.getRootContext());
	}
	
	public RootModuleDefinition getModuleDefinition() {
		return source.getModuleDefinition();
	}
}
