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

import org.impalaframework.module.builder.SimpleModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleBeansetModuleDefinition;
import org.impalaframework.testrun.InteractiveTestRunner;

public class AlternativeWineMerchantTest extends WineMerchantTest {

	public static void main(String[] args) {
		InteractiveTestRunner.run(AlternativeWineMerchantTest.class);
	}

	public RootModuleDefinition getModuleDefinition() {
		SimpleModuleDefinitionSource definition = new SimpleModuleDefinitionSource(new String[] { "parent-context.xml", "merchant-context.xml" }, 
						new String[] {
						"wineorder-hibernate", "wineorder-dao" });
		
		RootModuleDefinition parent = definition.getModuleDefinition();
		new SimpleBeansetModuleDefinition(parent, "wineorder-merchant", "alternative: myImports");
		
		return definition.getModuleDefinition();
	}

}