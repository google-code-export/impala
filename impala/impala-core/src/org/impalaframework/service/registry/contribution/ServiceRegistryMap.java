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

package org.impalaframework.service.registry.contribution;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.service.registry.BasicServiceRegistryReference;
import org.impalaframework.service.registry.ServiceRegistry;
import org.impalaframework.service.registry.ServiceRegistryAware;
import org.impalaframework.service.registry.ServiceRegistryReference;
import org.impalaframework.service.registry.event.ServiceAddedEvent;
import org.impalaframework.service.registry.event.ServiceRegistryEvent;
import org.impalaframework.service.registry.event.ServiceRegistryEventListener;
import org.impalaframework.service.registry.event.ServiceRemovedEvent;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class ServiceRegistryMap<K,V> implements Map<K,V>, ServiceRegistryEventListener, InitializingBean, ServiceRegistryAware {
	
	private static Log logger = LogFactory.getLog(ServiceRegistryMap.class);
	
	private Map<K,V> externalContributions = new ConcurrentHashMap<K, V>();
	private ServiceRegistryContributionMapFilter<K> filter = new ServiceRegistryContributionMapFilter<K>();
	private ServiceRegistry serviceRegistry;
	
	public void clear() {
	}

	public boolean containsKey(Object key) {
		boolean hasKey = this.externalContributions.containsKey(key);
		return hasKey;
	}

	public boolean containsValue(Object value) {
		boolean hasValue = this.externalContributions.containsValue(value);
		return hasValue;
	}

	public Set<java.util.Map.Entry<K, V>> entrySet() {
		Set<Entry<K, V>> externalSet = this.externalContributions.entrySet();
		return externalSet;
	}

	public V get(Object key) {
		V value =this.externalContributions.get(key);
		return value;
	}

	public boolean isEmpty() {
		boolean isEmpty = this.externalContributions.isEmpty();
		return isEmpty;
	}

	public Set<K> keySet() {
		Set<K> externalSet = this.externalContributions.keySet();
		return externalSet;
	}

	public V put(K key, V value) {
		return null;
	}

	public void putAll(Map<? extends K, ? extends V> map) {
	}

	public V remove(Object key) {
		return null;
	}

	public int size() {
		int externalSize = this.externalContributions.size();
		return externalSize;
	}

	public Collection<V> values() {
		Collection<V> externalValues = this.externalContributions.values();
		return externalValues;
	}

	public void handleServiceRegistryEvent(ServiceRegistryEvent event) {
		//add or remove from external contribution map
		if (event instanceof ServiceAddedEvent) {
			handleEventAdded(event);
		} else if (event instanceof ServiceRemovedEvent) {
			handleEventRemoved(event);
		}
	}

	private void handleEventRemoved(ServiceRegistryEvent event) {
		ServiceRegistryReference ref = event.getServiceReference();
		if (externalContributions.containsValue(ref.getBean())) {
			
			K contributionKeyName = filter.getContributionKeyName(ref);
			V removed = externalContributions.remove(contributionKeyName);
			
			if (logger.isDebugEnabled()) {
				logger.debug("Service " + removed + " removed for contribution key " + contributionKeyName + " for filter " + filter);
			}
		}
	}

	private void handleEventAdded(ServiceRegistryEvent event) {
		ServiceRegistryReference ref = event.getServiceReference();
		addService(ref);
	}

	private void addService(ServiceRegistryReference ref) {
		K contributionKeyName = filter.getContributionKeyName(ref);
		
		if (contributionKeyName != null) {
			Object beanObject = ref.getBean();
			V bean = castBean(beanObject);
			externalContributions.put(contributionKeyName, bean);
			if (logger.isDebugEnabled()) {
				logger.debug("Service " + bean + " added for contribution key " + contributionKeyName + " for filter " + filter);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private V castBean(Object beanObject) {
		V bean = null;
		try {
			bean = (V) beanObject;
		} catch (ClassCastException e) {
			throw new InvalidStateException("bean " + beanObject + " coudld not be cast to the correct type", e);
		}
		return bean;
	}

	Map<K, V> getExternalContributions() {
		return externalContributions;
	}

	public void setTagName(String tagName) {
		this.filter.setTagName(tagName);
	}

	public void setContributedBeanAttributeName(String attributeName) {
		this.filter.setContributedBeanAttributeName(attributeName);
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(serviceRegistry);
		Collection<ServiceRegistryReference> services = serviceRegistry.getServices(filter);
		for (ServiceRegistryReference serviceReference : services) {
			addService(serviceReference);
		}
	}

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}	
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		String externalString = externalContributions.toString();
		sb.append(externalString);
		return sb.toString();
	}

}
