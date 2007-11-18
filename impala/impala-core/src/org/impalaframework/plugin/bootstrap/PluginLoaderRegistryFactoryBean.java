package org.impalaframework.plugin.bootstrap;

import org.impalaframework.plugin.loader.ApplicationPluginLoader;
import org.impalaframework.plugin.loader.BeansetApplicationPluginLoader;
import org.impalaframework.plugin.loader.ManualReloadingParentPluginLoader;
import org.impalaframework.plugin.loader.PluginLoaderRegistry;
import org.impalaframework.plugin.loader.SystemParentPluginLoader;
import org.impalaframework.plugin.spec.PluginTypes;
import org.impalaframework.resolver.ClassLocationResolver;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class PluginLoaderRegistryFactoryBean implements FactoryBean, InitializingBean {

	private boolean reloadableParent;

	private ClassLocationResolver classLocationResolver;

	private PluginLoaderRegistry registry;

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(classLocationResolver, "classLocationResolver cannot be null");

		registry = new PluginLoaderRegistry();
		if (reloadableParent)
			registry.setPluginLoader(PluginTypes.ROOT, new ManualReloadingParentPluginLoader(classLocationResolver));
		else
			registry.setPluginLoader(PluginTypes.ROOT, new SystemParentPluginLoader(classLocationResolver));

		registry.setPluginLoader(PluginTypes.APPLICATION, new ApplicationPluginLoader(classLocationResolver));
		registry.setPluginLoader(PluginTypes.APPLICATION_WITH_BEANSETS, new BeansetApplicationPluginLoader(
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
