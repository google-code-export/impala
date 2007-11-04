package org.impalaframework.spring.beanset;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionDocumentReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

public class BeansetXmlDefinitionReader extends XmlBeanDefinitionReader {

	public BeansetXmlDefinitionReader(BeanDefinitionRegistry beanFactory) {
		super(beanFactory);
	}

	@Override
	protected BeanDefinitionDocumentReader createBeanDefinitionDocumentReader() {
		return super.createBeanDefinitionDocumentReader();
	}

}
