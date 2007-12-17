package org.impalaframework.module.bootstrap;

import junit.framework.TestCase;

import org.impalaframework.module.bootstrap.ModuleLoaderRegistryFactoryBean;
import org.impalaframework.module.loader.ApplicationPluginLoader;
import org.impalaframework.module.loader.BeansetApplicationPluginLoader;
import org.impalaframework.module.loader.ManualReloadingParentPluginLoader;
import org.impalaframework.module.loader.PluginLoaderRegistry;
import org.impalaframework.module.loader.SystemParentPluginLoader;
import org.impalaframework.module.spec.PluginTypes;
import org.impalaframework.resolver.PropertyClassLocationResolver;

public class ModuleLoaderRegistryFactoryBeanTest extends TestCase {

	private PropertyClassLocationResolver resolver;

	private ModuleLoaderRegistryFactoryBean factoryBean;

	public void setUp() {
		resolver = new PropertyClassLocationResolver();
		factoryBean = new ModuleLoaderRegistryFactoryBean();
	}

	public final void testMethods() throws Exception {
		assertEquals(true, factoryBean.isSingleton());
		assertEquals(PluginLoaderRegistry.class, factoryBean.getObjectType());
	}
	public final void testNoResolver() throws Exception {
		try {
			factoryBean.afterPropertiesSet();
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("classLocationResolver cannot be null", e.getMessage());
		}
	}

	public final void testAfterPropertiesSet() throws Exception {
		factoryBean.setClassLocationResolver(resolver);
		factoryBean.afterPropertiesSet();
		
		PluginLoaderRegistry registry = (PluginLoaderRegistry) factoryBean.getObject();
		assertEquals(SystemParentPluginLoader.class, registry.getPluginLoader(PluginTypes.ROOT).getClass());
		assertEquals(ApplicationPluginLoader.class, registry.getPluginLoader(PluginTypes.APPLICATION).getClass());
		assertEquals(BeansetApplicationPluginLoader.class, registry.getPluginLoader(PluginTypes.APPLICATION_WITH_BEANSETS).getClass());
	}
	
	public final void testWithReloadableParent() throws Exception {
		factoryBean.setClassLocationResolver(resolver);
		factoryBean.setReloadableParent(true);
		factoryBean.afterPropertiesSet();
		
		PluginLoaderRegistry registry = (PluginLoaderRegistry) factoryBean.getObject();
		assertEquals(ManualReloadingParentPluginLoader.class, registry.getPluginLoader(PluginTypes.ROOT).getClass());
		assertEquals(ApplicationPluginLoader.class, registry.getPluginLoader(PluginTypes.APPLICATION).getClass());
		assertEquals(BeansetApplicationPluginLoader.class, registry.getPluginLoader(PluginTypes.APPLICATION_WITH_BEANSETS).getClass());
	}

}
