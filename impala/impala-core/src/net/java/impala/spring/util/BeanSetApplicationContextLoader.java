/*
 * Copyright 2007 the original author or authors.
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

package net.java.impala.spring.util;

import java.util.Properties;

import net.java.impala.classloader.ContextResourceHelper;
import net.java.impala.spring.beanset.BeanSetImportDelegate;
import net.java.impala.spring.beanset.DebuggingBeanSetDefinitionDocumentReader;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;

@Deprecated
public class BeanSetApplicationContextLoader extends DefaultApplicationContextLoader {

	public BeanSetApplicationContextLoader(ContextResourceHelper resourceHelper) {
		super(resourceHelper);
	}

	@Override
	protected XmlBeanDefinitionReader newBeanDefinitionReader(GenericApplicationContext context) {
		BeanSetImportDelegate delegate = new BeanSetImportDelegate(new Properties());

		XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(context);
		delegate.initBeanDefinitionReader(xmlReader);
		
		xmlReader.setDocumentReaderClass(DebuggingBeanSetDefinitionDocumentReader.class);

		delegate.beforeRefresh(context);
		return xmlReader;
	}
}
