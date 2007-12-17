package org.impalaframework.module.loader;


import org.impalaframework.module.spec.PluginSpec;
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
	public Resource[] getClassLocations(PluginSpec pluginSpec) {
		return new Resource[0];
	}

}
