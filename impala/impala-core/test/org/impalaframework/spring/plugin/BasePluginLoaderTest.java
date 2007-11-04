package org.impalaframework.spring.plugin;

import org.impalaframework.resolver.PropertyClassLocationResolver;
import org.impalaframework.spring.plugin.ApplicationPluginLoader;
import org.impalaframework.spring.plugin.BasePluginLoader;
import org.impalaframework.spring.plugin.SimplePluginSpec;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;

import junit.framework.TestCase;

public class BasePluginLoaderTest extends TestCase {
	public void testNewBeanDefinitionReader() throws Exception {
		BasePluginLoader loader = new ApplicationPluginLoader(new PropertyClassLocationResolver());
		GenericApplicationContext context = new GenericApplicationContext();
		XmlBeanDefinitionReader reader = loader.newBeanDefinitionReader(context, new SimplePluginSpec("pluginName"));
		assertSame(context.getBeanFactory(), reader.getBeanFactory());
	}
}
