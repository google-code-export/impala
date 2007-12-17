package org.impalaframework.module.loader;

import java.io.File;

import org.impalaframework.classloader.FileSystemModuleClassLoader;
import org.impalaframework.module.spec.ModuleDefinition;
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
public class RootModuleLoader extends BaseModuleLoader implements ModuleLoader {

	private ClassLocationResolver classLocationResolver;

	public RootModuleLoader(ClassLocationResolver classLocationResolver) {
		super();
		Assert.notNull("classLocationResolver cannot be null");
		this.classLocationResolver = classLocationResolver;
	}

	public ClassLoader newClassLoader(ModuleDefinition moduleDefinition, ApplicationContext parent) {
		File[] parentClassLocations = getParentClassLocations();
		return new FileSystemModuleClassLoader(ClassUtils.getDefaultClassLoader(), parentClassLocations);
	}

	public Resource[] getClassLocations(ModuleDefinition moduleDefinition) {
		return ResourceUtils.getResources(getParentClassLocations());
	}

	@Override
	public XmlBeanDefinitionReader newBeanDefinitionReader(ConfigurableApplicationContext context, ModuleDefinition plugin) {
		// FIXME return tweaked version of BeanDefinitionReader which
		// will not add bean definitions if the applicationContext is not active
		return super.newBeanDefinitionReader(context, plugin);
	}

	private File[] getParentClassLocations() {
		String parentProject = classLocationResolver.getParentProject();
		File[] parentClassLocations = classLocationResolver.getApplicationPluginClassLocations(parentProject);
		return parentClassLocations;
	}

	public Resource[] getSpringConfigResources(ModuleDefinition moduleDefinition, ClassLoader classLoader) {
		return ResourceUtils.getClassPathResources(moduleDefinition.getContextLocations(), classLoader);
	}

}
