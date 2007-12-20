package org.impalaframework.module.loader;

import java.io.File;

import org.impalaframework.classloader.FileSystemModuleClassLoader;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.resolver.ModuleLocationResolver;
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
public class RootModuleLoader extends BaseModuleLoader implements ModuleLoader {

	private ModuleLocationResolver moduleLocationResolver;

	public RootModuleLoader(ModuleLocationResolver moduleLocationResolver) {
		super();
		Assert.notNull("moduleLocationResolver cannot be null");
		this.moduleLocationResolver = moduleLocationResolver;
	}

	public ClassLoader newClassLoader(ModuleDefinition moduleDefinition, ApplicationContext parent) {
		File[] parentClassLocations = getParentClassLocations();
		return new FileSystemModuleClassLoader(ClassUtils.getDefaultClassLoader(), parentClassLocations);
	}

	public Resource[] getClassLocations(ModuleDefinition moduleDefinition) {
		return ResourceUtils.getResources(getParentClassLocations());
	}

	@Override
	public XmlBeanDefinitionReader newBeanDefinitionReader(ConfigurableApplicationContext context, ModuleDefinition definition) {
		// FIXME return tweaked version of BeanDefinitionReader which
		// will not add bean definitions if the applicationContext is not active
		return super.newBeanDefinitionReader(context, definition);
	}

	private File[] getParentClassLocations() {
		String parentProject = moduleLocationResolver.getParentProject();
		File[] parentClassLocations = moduleLocationResolver.getApplicationPluginClassLocations(parentProject);
		return parentClassLocations;
	}

	public Resource[] getSpringConfigResources(ModuleDefinition moduleDefinition, ClassLoader classLoader) {
		return ResourceUtils.getClassPathResources(moduleDefinition.getContextLocations(), classLoader);
	}

}
