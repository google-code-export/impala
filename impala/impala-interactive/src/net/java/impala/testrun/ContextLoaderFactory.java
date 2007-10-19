package net.java.impala.testrun;

import net.java.impala.location.ClassLocationResolver;
import net.java.impala.spring.monitor.ScheduledPluginMonitor;
import net.java.impala.spring.plugin.ApplicationPluginLoader;
import net.java.impala.spring.plugin.ParentPluginLoader;
import net.java.impala.spring.plugin.PluginLoaderRegistry;
import net.java.impala.spring.plugin.PluginTypes;
import net.java.impala.spring.plugin.SystemParentPluginLoader;
import net.java.impala.spring.util.ApplicationContextLoader;
import net.java.impala.spring.util.RegistryBasedApplicationContextLoader;

public class ContextLoaderFactory {
	
	public ApplicationContextLoader newContextLoader(ClassLocationResolver classLocationResolver, boolean autoreload,
			boolean reloadableParent) {

		PluginLoaderRegistry registry = new PluginLoaderRegistry();

		if (reloadableParent)
			registry.setPluginLoader(PluginTypes.ROOT, new ParentPluginLoader(classLocationResolver));
		else
			registry.setPluginLoader(PluginTypes.ROOT, new SystemParentPluginLoader(classLocationResolver));

		registry.setPluginLoader(PluginTypes.APPLICATION, new ApplicationPluginLoader(classLocationResolver));

		final RegistryBasedApplicationContextLoader loader = new RegistryBasedApplicationContextLoader(registry);

		if (autoreload) {
			ScheduledPluginMonitor monitor = new ScheduledPluginMonitor();
			monitor.addModificationListener(new DynamicPluginModificationListener());
			loader.setPluginMonitor(monitor);
		}
		return loader;
	}
}
