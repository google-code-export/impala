package net.java.impala.spring.web;

import javax.servlet.ServletContext;

import junit.framework.TestCase;
import net.java.impala.location.PropertyClassLocationResolver;
import net.java.impala.spring.plugin.ApplicationPluginLoader;
import net.java.impala.spring.plugin.PluginLoaderRegistry;
import net.java.impala.spring.plugin.PluginTypes;
import net.java.impala.spring.plugin.WebPluginTypes;
import net.java.impala.spring.plugin.XmlBeansetApplicationPluginLoader;

import org.easymock.classextension.EasyMock;

public class RegistryBasedImpalaContextLoaderTest extends TestCase {

	public final void testNewRegistry() {
		RegistryBasedImpalaContextLoader loader = new RegistryBasedImpalaContextLoader();
		PluginLoaderRegistry registry = loader.newRegistry(EasyMock.createMock(ServletContext.class),
				new PropertyClassLocationResolver());

		assertTrue(registry.getPluginLoader(PluginTypes.ROOT) instanceof WebParentPluginLoader);
		assertTrue(registry.getPluginLoader(PluginTypes.APPLICATION) instanceof ApplicationPluginLoader);
		assertTrue(registry.getPluginLoader(PluginTypes.APPLICATION_WITH_BEANSETS) instanceof XmlBeansetApplicationPluginLoader);
		assertTrue(registry.getPluginLoader(WebPluginTypes.SERVLET) instanceof WebPluginLoader);
	}

}
