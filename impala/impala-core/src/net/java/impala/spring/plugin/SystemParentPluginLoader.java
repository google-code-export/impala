package net.java.impala.spring.plugin;

import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;

import net.java.impala.location.ClassLocationResolver;

/**
 * @author Phil Zoio
 */
public class SystemParentPluginLoader extends ParentPluginLoader implements PluginLoader {

	public SystemParentPluginLoader(ClassLocationResolver classLocationResolver) {
		super(classLocationResolver);
	}

	@Override
	public ClassLoader newClassLoader(ApplicationContextSet contextSet, PluginSpec pluginSpec, ApplicationContext parent) {
		return ClassUtils.getDefaultClassLoader();
	}
	
}
