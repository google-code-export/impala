package org.impalaframework.config;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderSupport;

public class DynamicPropertiesFactoryBean extends PropertiesLoaderSupport
  implements FactoryBean {

	private Log log = LogFactory.getLog(DynamicPropertiesFactoryBean.class);
	
	private LocationModificationStateHolder stateHolder = new LocationModificationStateHolder();

	private Object instance;

	@Override
	public void setLocations(Resource[] locations) {
		stateHolder.setLocations(locations);
		super.setLocations(locations);
	}

	@Override
	public void setLocation(Resource location) {
		stateHolder.setLocation(location);
		super.setLocation(location);
	}
	
	public Object createInstance() throws IOException {

		boolean load = false;
		if (this.instance == null) {
			load = true;
			stateHolder.isModifiedSinceLastCheck();
		}
		else {
			load = stateHolder.isModifiedSinceLastCheck();
		}

		if (load) {
			Object createdInstance = super.mergeProperties();
			
			log.info("Reloaded properties from locations " + stateHolder.getLocations() + ": " + createdInstance);
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
		return stateHolder.getLastModified();
	}

}
