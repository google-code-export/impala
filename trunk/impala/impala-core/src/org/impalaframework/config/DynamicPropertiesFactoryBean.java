package org.impalaframework.config;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderSupport;

public class DynamicPropertiesFactoryBean extends PropertiesLoaderSupport
  implements FactoryBean {

	private Resource[] resourceLocations;

	private AtomicLong lastModified = new AtomicLong(0L);

	private Object instance;

	@Override
	public void setLocations(Resource[] locations) {
		this.resourceLocations = locations;
		super.setLocations(locations);
	}

	@Override
	public void setLocation(Resource location) {
		this.resourceLocations = new Resource[] { location };
		super.setLocation(location);
	}
	
	public Object createInstance() throws IOException {

		boolean load = false;
		if (this.instance == null) {
			load = true;
		}

		long newLastModified = 0L;

		for (Resource resource : resourceLocations) {
			try {
				File file = resource.getFile();
				newLastModified = Math.max(newLastModified, file
						.lastModified());
			} catch (IOException e) {
			}
		}

		if (newLastModified > lastModified.get());
			load = true;

		lastModified.set(newLastModified);

		if (load) {
			Object createdInstance = super.mergeProperties();
			this.instance = createdInstance;
		}

		return instance;
	}

	public Object getObject() throws Exception {
		return createInstance();
	}

	public Class<?> getObjectType() {
		return null;
	}

	public boolean isSingleton() {
		return false;
	}

	Long getLastModified() {
		return lastModified.get();
	}

}
