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

package org.impalaframework.service.contribution;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.service.ServiceReferenceFilter;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.service.event.ServiceRegistryEvent;
import org.impalaframework.service.event.ServiceRegistryEventListener;
import org.impalaframework.service.filter.ldap.LdapServiceReferenceFilter;
import org.impalaframework.service.registry.ServiceRegistryAware;
import org.springframework.util.Assert;

/**
 * Map implementation which is dynamically backed by the service registry. It
 * implements {@link ServiceRegistryEventListener} so that it can pick up
 * and respond to changes in the service registry. By default, uses
 * {@link LdapServiceReferenceFilter} to filter out relevant
 * service entries from the service registry. Alternatively, a {@link ServiceReferenceFilter}
 * can be wired in directly.
 * 
 * An entry is eligible for contribution to this map if it matches the {@link ServiceReferenceFilter}
 * associated with this instance, and if it has a non-null "map key" attribute.
 * The default name for this attribute is "mapkey" but can be changed using {@link #setMapKey(String)}.
 * The value of this attribute in {@link ServiceReferenceFilter} is used as the key for a contribution
 * added to this map.
 * 
 * @see LdapServiceReferenceFilter
 * @author Phil Zoio
 */
@SuppressWarnings("unchecked")
//FIXME extract superclass which can be basis of List and Map implementation
public abstract class BaseServiceRegistryMap implements Map, 
	ServiceRegistryEventListener,
	ServiceRegistryAware, 
	ServiceActivityNotifiable {
	
	private static Log logger = LogFactory.getLog(BaseServiceRegistryMap.class);
	
	/**
	 * Holds external contributions to this map, obtained from the service registry
	 */
	private Map<String,Object> contributions = new ConcurrentHashMap<String,Object>();
	
	/**
	 * Used to simplify interactions with the {@link ServiceRegistry}
	 */
	private ServiceRegistryMonitor serviceRegistryMonitor;
	
	/**
	 * Source of map contributions
	 */
	private ServiceRegistry serviceRegistry;
	
	/**
	 * The {@link ServiceRegistryReference} attribute which is used 
	 * as a key for contributions added to the key of this map. By default, specified using the
	 * key "mapkey" in the {@link ServiceRegistryReference} instance.
	 */
	private String mapKey = "mapkey";
	
	/**
	 * Filter expression used to retrieve services which are eligible to be added
	 * as a contribution to this map.
	 */
	private String filterExpression;
	
	/**
	 * Filter used to retrieve services which are eligible to be added
	 * as a contribution to this map.
	 */
	private ServiceReferenceFilter filter;
	
	public BaseServiceRegistryMap() {
		super();
		this.serviceRegistryMonitor = new ServiceRegistryMonitor();
		this.serviceRegistryMonitor.setServiceActivityNotifiable(this);
	}
	
	/* **************** Initializing method *************** */
	
	public void init() {
		Assert.notNull(this.serviceRegistry, "serviceRegistry cannot be null");
		Assert.notNull(this.serviceRegistryMonitor, "serviceRegistryMonitor cannot be null");

		if (this.filter == null) {
			Assert.notNull(this.filterExpression, "filterExpression and filte both cannot be null");
			this.filter = new LdapServiceReferenceFilter(filterExpression);
		}
		
		this.serviceRegistryMonitor.setServiceRegistry(serviceRegistry);
		this.serviceRegistryMonitor.init();
	}
	
	/* ******************* Implementation of ServiceRegistryNotifiable ******************** */
	
	public ServiceReferenceFilter getServiceReferenceFilter() {
		return filter;
	}
	
	public void add(ServiceRegistryReference ref) {
		
		final Map<String, ?> attributes = ref.getAttributes();
		final Object contributionKeyName = attributes.get(mapKey);
		
		if (contributionKeyName != null) {
			Object beanObject = ref.getBean();
			
			final Object proxyObject = maybeGetProxy(ref);
	
			this.contributions.put(contributionKeyName.toString(), proxyObject);
			if (logger.isDebugEnabled()) {
				logger.debug("Service " + beanObject + " added for contribution key " + contributionKeyName + " for filter " + filter);
			}
		} else {
			//FIXME log no mapKeyValue
		}
	}
	
	public void remove(ServiceRegistryReference ref) {
		if (contributions.containsValue(ref.getBean())) {
			final Map<String, ?> attributes = ref.getAttributes();
			final Object contributionKeyName = attributes.get(mapKey);
			
			this.contributions.remove(contributionKeyName);
		}
	}

	/* ******************* Implementation of ServiceRegistryEventListener ******************** */

	public void handleServiceRegistryEvent(ServiceRegistryEvent event) {
		serviceRegistryMonitor.handleServiceRegistryEvent(event);
	}
	
	/* **************** Map implementation *************** */
	
	public boolean containsKey(Object key) {
		boolean hasKey = this.contributions.containsKey(key);
		return hasKey;
	}
	
	public boolean containsValue(Object value) {
		boolean hasValue = this.contributions.containsValue(value);
		return hasValue;
	}
	
	public Set entrySet() {
		Set externalSet = this.contributions.entrySet();
		return externalSet;
	}
	
	public Object get(Object key) {
		Object value = this.contributions.get(key);
		return value;
	}
	
	public boolean isEmpty() {
		boolean isEmpty = this.contributions.isEmpty();
		return isEmpty;
	}
	
	public Set keySet() {
		Set externalSet = this.contributions.keySet();
		return externalSet;
	}
	
	public int size() {
		int externalSize = this.contributions.size();
		return externalSize;
	}
	
	public Collection values() {
		Collection externalValues = this.contributions.values();
		return externalValues;
	}
	
	/* **************** Unsupported interface operations *************** */

	public void clear() {
		throw new UnsupportedOperationException();
	}
	
	public Object put(Object key, Object value) {
		throw new UnsupportedOperationException();
	}
	
	public void putAll(Map t) {
		throw new UnsupportedOperationException();
	}
	
	public Object remove(Object key) {
		throw new UnsupportedOperationException();
	}

	/* ******************* Protected and package level methods ******************** */
	
	protected abstract Object maybeGetProxy(ServiceRegistryReference reference);
	
	protected ServiceRegistry getServiceRegistry() {
		return serviceRegistry;
	}
	
	Map<String, Object> getContributions() {
		return contributions;
	}

	/* ******************* ServiceRegistryAware implementation ******************** */
	
	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}	
	
	/* ******************* Injected setters ******************** */
	
	public void setFilterExpression(String filterExpression) {
		this.filterExpression = filterExpression;
	}
	
	public void setFilter(ServiceReferenceFilter filter) {
		this.filter = filter;
	}
	
	public void setMapKey(String mapKey) {
		this.mapKey = mapKey;
	}

	public void setServiceRegistryMonitor(ServiceRegistryMonitor serviceRegistryMonitor) {
		this.serviceRegistryMonitor = serviceRegistryMonitor;
	}

	/* ******************* toString() implementation ******************** */
	
	@Override
	public java.lang.String toString() {
		StringBuffer sb = new StringBuffer();
		String externalString = contributions.toString();
		sb.append(externalString);
		return sb.toString();
	}

}
