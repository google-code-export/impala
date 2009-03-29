/*
 * Copyright 2007-2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.service.registry.internal;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.service.ServiceReferenceFilter;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.service.event.ServiceAddedEvent;
import org.impalaframework.service.event.ServiceRegistryEvent;
import org.impalaframework.service.event.ServiceRegistryEventListener;
import org.impalaframework.service.event.ServiceRemovedEvent;
import org.impalaframework.service.registry.BasicServiceRegistryReference;
import org.springframework.util.Assert;

/**
 * Implementation of {@link ServiceRegistry}, which holds services which can be shared across modules
 * @author Phil Zoio
 */
public class ServiceRegistryImpl implements ServiceRegistry {

	private static Log logger = LogFactory.getLog(ServiceRegistryImpl.class);

	//FIXME issue 4 - need to move away from holding string to ServiceRegistryReference as this is not very flexible
	private Map<String, ServiceRegistryReference> beanNameToService = new ConcurrentHashMap<String, ServiceRegistryReference>();
	private Map<Object, String> targetToBeanName = new IdentityHashMap<Object, String>();

	// use CopyOnWriteArrayList to support non-blocking thread-safe iteration
	private List<ServiceRegistryEventListener> listeners = new CopyOnWriteArrayList<ServiceRegistryEventListener>();

	private Object registryLock = new Object();
	private Object listenersLock = new Object();

	/**
	 * Adds to global event listeners to which all service registry events will
	 * be broadcast
	 */
	public void addEventListener(ServiceRegistryEventListener listener) {
		Assert.notNull(listener);
		
		synchronized (listenersLock) {
			listeners.add(listener);
		}
		if (logger.isDebugEnabled())
			logger.debug("Added service registry listener " + listener);
	}
	
	/**
	 * Removes global event listeners to which all service registry events will
	 * be broadcast
	 */
	public void removeEventListener(ServiceRegistryEventListener listener) {
		List<ServiceRegistryEventListener> listeners = this.listeners;
		removeListener(listener, listeners);
	}

	public void addService(
			String beanName, 
			String moduleName, 
			Object service,
			ClassLoader classLoader) {
		addService(beanName, moduleName, service, null, classLoader);
	}

	public void addService(
			String beanName, 
			String moduleName, 
			Object service,
			Map<String, ?> attributes, 
			ClassLoader classLoader) {
		
		BasicServiceRegistryReference serviceReference = null;
		synchronized (registryLock) {
			serviceReference = new BasicServiceRegistryReference(service, beanName,
					moduleName, attributes, classLoader);
			
			//deal with the case of overriding and existing bean
			if (beanNameToService.containsKey(beanName)) {
				ServiceRegistryReference ref = beanNameToService.get(beanName);
				throw new InvalidStateException("Cannot register bean named '" + beanName + "' as entry for this name is already present in the service registry. Currently registered bean from module " 
						+ ref.getContributingModule() 
						+ "', with class '" 
						+ ref.getBean().getClass().getName() + "'" );
			}
			
			beanNameToService.put(beanName, serviceReference);
			targetToBeanName.put(service, beanName);
		}
		
		if (logger.isDebugEnabled())
			logger.debug("Added service bean '" + beanName
					+ "' contributed from module '" + moduleName
					+ "' to service registry, with attributes " + attributes);

		ServiceAddedEvent event = new ServiceAddedEvent(serviceReference);
		invokeListeners(event);
	}

	public void remove(Object service) {
		
		ServiceRegistryReference serviceReference = null;
		String beanName = null;
		
		synchronized (registryLock) {
			beanName = targetToBeanName.remove(service);
			if (beanName != null) {
				serviceReference = beanNameToService.remove(beanName);
			}
		}

		if (serviceReference != null) {
			
			if (logger.isDebugEnabled())
				logger.debug("Removed from service reference bean '" + beanName
						+ "' contributed from module '"
						+ serviceReference.getContributingModule() + "'");
			
			ServiceRemovedEvent event = new ServiceRemovedEvent(serviceReference);
			invokeListeners(event);
		}
	}

	public ServiceRegistryReference getService(String beanName) {
		return beanNameToService.get(beanName);
	}

	/* ************ helper methods * ************** */

	private List<ServiceRegistryEventListener> getListeners() {
		return listeners;
	}

	private void invokeListeners(ServiceRegistryEvent event) {
		
		List<ServiceRegistryEventListener> listeners = getListeners();
		
		//inform all listeners of service listener event
		for (ServiceRegistryEventListener listener : listeners) {
			listener.handleServiceRegistryEvent(event);
		}
	}

	private void removeListener(ServiceRegistryEventListener listener, List<ServiceRegistryEventListener> listeners) {
		
		for (Iterator<ServiceRegistryEventListener> iterator = listeners.iterator(); iterator.hasNext();) {
			
			ServiceRegistryEventListener currentListener = iterator.next();
			
			if (currentListener == listener) {
				iterator.remove();
				if (logger.isDebugEnabled())
					logger.debug("Removed service registry listener " + listener);
			}
		}
	}

	public List<ServiceRegistryReference> getServices(ServiceReferenceFilter filter) {
		
		List<ServiceRegistryReference> serviceList = new LinkedList<ServiceRegistryReference>();
		Collection<ServiceRegistryReference> values = beanNameToService.values();
		
	    for (ServiceRegistryReference serviceReference : values) {
			if (filter.matches(serviceReference)) {
				serviceList.add(serviceReference);
			}
		}
		return serviceList;
	}

}
