package org.impalaframework.module.builder;

import junit.framework.TestCase;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.builder.XmlSpecDocumentLoader;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;

public class XmlSpecDocumentLoaderTest extends TestCase {

	public final void testLoadDocument() {
		XmlSpecDocumentLoader builder = new XmlSpecDocumentLoader();
		Document document = builder.loadDocument(new ClassPathResource("xmlspec/pluginspec.xml"));
		assertNotNull(document);
	}
	
	public final void testNotPresentLoadDocument() {
		XmlSpecDocumentLoader builder = new XmlSpecDocumentLoader();
		try {
			builder.loadDocument(new ClassPathResource("xmlspec/notpresent.xml"));
			fail();
		}
		catch (ConfigurationException e) {
			assertEquals("Could not load plugin specification, as unable to obtain input stream for resource class path resource [xmlspec/notpresent.xml]", e.getMessage());
		}
	}
	
	public final void testBadlyFormedLoadDocument() {
		XmlSpecDocumentLoader builder = new XmlSpecDocumentLoader();
		try {
			builder.loadDocument(new ClassPathResource("xmlspec/badlyformedspec.xml"));
			fail();
		}
		catch (ConfigurationException e) {
			assertEquals("Unable to load XML plugin specification document from resource class path resource [xmlspec/badlyformedspec.xml]", e.getMessage());
		}
	}

}
