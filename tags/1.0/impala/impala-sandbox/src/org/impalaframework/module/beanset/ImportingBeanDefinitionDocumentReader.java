/*
 * Copyright 2007-2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.module.beanset;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.util.SystemPropertyUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Overrides <code>DefaultBeanDefinitionDocumentReader</code> to implement
 * smart importing using the <code>beanset:</code> prefix
 * @author Phil Zoio
 */
public class ImportingBeanDefinitionDocumentReader extends DefaultBeanDefinitionDocumentReader {

	static String BEANSET_PREFIX = "beanset:";

	private Set<String> importedResources;

	private Set<String> namedResources;

	Properties beanSetMappings;

	private static final Log logger = LogFactory.getLog(ImportingBeanDefinitionDocumentReader.class);

	public ImportingBeanDefinitionDocumentReader(Properties beanSetMappings) {
		super();
		this.importedResources = new HashSet<String>();
		this.namedResources = new HashSet<String>();
		this.beanSetMappings = beanSetMappings;
	}

	@Override
	public void registerBeanDefinitions(Document doc, XmlReaderContext readerContext) {
		String filename = readerContext.getResource().getFilename();
		if (logger.isInfoEnabled())
			logger.info("Registering definitions for " + filename);

		if (!importedResources.contains(filename))
			namedResources.add(filename);
		doRegisterBeanDefinitions(filename, doc, readerContext);
	}

	@Override
	protected void importBeanDefinitionResource(Element element) {
		String location = element.getAttribute(RESOURCE_ATTRIBUTE);
		location = SystemPropertyUtils.resolvePlaceholders(location);

		if (logger.isDebugEnabled())
			logger.debug("Handling import definition location ... " + location);

		String beanSetName = null;

		if (location.startsWith(BEANSET_PREFIX)) {
			beanSetName = location.substring(BEANSET_PREFIX.length());

			if (logger.isDebugEnabled())
				logger.debug("Beanset key ... " + beanSetName);

			String beanSetLocation = beanSetMappings.getProperty(beanSetName);

			if (beanSetLocation == null) {
				throw new FatalBeanException("Unable to find methodName for beanset key " + beanSetName);
			}
			location = beanSetLocation;
			element.setAttribute(RESOURCE_ATTRIBUTE, beanSetLocation);
		}

		boolean ignore = true;

		boolean alreadyImported = importedResources.contains(location);
		boolean alreadyLoaded = false;

		if (alreadyImported)
			notifyAlreadyImported(location);

		if (!alreadyImported)
			alreadyLoaded = namedResources.contains(location);

		if (alreadyLoaded)
			notifyAlreadyLoaded(location);

		ignore = alreadyImported || alreadyLoaded;

		if (!ignore) {
			if (logger.isInfoEnabled())
				logger.info("Importing resource " +  location);

			importedResources.add(location);
			doImportBeanDefinitionResource(location, beanSetName, element);
		}
	}

	void notifyAlreadyLoaded(String location) {
		if (logger.isInfoEnabled()) {
			logger.info("Ignoring resource loaded explicitly: " +  location);
		}
	}

	void notifyAlreadyImported(String location) {
		if (logger.isInfoEnabled()) {
			logger.info("Ignoring resource already imported: " +  location);
		}
	}

	protected void doImportBeanDefinitionResource(String filename, String beansetLocation, Element ele) {
		super.importBeanDefinitionResource(ele);
	}

	protected void doRegisterBeanDefinitions(String fileName, Document doc, XmlReaderContext readerContext) {
		super.registerBeanDefinitions(doc, readerContext);
	}
}
