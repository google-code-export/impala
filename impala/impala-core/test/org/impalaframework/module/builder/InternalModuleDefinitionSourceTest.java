package org.impalaframework.module.builder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import org.impalaframework.exception.ConfigurationException;
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

		moduleDefinitionSource = new InternalModuleDefinitionSource(resolver, new String[] { 
				"impala-core",
				"sample-module1", 
				"sample-module2", 
				"sample-module3", 
				"sample-module4" }, true);
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

	public void testGetNoResourceForModule() throws IOException {
		try {
			moduleDefinitionSource.getResourceForModule("sample-module1", "nothere.properties");
			fail();
		}
		catch (ConfigurationException e) {
			assertEquals(
					"Application is using internally defined module structure, but no module.properties file is present on the classpath for module sample-module1",
					e.getMessage());
		}
	}

	public void testMap() throws IOException {
		moduleDefinitionSource.loadProperties();
		Map<String, Properties> map = moduleDefinitionSource.getModuleProperties();
		assertEquals(5, map.size());
		for (String key : map.keySet()) {
			assertNotNull(map.get(key));
		}
	}

}
