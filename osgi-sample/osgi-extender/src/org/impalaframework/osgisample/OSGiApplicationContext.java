package org.impalaframework.osgisample;

import java.io.IOException;
import java.net.URL;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.UrlResource;
import org.springframework.osgi.context.support.OsgiBundleXmlApplicationContext;

public class OSGiApplicationContext extends OsgiBundleXmlApplicationContext {

	private URL[] resources;

	public OSGiApplicationContext(URL[] resources) {
		super();
		this.resources = resources;
	}

	protected void loadBeanDefinitions(XmlBeanDefinitionReader reader)
			throws BeansException, IOException {
		for (int i = 0; i < resources.length; i++) {
			reader.loadBeanDefinitions(new UrlResource(resources[i]));
		}
	}
}
