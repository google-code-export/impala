package org.impalaframework.module.transition;

import java.util.ArrayList;
import java.util.List;

import org.impalaframework.module.loader.PluginLoader;
import org.impalaframework.module.loader.PluginLoaderRegistry;
import org.impalaframework.module.spec.RootModuleDefinition;
import org.impalaframework.module.spec.ModuleDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

public class AddLocationsTransitionProcessor implements TransitionProcessor {

	private PluginLoaderRegistry pluginLoaderRegistry;

	public AddLocationsTransitionProcessor(PluginLoaderRegistry pluginLoaderRegistry) {
		super();
		this.pluginLoaderRegistry = pluginLoaderRegistry;
	}

	public boolean process(PluginStateManager pluginStateManager, RootModuleDefinition existingSpec, RootModuleDefinition newSpec,
			ModuleDefinition plugin) {

		PluginLoader pluginLoader = pluginLoaderRegistry.getPluginLoader(newSpec.getType());
		ConfigurableApplicationContext parentContext = pluginStateManager.getParentContext();

		ClassLoader classLoader = parentContext.getClassLoader();

		Resource[] existingResources = pluginLoader.getSpringConfigResources(existingSpec, classLoader);
		Resource[] newResources = pluginLoader.getSpringConfigResources(newSpec, classLoader);

		// compare difference
		List<Resource> existingResourceList = newResourceList(existingResources);
		List<Resource> newResourceList = newResourceList(newResources);
		List<Resource> toAddList = new ArrayList<Resource>();

		for (Resource resource : newResourceList) {
			if (!existingResourceList.contains(resource)) {
				toAddList.add(resource);
			}
		}

		BeanDefinitionReader beanDefinitionReader = pluginLoader.newBeanDefinitionReader(parentContext, newSpec);
		beanDefinitionReader.loadBeanDefinitions(toAddList.toArray(new Resource[toAddList.size()]));
		
		return true;
	}

	private List<Resource> newResourceList(Resource[] array) {
		List<Resource> list = new ArrayList<Resource>();
		for (Resource resource : array) {
			list.add(resource);
		}
		return list;
	}

}
