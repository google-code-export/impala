package org.impalaframework.module.bootstrap;

import org.impalaframework.module.loader.ApplicationPluginLoader;
import org.impalaframework.module.loader.BeansetApplicationPluginLoader;
import org.impalaframework.module.loader.ManualReloadingParentPluginLoader;
import org.impalaframework.module.loader.PluginLoaderRegistry;
import org.impalaframework.module.loader.SystemParentPluginLoader;
import org.impalaframework.module.spec.ModuleTypes;
import org.impalaframework.resolver.ClassLocationResolver;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class ModuleLoaderRegistryFactoryBean implements FactoryBean, InitializingBean {

	private boolean reloadableParent;

	private ClassLocationResolver classLocationResolver;

	private PluginLoaderRegistry registry;

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(classLocationResolver, "classLocationResolver cannot be null");

		registry = new PluginLoaderRegistry();
		if (reloadableParent)
			registry.setPluginLoader(ModuleTypes.ROOT, new ManualReloadingParentPluginLoader(classLocationResolver));
		else
			registry.setPluginLoader(ModuleTypes.ROOT, new SystemParentPluginLoader(classLocationResolver));

		registry.setPluginLoader(ModuleTypes.APPLICATION, new ApplicationPluginLoader(classLocationResolver));
		registry.setPluginLoader(ModuleTypes.APPLICATION_WITH_BEANSETS, new BeansetApplicationPluginLoader(
				classLocationResolver));
	}

	public Object getObject() throws Exception {
		return registry;
	}

	public Class getObjectType() {
		return PluginLoaderRegistry.class;
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
