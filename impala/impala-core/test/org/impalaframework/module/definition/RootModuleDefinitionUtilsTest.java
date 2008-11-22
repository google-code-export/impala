package org.impalaframework.module.definition;

import org.impalaframework.exception.ConfigurationException;

import junit.framework.TestCase;

public class RootModuleDefinitionUtilsTest extends TestCase {

	public void testGetProjectNameList() {
		try {
			RootModuleDefinitionUtils.getProjectNameList(new SimpleModuleDefinition("moduleName"));
			fail();
		} catch (ConfigurationException e) {
			assertEquals("Attempting to get root project names from module definition instance [name=moduleName, contextLocations=[moduleName-context.xml], type=APPLICATION, dependencies=[]], an instance of org.impalaframework.module.definition.SimpleModuleDefinition, which is not an instance of org.impalaframework.module.definition.RootModuleDefinition", e.getMessage());
		}
		
		assertEquals("myModule", RootModuleDefinitionUtils.getProjectNameList(new SimpleRootModuleDefinition("myModule", "mycontext.xml")).iterator().next());
	}

}
