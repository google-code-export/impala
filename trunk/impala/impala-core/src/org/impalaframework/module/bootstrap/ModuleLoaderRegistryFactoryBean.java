package org.impalaframework.module.bootstrap;

import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.loader.ApplicationModuleLoader;
import org.impalaframework.module.loader.BeansetApplicationModuleLoader;
import org.impalaframework.module.loader.ManualReloadingRootModuleLoader;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.module.loader.SystemRootModuleLoader;
import org.impalaframework.resolver.ClassLocationResolver;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class ModuleLoaderRegistryFactoryBean implements FactoryBean, InitializingBean {

	private boolean reloadableParent;

	private ClassLocationResolver classLocationResolver;

	private ModuleLoaderRegistry registry;

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(classLocationResolver, "classLocationResolver cannot be null");

		registry = new ModuleLoaderRegistry();
		if (reloadableParent)
			registry.setPluginLoader(ModuleTypes.ROOT, new ManualReloadingRootModuleLoader(classLocationResolver));
		else
			registry.setPluginLoader(ModuleTypes.ROOT, new SystemRootModuleLoader(classLocationResolver));

		registry.setPluginLoader(ModuleTypes.APPLICATION, new ApplicationModuleLoader(classLocationResolver));
		registry.setPluginLoader(ModuleTypes.APPLICATION_WITH_BEANSETS, new BeansetApplicationModuleLoader(
				classLocationResolver));
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

	public void setClassLocationResolver(ClassLocationResolver classLocationResolver) {
		this.classLocationResolver = classLocationResolver;
	}

	public void setReloadableParent(boolean reloadableParent) {
		this.reloadableParent = reloadableParent;
	}

}
