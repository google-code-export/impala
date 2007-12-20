package org.impalaframework.module.builder;

import java.io.IOException;
import java.io.InputStream;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.xml.SimpleSaxErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.xml.DefaultDocumentLoader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class XmlModulelDefinitionDocumentLoader {

	final Logger logger = LoggerFactory.getLogger(XmlModulelDefinitionDocumentLoader.class);

	Document loadDocument(Resource resource) {

		Assert.notNull(resource);

		EncodedResource encodedResource = new EncodedResource(resource, "UTF-8");

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
			document = loader.loadDocument(inputSource, null, new SimpleSaxErrorHandler(logger),
					XmlBeanDefinitionReader.VALIDATION_NONE, false);
		}
		catch (Exception e) {
			throw new ConfigurationException("Unable to load XML plugin specification document from resource "
					+ encodedResource.getResource(), e);
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

}
