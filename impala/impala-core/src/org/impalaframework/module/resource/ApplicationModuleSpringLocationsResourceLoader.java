package org.impalaframework.module.resource;

import java.util.ArrayList;
import java.util.List;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.spring.resource.ResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class ApplicationModuleSpringLocationsResourceLoader implements SpringLocationsResourceLoader {

	private ResourceLoader resourceLoader;

	public ApplicationModuleSpringLocationsResourceLoader() {
		super();
	}

	public Resource[] getSpringLocations(ModuleDefinition moduleDefinition, ClassLoader classLoader) {
		Assert.notNull(moduleDefinition);
		Assert.notNull(resourceLoader);

		List<String> contextLocations = moduleDefinition.getContextLocations();

		List<Resource> resourceList = new ArrayList<Resource>();
		for (String location : contextLocations) {
			Resource resource = resourceLoader.getResource(location, classLoader);
			checkResource(resource, location, moduleDefinition);

			if (resource != null) {
				resourceList.add(resource);
			}
		}
		Resource[] resources = new Resource[resourceList.size()];
		resources = resourceList.toArray(resources);

		return resources;
	}

	protected void checkResource(Resource resource, String location, ModuleDefinition moduleDefinition) {
		// FIXME test
		if (resource == null)
			throw new ConfigurationException("Unable to load resource from location " + location
					+ " for module definition " + moduleDefinition.getName());
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

}
