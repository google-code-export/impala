package org.impalaframework.plugin.builder;

import org.impalaframework.exception.ConfigurationException;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;

import junit.framework.TestCase;

public class XmlPluginSpecBuilderTest extends TestCase {

	public final void testLoadDocument() {
		XmlPluginSpecBuilder builder = new XmlPluginSpecBuilder();
		builder.setResource(new ClassPathResource("xmlspec/pluginspec.xml"));
		Document document = builder.loadDocument();
		assertNotNull(document);
	}
	
	public final void testNotPresentLoadDocument() {
		XmlPluginSpecBuilder builder = new XmlPluginSpecBuilder();
		builder.setResource(new ClassPathResource("xmlspec/notpresent.xml"));
		try {
			builder.loadDocument();
			fail();
		}
		catch (ConfigurationException e) {
			assertEquals("Could not load plugin specification, as unable to obtain input stream for resource class path resource [xmlspec/notpresent.xml]", e.getMessage());
		}
	}
	
	public final void testBadlyFormedLoadDocument() {
		XmlPluginSpecBuilder builder = new XmlPluginSpecBuilder();
		builder.setResource(new ClassPathResource("xmlspec/badlyformedspec.xml"));
		try {
			builder.loadDocument();
			fail();
		}
		catch (ConfigurationException e) {
			assertEquals("Unable to load XML plugin specification document from resource class path resource [xmlspec/badlyformedspec.xml]", e.getMessage());
		}
	}

}
