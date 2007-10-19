package net.java.impala.spring.plugin;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;

import net.java.impala.location.PropertyClassLocationResolver;
import junit.framework.TestCase;

public class BasePluginLoaderTest extends TestCase {
	public void testNewBeanDefinitionReader() throws Exception {
		BasePluginLoader loader = new ApplicationPluginLoader(new PropertyClassLocationResolver());
		GenericApplicationContext context = new GenericApplicationContext();
		XmlBeanDefinitionReader reader = loader.newBeanDefinitionReader(context);
		assertSame(context, reader.getBeanFactory());
	}
}
