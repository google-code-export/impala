package org.impalaframework.module.loader;

import java.util.ArrayList;
import java.util.List;

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
		Resource[] parentClassLocations = getParentClassLocations();
		return new FileSystemModuleClassLoader(ClassUtils.getDefaultClassLoader(), ResourceUtils.getFiles(parentClassLocations));
	}

	public Resource[] getClassLocations(ModuleDefinition moduleDefinition) {
		return getParentClassLocations();
	}

	@Override
	public XmlBeanDefinitionReader newBeanDefinitionReader(ConfigurableApplicationContext context, ModuleDefinition definition) {
		// FIXME return tweaked version of BeanDefinitionReader which
		// will not add bean definitions if the applicationContext is not active
		return super.newBeanDefinitionReader(context, definition);
	}

	Resource[] getParentClassLocations() {
		String parentProject = moduleLocationResolver.getRootProjects();
		
		List<Resource> allLocations = new ArrayList<Resource>();
		
		String[] parentProjects = parentProject.split(",");
		for (String rootProjectName : parentProjects) {
			List<Resource> locations = moduleLocationResolver.getApplicationModuleClassLocations(rootProjectName.trim());
			allLocations.addAll(locations);
		}
		
		return allLocations.toArray(new Resource[allLocations.size()]);
	}

	public Resource[] getSpringConfigResources(ModuleDefinition moduleDefinition, ClassLoader classLoader) {
		return ResourceUtils.getClassPathResources(moduleDefinition.getContextLocations(), classLoader);
	}

}
