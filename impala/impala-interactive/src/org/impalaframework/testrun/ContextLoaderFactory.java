package org.impalaframework.testrun;

import org.impalaframework.plugin.beanset.BeansetApplicationPluginLoader;
import org.impalaframework.plugin.loader.ApplicationPluginLoader;
import org.impalaframework.plugin.loader.ManualReloadingParentPluginLoader;
import org.impalaframework.plugin.loader.PluginLoaderRegistry;
import org.impalaframework.plugin.loader.SystemParentPluginLoader;
import org.impalaframework.plugin.monitor.ScheduledPluginMonitor;
import org.impalaframework.plugin.plugin.PluginTypes;
import org.impalaframework.plugin.util.ApplicationContextLoader;
import org.impalaframework.plugin.util.RegistryBasedApplicationContextLoader;
import org.impalaframework.resolver.ClassLocationResolver;

public class ContextLoaderFactory {

	public ApplicationContextLoader newContextLoader(ClassLocationResolver classLocationResolver, boolean autoreload,
			boolean reloadableParent) {

		PluginLoaderRegistry registry = new PluginLoaderRegistry();

		if (reloadableParent)
			registry.setPluginLoader(PluginTypes.ROOT, new ManualReloadingParentPluginLoader(classLocationResolver));
		else
			registry.setPluginLoader(PluginTypes.ROOT, new SystemParentPluginLoader(classLocationResolver));

		registry.setPluginLoader(PluginTypes.APPLICATION, new ApplicationPluginLoader(classLocationResolver));
		registry.setPluginLoader(PluginTypes.APPLICATION_WITH_BEANSETS, new BeansetApplicationPluginLoader(
				classLocationResolver));

		final RegistryBasedApplicationContextLoader loader = new RegistryBasedApplicationContextLoader(registry);

		if (autoreload) {
			ScheduledPluginMonitor monitor = new ScheduledPluginMonitor();
			monitor.addModificationListener(new DynamicPluginModificationListener());
			loader.setPluginMonitor(monitor);
		}
		return loader;
	}
}
