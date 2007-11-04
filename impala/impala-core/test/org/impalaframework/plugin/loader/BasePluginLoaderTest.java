package org.impalaframework.plugin.loader;

import org.impalaframework.plugin.loader.ApplicationPluginLoader;
import org.impalaframework.plugin.loader.BasePluginLoader;
import org.impalaframework.plugin.spec.SimplePluginSpec;
import org.impalaframework.resolver.PropertyClassLocationResolver;
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
