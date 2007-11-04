package org.impalaframework.spring.web;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.easymock.classextension.EasyMock;
import org.impalaframework.location.PropertyClassLocationResolver;
import org.impalaframework.spring.plugin.ApplicationPluginLoader;
import org.impalaframework.spring.plugin.BeansetApplicationPluginLoader;
import org.impalaframework.spring.plugin.PluginLoaderRegistry;
import org.impalaframework.spring.plugin.PluginTypes;
import org.impalaframework.spring.plugin.WebPluginTypes;
import org.impalaframework.spring.web.RegistryBasedImpalaContextLoader;
import org.impalaframework.spring.web.WebParentPluginLoader;
import org.impalaframework.spring.web.WebPluginLoader;

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
