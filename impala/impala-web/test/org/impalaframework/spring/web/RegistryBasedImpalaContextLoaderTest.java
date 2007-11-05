package org.impalaframework.spring.web;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.easymock.classextension.EasyMock;
import org.impalaframework.plugin.loader.ApplicationPluginLoader;
import org.impalaframework.plugin.loader.BeansetApplicationPluginLoader;
import org.impalaframework.plugin.loader.PluginLoaderRegistry;
import org.impalaframework.plugin.spec.PluginTypes;
import org.impalaframework.plugin.web.WebPluginTypes;
import org.impalaframework.resolver.PropertyClassLocationResolver;
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
