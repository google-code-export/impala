package org.impalaframework.module.bootstrap;

import junit.framework.TestCase;

import org.impalaframework.module.bootstrap.ModuleLoaderRegistryFactoryBean;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.loader.ApplicationModuleLoader;
import org.impalaframework.module.loader.BeansetApplicationModuleLoader;
import org.impalaframework.module.loader.ManualReloadingRootModuleLoader;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.module.loader.SystemRootModuleLoader;
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
		assertEquals(ModuleLoaderRegistry.class, factoryBean.getObjectType());
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
		
		ModuleLoaderRegistry registry = (ModuleLoaderRegistry) factoryBean.getObject();
		assertEquals(SystemRootModuleLoader.class, registry.getPluginLoader(ModuleTypes.ROOT).getClass());
		assertEquals(ApplicationModuleLoader.class, registry.getPluginLoader(ModuleTypes.APPLICATION).getClass());
		assertEquals(BeansetApplicationModuleLoader.class, registry.getPluginLoader(ModuleTypes.APPLICATION_WITH_BEANSETS).getClass());
	}
	
	public final void testWithReloadableParent() throws Exception {
		factoryBean.setClassLocationResolver(resolver);
		factoryBean.setReloadableParent(true);
		factoryBean.afterPropertiesSet();
		
		ModuleLoaderRegistry registry = (ModuleLoaderRegistry) factoryBean.getObject();
		assertEquals(ManualReloadingRootModuleLoader.class, registry.getPluginLoader(ModuleTypes.ROOT).getClass());
		assertEquals(ApplicationModuleLoader.class, registry.getPluginLoader(ModuleTypes.APPLICATION).getClass());
		assertEquals(BeansetApplicationModuleLoader.class, registry.getPluginLoader(ModuleTypes.APPLICATION_WITH_BEANSETS).getClass());
	}

}
