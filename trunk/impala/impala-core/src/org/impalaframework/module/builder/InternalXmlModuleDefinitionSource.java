/*
 * Copyright 2007-2008 the original author or authors.
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

package org.impalaframework.module.builder;

import java.util.Map;

import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.type.TypeReader;
import org.impalaframework.module.type.TypeReaderRegistryFactory;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class InternalXmlModuleDefinitionSource implements ModuleDefinitionSource {

	private Resource resource;

	private ModuleLocationResolver moduleLocationResolver;
	
	private XmlModulelDefinitionDocumentLoader xmlDefinitionLoader;
	
	private Map<String, TypeReader> typeReaders;

	public InternalXmlModuleDefinitionSource(ModuleLocationResolver moduleLocationResolver) {
		this(moduleLocationResolver, TypeReaderRegistryFactory.getTypeReaders());
	}

	protected InternalXmlModuleDefinitionSource(ModuleLocationResolver moduleLocationResolver, Map<String, TypeReader> typeReaders) {
		super();
		Assert.notNull(moduleLocationResolver, "moduleLocationResolver cannot be null");
		Assert.notNull(typeReaders, "typeReaders cannot be null");
		this.typeReaders = typeReaders;
		this.xmlDefinitionLoader = new XmlModulelDefinitionDocumentLoader();
		this.moduleLocationResolver = moduleLocationResolver;
	}

	public RootModuleDefinition getModuleDefinition() {
		Document document = xmlDefinitionLoader.loadDocument(resource);

		Element root = document.getDocumentElement();
		Element namesElement = DomUtils.getChildElementByTagName(root, ModuleElementNames.NAMES_ELEMENT);
		
		if (namesElement == null) {
			//FIXME 
			throw new RuntimeException();
		}
		
		String value = namesElement.getTextContent();
		String[] moduleNames = StringUtils.tokenizeToStringArray(value, " ,\n\r", true, true);
		
		InternalModuleDefinitionSource internalModuleSource = new InternalModuleDefinitionSource(moduleLocationResolver, moduleNames);
		//FIXME now need to tweak properties with XML variants
		
		return internalModuleSource.getModuleDefinition();
	}
	
	public void setResource(Resource resource) {
		this.resource = resource;
	}

}
