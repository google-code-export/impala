package org.impalaframework.plugin.transition;

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

	public boolean process(PluginStateManager pluginStateManager, ParentSpec existingSpec, ParentSpec newSpec,
			PluginSpec plugin) {

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

		//FIXME should only load new definitions
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
