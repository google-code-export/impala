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
import org.impalaframework.service.registry.ServiceRegistryAware;
import org.springframework.util.Assert;

/**
 * Map implementation which is dynamically backed by the service registry. It
 * implements <code>ServiceRegistryEventListener</code> so that it can pick up
 * and respond to changes in the service registry. Uses the
 * <code>ServiceRegistryContributionMapFilter</code> to filter out relevant
 * service entries from the service registry.
 * 
 * @see org.impalaframework.service.contribution.ServiceRegistryContributionMapFilter
 * @author Phil Zoio
 */
@SuppressWarnings("unchecked")
public abstract class BaseServiceRegistryMap implements Map, 
	ServiceRegistryEventListener,
	ServiceRegistryAware, 
	ServiceActivityNotifiable {
	
	private static Log logger = LogFactory.getLog(BaseServiceRegistryMap.class);
	
	private Map<String,Object> externalContributions = new ConcurrentHashMap<String,Object>();
	
	//FIXME should wire in filter or use filter string
	private ServiceRegistryContributionMapFilter filter = new ServiceRegistryContributionMapFilter();
	private ServiceRegistryMonitor serviceRegistryMonitor;
	private ServiceRegistry serviceRegistry;
	
	public BaseServiceRegistryMap() {
		super();
		serviceRegistryMonitor = new ServiceRegistryMonitor();
		serviceRegistryMonitor.setServiceActivityNotifiable(this);
	}
	
	/* **************** Initializing method *************** */
	
	public void init() throws Exception {
		Assert.notNull(serviceRegistry);
		Assert.notNull(serviceRegistryMonitor);
		
		serviceRegistryMonitor.setServiceRegistry(serviceRegistry);
		serviceRegistryMonitor.init();
	}
	
	/* ******************* Implementation of ServiceRegistryNotifiable ******************** */
	
	public ServiceReferenceFilter getServiceReferenceFilter() {
		return filter;
	}
	
	public void add(ServiceRegistryReference ref) {
		String contributionKeyName = filter.getContributionKeyName(ref);
		
		//FIXME filter should not return item if no contribution key is present
		if (contributionKeyName != null) {
			Object beanObject = ref.getBean();
			
			final Object proxyObject = maybeGetProxy(ref);
	
			externalContributions.put(contributionKeyName, proxyObject);
			if (logger.isDebugEnabled()) {
				logger.debug("Service " + beanObject + " added for contribution key " + contributionKeyName + " for filter " + filter);
			}
		}
	}
	
	public void remove(ServiceRegistryReference ref) {
		if (externalContributions.containsValue(ref.getBean())) {
			
			String contributionKeyName = filter.getContributionKeyName(ref);
			Object removed = externalContributions.remove(contributionKeyName);
			
			if (logger.isDebugEnabled()) {
				logger.debug("Service " + removed + " removed for contribution key " + contributionKeyName + " for filter " + filter);
			}
		}
	}

	/* ******************* Implementation of ServiceRegistryEventListener ******************** */

	public void handleServiceRegistryEvent(ServiceRegistryEvent event) {
		serviceRegistryMonitor.handleServiceRegistryEvent(event);
	}
	
	/* **************** Map implementation *************** */

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
	
	public Set entrySet() {
		Set externalSet = this.externalContributions.entrySet();
		return externalSet;
	}
	
	public Object get(Object key) {
		Object value = this.externalContributions.get(key);
		return value;
	}
	
	public boolean isEmpty() {
		boolean isEmpty = this.externalContributions.isEmpty();
		return isEmpty;
	}
	
	public Set keySet() {
		Set externalSet = this.externalContributions.keySet();
		return externalSet;
	}
	
	public int size() {
		int externalSize = this.externalContributions.size();
		return externalSize;
	}
	
	public Collection values() {
		Collection externalValues = this.externalContributions.values();
		return externalValues;
	}
	
	/* **************** Unsupported interface operations *************** */
	
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
	
	Map<String, Object> getExternalContributions() {
		return externalContributions;
	}

	/* ******************* ServiceRegistryAware implementation ******************** */
	
	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}	
	
	/* ******************* Injected setters ******************** */
	
	public void setTagName(String tagName) {
		this.filter.setTagName((java.lang.String) tagName);
	}
	
	public void setContributedBeanAttributeName(String attributeName) {
		this.filter.setContributedBeanAttributeName((java.lang.String) attributeName);
	}
	
	public void setServiceRegistryMonitor(ServiceRegistryMonitor serviceRegistryMonitor) {
		this.serviceRegistryMonitor = serviceRegistryMonitor;
	}
	
	@Override
	public java.lang.String toString() {
		StringBuffer sb = new StringBuffer();
		String externalString = externalContributions.toString();
		sb.append(externalString);
		return sb.toString();
	}

}
