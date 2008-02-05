package org.impalaframework.module.loader;

import java.util.ArrayList;
import java.util.List;

import org.impalaframework.exception.ExecutionException;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

/**
 * @author Phil Zoio
 */
public abstract class ModuleUtils {
	
	public static ClassLoader getParentClassLoader(ApplicationContext parent) {
		ClassLoader parentClassLoader = null;
		if (parent != null) {
			parentClassLoader = parent.getClassLoader();
		}
		if (parentClassLoader == null) {
			parentClassLoader = ClassUtils.getDefaultClassLoader();
		}
		return parentClassLoader;
	}

	public static BeanDefinitionRegistry castToBeanDefinitionRegistry(final ConfigurableListableBeanFactory beanFactory) {
		if (!(beanFactory instanceof BeanDefinitionRegistry)) {
			throw new ExecutionException(beanFactory.getClass().getName() + " is not an instance of "
					+ BeanDefinitionRegistry.class.getSimpleName());
		}
	
		BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) beanFactory;
		return beanDefinitionRegistry;
	}

	public static Resource[] getRootClassLocations(ModuleLocationResolver moduleLocationResolver) {
		//Note that this method supports multiple root projects
		List<String> rootProjects = moduleLocationResolver.getRootProjects();
		
		List<Resource> allLocations = new ArrayList<Resource>(rootProjects.size());
		
		for (String rootProjectName : rootProjects) {
			List<Resource> locations = moduleLocationResolver.getApplicationModuleClassLocations(rootProjectName);
			allLocations.addAll(locations);
		}
		
		return allLocations.toArray(new Resource[allLocations.size()]);
	}


}
