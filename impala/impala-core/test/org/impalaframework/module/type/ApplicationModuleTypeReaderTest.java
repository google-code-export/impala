package org.impalaframework.module.type;

import java.util.Arrays;
import java.util.Properties;

import junit.framework.TestCase;

import org.impalaframework.module.builder.ModuleElementNames;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.util.XmlDomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ApplicationModuleTypeReaderTest extends TestCase {

	private ApplicationModuleTypeReader reader;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		reader = new ApplicationModuleTypeReader();
	}

	public void testReadModuleDefinition() {
		ModuleDefinition definition = reader.readModuleDefinition(null, "mymodule", new Properties());
		SimpleModuleDefinition moduleDefinition = (SimpleModuleDefinition) definition;
		assertEquals("mymodule", moduleDefinition.getName());
		assertEquals(ModuleTypes.APPLICATION, moduleDefinition.getType());
	}
	
	public void testReadModuleDefinitionLocations() {
		Properties properties = new Properties();
		properties.put(ModuleElementNames.CONTEXT_LOCATIONS_ELEMENT, "loc1, loc2,loc3");
		ModuleDefinition definition = reader.readModuleDefinition(null, "mymodule", properties);
		SimpleModuleDefinition moduleDefinition = (SimpleModuleDefinition) definition;
		assertEquals("mymodule", moduleDefinition.getName());
		assertEquals(ModuleTypes.APPLICATION, moduleDefinition.getType());
		assertEquals(Arrays.asList(new String[]{ "loc1", "loc2", "loc3"}), moduleDefinition.getContextLocations());
	}

	public void testReadModuleDefinitionProperties() throws Exception {
	    Document document = XmlDomUtils.newDocument();
	    Element root = document.createElement("root");
	    document.appendChild(root);
	    
		Element locations = document.createElement("context-locations");
	    root.appendChild(locations);
	    
	    Element location1 = document.createElement("context-location");
	    location1.setTextContent("location1");
	    locations.appendChild(location1);
	    
	    Element location2 = document.createElement("context-location");
	    location2.setTextContent("location2");
	    locations.appendChild(location2);
	    
		Properties properties = new Properties();
		reader.readModuleDefinitionProperties(properties, "mymodule", root);
		System.out.println(properties);
		assertEquals("location1,location2", properties.get("context-locations"));
	}
	
}
