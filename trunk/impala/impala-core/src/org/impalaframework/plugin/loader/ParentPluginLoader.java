package org.impalaframework.plugin.loader;

import java.io.File;

import org.impalaframework.classloader.FileSystemPluginClassLoader;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.resolver.ClassLocationResolver;
import org.impalaframework.util.ResourceUtils;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * @author Phil Zoio
 */
public class ParentPluginLoader extends BasePluginLoader implements PluginLoader {

	private ClassLocationResolver classLocationResolver;

	public ParentPluginLoader(ClassLocationResolver classLocationResolver) {
		super();
		Assert.notNull("classLocationResolver cannot be null");
		this.classLocationResolver = classLocationResolver;
	}

	public ClassLoader newClassLoader(PluginSpec pluginSpec, ApplicationContext parent) {
		File[] parentClassLocations = getParentClassLocations();
		return new FileSystemPluginClassLoader(ClassUtils.getDefaultClassLoader(), parentClassLocations);
	}

	public Resource[] getClassLocations(PluginSpec pluginSpec) {
		return ResourceUtils.getResources(getParentClassLocations());
	}

	@Override
	public XmlBeanDefinitionReader newBeanDefinitionReader(ConfigurableApplicationContext context, PluginSpec plugin) {
		// FIXME return tweaked version of BeanDefinitionReader which
		// will not add bean definitions if the applicationContext is not active
		return super.newBeanDefinitionReader(context, plugin);
	}

	private File[] getParentClassLocations() {
		String parentProject = classLocationResolver.getParentProject();
		File[] parentClassLocations = classLocationResolver.getApplicationPluginClassLocations(parentProject);
		return parentClassLocations;
	}

	public Resource[] getSpringConfigResources(PluginSpec pluginSpec, ClassLoader classLoader) {
		return ResourceUtils.getClassPathResources(pluginSpec.getContextLocations(), classLoader);
	}

}
