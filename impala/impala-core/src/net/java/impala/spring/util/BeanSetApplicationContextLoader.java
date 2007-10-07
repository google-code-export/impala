package net.java.impala.spring.util;

import java.util.Properties;

import net.java.impala.classloader.ContextResourceHelper;
import net.java.impala.spring.beanset.BeanSetImportDelegate;
import net.java.impala.spring.beanset.DebuggingBeanSetDefinitionDocumentReader;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;

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
