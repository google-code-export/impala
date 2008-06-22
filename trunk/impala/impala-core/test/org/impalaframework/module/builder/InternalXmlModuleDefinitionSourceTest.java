package org.impalaframework.module.builder;

import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.resolver.StandaloneModuleLocationResolver;
import org.springframework.core.io.ClassPathResource;

import junit.framework.TestCase;

public class InternalXmlModuleDefinitionSourceTest extends TestCase {

	private InternalXmlModuleDefinitionSource moduleDefinitionSource;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		moduleDefinitionSource = new InternalXmlModuleDefinitionSource(new StandaloneModuleLocationResolver());
		moduleDefinitionSource.setResource(new ClassPathResource("xmlinternal/moduledefinition.xml"));
	}
	
	public void testGetModuleDefinition() {
		RootModuleDefinition moduleDefinition = moduleDefinitionSource.getModuleDefinition();
		System.out.println(moduleDefinition);
		//FIXME add asserts
	}

}
