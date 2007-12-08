package org.impalaframework.plugin.builder;

import java.io.IOException;
import java.io.InputStream;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.plugin.spec.ParentSpec;
import org.springframework.beans.factory.xml.DefaultDocumentLoader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class XmlPluginSpecBuilder implements PluginSpecBuilder {

	private Resource resource;

	public ParentSpec getParentSpec() {
		Document document = loadDocument();

		return null;
	}

	Document loadDocument() {

		Assert.notNull(resource);

		EncodedResource encodedResource = new EncodedResource(resource, "UTF8");

		InputStream inputStream = null;
		try {
			// this will throw an IOException if the stream cannot be opened
			inputStream = encodedResource.getResource().getInputStream();
		}
		catch (IOException e) {
			throw new ConfigurationException(
					"Could not load plugin specification, as unable to obtain input stream for resource "
							+ encodedResource.getResource(), e);
		}

		Document document = null;

		DefaultDocumentLoader loader = new DefaultDocumentLoader();
		try {
			InputSource inputSource = new InputSource(inputStream);
			inputSource.setEncoding(encodedResource.getEncoding());
			document = loader.loadDocument(inputSource, null, null, XmlBeanDefinitionReader.VALIDATION_NONE, false);
		}
		catch (Exception e) {
			throw new ConfigurationException("Unable to load XML plugin specification document from resource " + encodedResource.getResource(), e);
		}
		finally {
			try {
				if (inputStream != null)
					inputStream.close();
			}
			catch (IOException e) {
			}
		}
		return document;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

}
