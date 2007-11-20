package org.impalaframework.testrun;

import org.impalaframework.plugin.loader.ApplicationContextLoader;
import org.impalaframework.plugin.loader.ApplicationPluginLoader;
import org.impalaframework.plugin.loader.BeansetApplicationPluginLoader;
import org.impalaframework.plugin.loader.ManualReloadingParentPluginLoader;
import org.impalaframework.plugin.loader.PluginLoaderRegistry;
import org.impalaframework.plugin.loader.RegistryBasedApplicationContextLoader;
import org.impalaframework.plugin.loader.SystemParentPluginLoader;
import org.impalaframework.plugin.monitor.ScheduledPluginMonitor;
import org.impalaframework.plugin.spec.PluginTypes;
import org.impalaframework.resolver.ClassLocationResolver;

public class ContextLoaderFactory {

	public ApplicationContextLoader newContextLoader(ClassLocationResolver classLocationResolver, boolean autoreload,
			boolean reloadableParent) {

		PluginLoaderRegistry registry = getPluginLoaderRegistry(classLocationResolver, reloadableParent);

		final RegistryBasedApplicationContextLoader loader = new RegistryBasedApplicationContextLoader(registry);

		if (autoreload) {
			ScheduledPluginMonitor monitor = new ScheduledPluginMonitor();
			monitor.addModificationListener(new DynamicPluginModificationListener());
			loader.setPluginMonitor(monitor);
		}
		return loader;
	}

	public PluginLoaderRegistry getPluginLoaderRegistry(ClassLocationResolver classLocationResolver, boolean reloadableParent) {
		
		//FIXME test - probably shouldn't work this way
		PluginLoaderRegistry registry = new PluginLoaderRegistry();

		if (reloadableParent) {
			//FIXME figure out what is going on here!
			registry.setPluginLoader(PluginTypes.ROOT, new ManualReloadingParentPluginLoader(classLocationResolver));
		}
		else {
			registry.setPluginLoader(PluginTypes.ROOT, new SystemParentPluginLoader(classLocationResolver));
		}

		registry.setPluginLoader(PluginTypes.APPLICATION, new ApplicationPluginLoader(classLocationResolver));
		registry.setPluginLoader(PluginTypes.APPLICATION_WITH_BEANSETS, new BeansetApplicationPluginLoader(
				classLocationResolver));
		return registry;
	}
}
