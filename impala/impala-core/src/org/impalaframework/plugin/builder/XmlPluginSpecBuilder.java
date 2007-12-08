package org.impalaframework.plugin.builder;

import org.impalaframework.plugin.spec.ParentSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;

public class XmlPluginSpecBuilder implements PluginSpecBuilder {

	final Logger logger = LoggerFactory.getLogger(XmlPluginSpecBuilder.class);

	private Resource resource;

	private XmlSpecDocumentLoader xmlSpecLoader;

	private XmlPluginSpecBuilder() {
		super();
		this.xmlSpecLoader = new XmlSpecDocumentLoader();
	}

	public ParentSpec getParentSpec() {
		Document document = xmlSpecLoader.loadDocument(resource);

		return null;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

}
