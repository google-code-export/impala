package org.impalaframework.module.bootstrap;

import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.loader.ApplicationModuleLoader;
import org.impalaframework.module.loader.BeansetApplicationModuleLoader;
import org.impalaframework.module.loader.ManualReloadingRootModuleLoader;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.module.loader.SystemRootModuleLoader;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class ModuleLoaderRegistryFactoryBean implements FactoryBean, InitializingBean {

	private boolean reloadableParent;

	private ModuleLocationResolver moduleLocationResolver;

	private ModuleLoaderRegistry registry;

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(moduleLocationResolver, "moduleLocationResolver cannot be null");

		registry = new ModuleLoaderRegistry();
		if (reloadableParent)
			registry.setPluginLoader(ModuleTypes.ROOT, new ManualReloadingRootModuleLoader(moduleLocationResolver));
		else
			registry.setPluginLoader(ModuleTypes.ROOT, new SystemRootModuleLoader(moduleLocationResolver));

		registry.setPluginLoader(ModuleTypes.APPLICATION, new ApplicationModuleLoader(moduleLocationResolver));
		registry.setPluginLoader(ModuleTypes.APPLICATION_WITH_BEANSETS, new BeansetApplicationModuleLoader(
				moduleLocationResolver));
	}

	public Object getObject() throws Exception {
		return registry;
	}

	public Class getObjectType() {
		return ModuleLoaderRegistry.class;
	}

	public boolean isSingleton() {
		return true;
	}

	/* ****************** injected setters **************** */

	public void setClassLocationResolver(ModuleLocationResolver moduleLocationResolver) {
		this.moduleLocationResolver = moduleLocationResolver;
	}

	public void setReloadableParent(boolean reloadableParent) {
		this.reloadableParent = reloadableParent;
	}

}
