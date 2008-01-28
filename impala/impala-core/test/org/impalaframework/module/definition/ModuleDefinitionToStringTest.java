package org.impalaframework.module.definition;

import java.io.InputStreamReader;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import junit.framework.TestCase;

public class ModuleDefinitionToStringTest extends TestCase {

	public void testToString() throws Exception {
		RootModuleDefinition definition = new SimpleRootModuleDefinition(new String[] { "location1.xml",
				"location2.xml" });
		SimpleModuleDefinition module1 = new SimpleModuleDefinition(definition, "module1");
		new SimpleModuleDefinition(definition, "module2", new String[] {
				"module2-1.xml", "module1-2.xml" });
		new SimpleBeansetModuleDefinition(definition, "module3", new String[] {
				"module3-1.xml", "module3-2.xml"}, "main: alternative");
		
		new SimpleModuleDefinition(module1, "module4");
		new SimpleModuleDefinition(module1, "module5");
		
		String output = definition.toString();
		System.out.println(output);
		
		ClassPathResource resource = new ClassPathResource("tostring/moduletostring.txt");
		String expected = FileCopyUtils.copyToString(new InputStreamReader(resource.getInputStream()));
		assertEquals(expected, output);
	}

}
