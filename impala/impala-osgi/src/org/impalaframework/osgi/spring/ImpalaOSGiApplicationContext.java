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

package org.impalaframework.osgi.spring;

import java.io.IOException;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.osgi.context.support.OsgiBundleXmlApplicationContext;

public class ImpalaOSGiApplicationContext extends OsgiBundleXmlApplicationContext {
	
	private Resource[] resources;

	public ImpalaOSGiApplicationContext() {
		super();
	}	
	
	public ImpalaOSGiApplicationContext(ApplicationContext parent) {
		super(parent);
	}

	protected void loadBeanDefinitions(XmlBeanDefinitionReader reader)
			throws BeansException, IOException {
		
		//FIXME assert resources not null
		
		for (int i = 0; i < resources.length; i++) {
			reader.loadBeanDefinitions(resources);
		}
	}

	public void setConfigResources(Resource[] resources) {
		this.resources = resources;
	}
	
}

