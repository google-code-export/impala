package net.java.impala.spring.web;

import javax.servlet.ServletContext;

import net.java.impala.location.PropertyClassLocationResolver;
import net.java.impala.spring.plugin.ApplicationPluginLoader;
import net.java.impala.spring.plugin.BeansetApplicationPluginLoader;
import net.java.impala.spring.plugin.PluginLoaderRegistry;
import net.java.impala.spring.plugin.PluginTypes;
import net.java.impala.spring.plugin.WebPluginTypes;

import org.easymock.classextension.EasyMock;

import junit.framework.TestCase;

public class RegistryBasedImpalaContextLoaderTest extends TestCase {

	public final void testNewRegistry() {
		RegistryBasedImpalaContextLoader loader = new RegistryBasedImpalaContextLoader();
		PluginLoaderRegistry registry = loader.newRegistry(EasyMock.createMock(ServletContext.class),
				new PropertyClassLocationResolver());

		assertTrue(registry.getPluginLoader(PluginTypes.ROOT) instanceof WebParentPluginLoader);
		assertTrue(registry.getPluginLoader(PluginTypes.APPLICATION) instanceof ApplicationPluginLoader);
		assertTrue(registry.getPluginLoader(PluginTypes.APPLICATION_WITH_BEANSETS) instanceof BeansetApplicationPluginLoader);
		assertTrue(registry.getPluginLoader(WebPluginTypes.SERVLET) instanceof WebPluginLoader);
	}

}
