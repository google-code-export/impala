package org.impalaframework.config;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderSupport;

public class DynamicPropertiesFactoryBean extends PropertiesLoaderSupport
  implements FactoryBean {

	private Log log = LogFactory.getLog(DynamicPropertiesFactoryBean.class);
	
	private Resource[] resourceLocations;

	private long lastModified = 0L;

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
				long fileLastModified = file.lastModified();
				newLastModified = Math.max(newLastModified, fileLastModified);
				
				if (log.isDebugEnabled()) log.debug("Last modified for resource " + file + ": " + fileLastModified);
				
			} catch (IOException e) {
				System.out.println("Unable to get last modified for resource " + resource);
			}
		}

		long oldLastModified = this.lastModified;
		long diff = newLastModified - oldLastModified;
		if (diff > 0) {
			load = true;
			
			if (log.isDebugEnabled()) 
				log.debug("File has been updated more recently - reloading. Old: " + oldLastModified + ", new: " + newLastModified);
		} 

		this.lastModified = newLastModified;

		if (load) {
			Object createdInstance = super.mergeProperties();
			
			log.info("Reloaded properties from locations " + resourceLocations + ": " + createdInstance);
			this.instance = createdInstance;
		}

		return instance;
	}

	public Object getObject() throws IOException {
		return createInstance();
	}

	public Class<?> getObjectType() {
		return null;
	}

	public boolean isSingleton() {
		return false;
	}

	Long getLastModified() {
		return lastModified;
	}

}
