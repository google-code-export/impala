package org.impalaframework.bean;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.Resource;

/**
 * 
 * @author Phil Zoio
 */
public class OptionalPropertiesFactoryBean extends PropertiesFactoryBean {

	//FIXME move to package containing other factory beans
	@Override
	public void setLocation(Resource location) {
		if (location.exists())
			super.setLocations(new Resource[]{location});
	}

	@Override
	public void setLocations(Resource[] locations) {
		List<Resource> resources = new ArrayList<Resource>();
		for (int i = 0; i < locations.length; i++) {
			final Resource resource = locations[i];
			if (resource.exists()) {
				resources.add(resource);
			} else {
				//FIXME log only at info level
			}
		}
		Resource[] existingArray = new Resource[resources.size()];
		resources.toArray(existingArray);
		super.setLocations(existingArray);
	}
}
