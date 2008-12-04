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

package org.impalaframework.osgiroot.test;

import org.impalaframework.definition.source.TestDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.osgi.test.OsgiIntegrationTest;
import org.osgi.framework.Bundle;
import org.springframework.osgi.util.OsgiStringUtils;

public class OsgiContextTest extends OsgiIntegrationTest {
	
	public OsgiContextTest() {
		super();
		System.setProperty("impala.module.class.dir", "target/classes");
	}
	
	public void testOsgiEnvironment() throws Exception {
		Bundle[] bundles = bundleContext.getBundles();
		
		System.out.println("Bundles loaded <------------------- ");
		for (int i = 0; i < bundles.length; i++) {
			System.out.print(OsgiStringUtils.nullSafeName(bundles[i]));
			System.out.println(" (" + bundles[i].getSymbolicName() + ")");
		}
		System.out.println("------------------->");
		Thread.sleep(2000);
	}

	public RootModuleDefinition getModuleDefinition() {
		return new TestDefinitionSource("osgi-root", "osgi-module1").getModuleDefinition();
	}
	
}