package org.impalaframework.osgisample;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.osgi.io.OsgiBundleResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.support.GenericWebApplicationContext;

public class ImpalaActivator implements BundleActivator {

	public void start(BundleContext bundleContext) throws Exception {
		URL resource = bundleContext.getBundle().getResource("META-INF/impala-bootstrap.xml");
		
		if (resource != null) {
			InputStream stream = resource.openStream();
			FileCopyUtils.copyToString(new InputStreamReader(stream));
			System.out.println(stream);
			
			String name = "";
			
			final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
			final GenericWebApplicationContext applicationContext = new GenericWebApplicationContext(beanFactory);
			//applicationContext.setServletContext(servletContext);

			XmlBeanDefinitionReader definitionReader = new XmlBeanDefinitionReader(beanFactory);
			definitionReader.loadBeanDefinitions(new OsgiBundleResource(bundleContext.getBundle(), "META-INF/impala-bootstrap.xml"));
			applicationContext.refresh();
		}
	}



	public void stop(BundleContext bundleContext) throws Exception {
	}
}

