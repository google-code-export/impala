package org.impalaframework.plugin.spec.transition;

import java.util.ArrayList;
import java.util.List;

import org.impalaframework.plugin.loader.PluginLoader;
import org.impalaframework.plugin.loader.PluginLoaderRegistry;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

public class AddLocationsTransitionProcessor implements TransitionProcessor {

	private PluginLoaderRegistry pluginLoaderRegistry;

	public AddLocationsTransitionProcessor(PluginLoaderRegistry pluginLoaderRegistry) {
		super();
		this.pluginLoaderRegistry = pluginLoaderRegistry;
	}

	public void process(PluginStateManager pluginStateManager, ParentSpec existingSpec, ParentSpec newSpec,
			PluginSpec plugin) {

		//FIXME test it
		
		PluginLoader pluginLoader = pluginLoaderRegistry.getPluginLoader(newSpec.getType());
		ConfigurableApplicationContext parentContext = pluginStateManager.getParentContext();

		Resource[] existingResources = pluginLoader.getSpringConfigResources(existingSpec, parentContext
				.getClassLoader());
		Resource[] newResources = pluginLoader.getSpringConfigResources(newSpec, parentContext.getClassLoader());

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
	}

	private List<Resource> newResourceList(Resource[] array) {
		List<Resource> list = new ArrayList<Resource>();
		for (Resource resource : array) {
			list.add(resource);
		}
		return list;
	}

}
