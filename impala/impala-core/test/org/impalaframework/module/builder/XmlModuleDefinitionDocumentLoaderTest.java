package org.impalaframework.module.builder;

import junit.framework.TestCase;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.builder.XmlModulelDefinitionDocumentLoader;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;

public class XmlModuleDefinitionDocumentLoaderTest extends TestCase {

	public final void testLoadDocument() {
		XmlModulelDefinitionDocumentLoader builder = new XmlModulelDefinitionDocumentLoader();
		Document document = builder.loadDocument(new ClassPathResource("xmlspec/moduledefinition.xml"));
		assertNotNull(document);
	}
	
	public final void testNotPresentLoadDocument() {
		XmlModulelDefinitionDocumentLoader builder = new XmlModulelDefinitionDocumentLoader();
		try {
			builder.loadDocument(new ClassPathResource("xmlspec/notpresent.xml"));
			fail();
		}
		catch (ConfigurationException e) {
			assertEquals("Could not load plugin specification, as unable to obtain input stream for resource class path resource [xmlspec/notpresent.xml]", e.getMessage());
		}
	}
	
	public final void testBadlyFormedLoadDocument() {
		XmlModulelDefinitionDocumentLoader builder = new XmlModulelDefinitionDocumentLoader();
		try {
			builder.loadDocument(new ClassPathResource("xmlspec/badlyformedspec.xml"));
			fail();
		}
		catch (ConfigurationException e) {
			assertEquals("Unable to load XML plugin specification document from resource class path resource [xmlspec/badlyformedspec.xml]", e.getMessage());
		}
	}

}
