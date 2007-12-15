/*
 * Copyright 2007 the original author or authors.
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

package tests;

import interfaces.WineDAO;

import org.impalaframework.plugin.builder.SimplePluginSpecBuilder;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.testrun.DynamicContextHolder;
import org.impalaframework.testrun.PluginTestRunner;

import test.BaseIntegrationTest;

public class InitialIntegrationTest extends BaseIntegrationTest {

	public static void main(String[] args) {
		System.setProperty("impala.parent.project", "wineorder");
		PluginTestRunner.run(InitialIntegrationTest.class);
	}

	public void testIntegration() {
		DynamicContextHolder.getBean(this, "wineDAO", WineDAO.class);
	}

	public ParentSpec getPluginSpec() {
		return new SimplePluginSpecBuilder(new String[] { "parent-context.xml", "merchant-context.xml" },
				new String[] {}).getPluginSpec();
	}

}