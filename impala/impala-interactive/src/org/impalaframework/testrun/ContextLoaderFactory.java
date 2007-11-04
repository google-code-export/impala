package org.impalaframework.testrun;

import org.impalaframework.resolver.ClassLocationResolver;
import org.impalaframework.spring.beanset.BeansetApplicationPluginLoader;
import org.impalaframework.spring.monitor.ScheduledPluginMonitor;
import org.impalaframework.spring.plugin.ApplicationPluginLoader;
import org.impalaframework.spring.plugin.ManualReloadingParentPluginLoader;
import org.impalaframework.spring.plugin.PluginLoaderRegistry;
import org.impalaframework.spring.plugin.PluginTypes;
import org.impalaframework.spring.plugin.SystemParentPluginLoader;
import org.impalaframework.spring.util.ApplicationContextLoader;
import org.impalaframework.spring.util.RegistryBasedApplicationContextLoader;

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
