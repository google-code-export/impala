package org.impalaframework.plugin.plugin;


import org.impalaframework.resolver.ClassLocationResolver;
import org.springframework.core.io.Resource;

/**
 * @author Phil Zoio
 */
public class ManualReloadingParentPluginLoader extends ParentPluginLoader implements PluginLoader {

	public ManualReloadingParentPluginLoader(ClassLocationResolver classLocationResolver) {
		super(classLocationResolver);
	}

	@Override
	public Resource[] getClassLocations(ApplicationContextSet contextSet, PluginSpec pluginSpec) {
		return new Resource[0];
	}

}
