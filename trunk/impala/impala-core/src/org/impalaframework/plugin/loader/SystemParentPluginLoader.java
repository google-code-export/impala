package org.impalaframework.plugin.loader;

import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.resolver.ClassLocationResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;


/**
 * @author Phil Zoio
 */
public class SystemParentPluginLoader extends ParentPluginLoader implements PluginLoader {

	public SystemParentPluginLoader(ClassLocationResolver classLocationResolver) {
		super(classLocationResolver);
	}

	@Override
	public ClassLoader newClassLoader(PluginSpec pluginSpec, ApplicationContext parent) {
		return ClassUtils.getDefaultClassLoader();
	}
	
}
