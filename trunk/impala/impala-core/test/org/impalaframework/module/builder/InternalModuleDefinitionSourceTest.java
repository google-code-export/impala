package org.impalaframework.module.builder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.resolver.StandaloneModuleLocationResolver;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import junit.framework.TestCase;

public class InternalModuleDefinitionSourceTest extends TestCase {

	private InternalModuleDefinitionSource moduleDefinitionSource;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		StandaloneModuleLocationResolver resolver = new StandaloneModuleLocationResolver();
		
		moduleDefinitionSource = new InternalModuleDefinitionSource(resolver, 
				new String[]{
				"impala-core", 
				"sample-module1", 
				"sample-module2", 
				"sample-module3", 
				"sample-module4"}
		, true);
	}

	public void testGetModuleDefinition() {
		RootModuleDefinition moduleDefinition = moduleDefinitionSource.getModuleDefinition();
		System.out.println(moduleDefinition);
	}
	
	public void testGetResourceForModule() throws IOException {
		URL resourceForModule = moduleDefinitionSource.getResourceForModule("sample-module1", "module.properties");
		assertNotNull(resourceForModule);
		
		InputStream inputStream = resourceForModule.openStream();
		String text = FileCopyUtils.copyToString(new InputStreamReader(inputStream));
		assertTrue(StringUtils.hasText(text));
	}

}
