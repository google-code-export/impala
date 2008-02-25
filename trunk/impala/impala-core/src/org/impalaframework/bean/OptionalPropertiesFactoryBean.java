package org.impalaframework.bean;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.Resource;

/**
 * 
 * @author Phil Zoio
 */
public class OptionalPropertiesFactoryBean extends PropertiesFactoryBean {

	Logger logger = LoggerFactory.getLogger(OptionalPropertiesFactoryBean.class);

	@Override
	public void setLocation(Resource location) {
		if (location.exists())
			super.setLocations(new Resource[] { location });
	}

	@Override
	public void setLocations(Resource[] locations) {
		List<Resource> resources = new ArrayList<Resource>();
		for (int i = 0; i < locations.length; i++) {
			final Resource resource = locations[i];
			if (resource.exists()) {
				resources.add(resource);
				if (logger.isDebugEnabled())
					logger.debug("Extracting properties from resource " + resource.getDescription());
			}
			else {
				logger.info("Not extracting properties from resource " + resource.getDescription()
						+ " as this resource does not exist");
			}
		}
		Resource[] existingArray = new Resource[resources.size()];
		resources.toArray(existingArray);
		super.setLocations(existingArray);
	}
}
