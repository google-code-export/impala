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
import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.service.ServiceReferenceFilter;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.service.event.ServiceRegistryEvent;
import org.impalaframework.service.event.ServiceRegistryEventListener;
import org.impalaframework.service.proxy.ProxyHelper;
import org.impalaframework.service.registry.ServiceRegistryAware;
import org.springframework.util.Assert;

/**
 * Map implementation which is dynamically backed by the service registry. It implements
 * <code>ServiceRegistryEventListener</code> so that it can pick up and respond to changes in the service registry.
 * Uses the <code>ServiceRegistryContributionMapFilter</code> to filter out relevant service entries from the
 * service registry.
 * @see org.impalaframework.service.contribution.ServiceRegistryContributionMapFilter
 * @author Phil Zoio
 */
@SuppressWarnings(value={"hiding","unchecked"})
public class ServiceRegistryMap<String,V> implements Map<String,V>, 
	ServiceRegistryEventListener,
	ServiceRegistryAware, 
	ServiceActivityNotifiable {
	
	private static Log logger = LogFactory.getLog(ServiceRegistryMap.class);
	
	private Map<String,V> externalContributions = new ConcurrentHashMap<String, V>();
	private ServiceRegistryContributionMapFilter filter = new ServiceRegistryContributionMapFilter();
	private ServiceRegistryMonitor serviceRegistryMonitor;
	private ServiceRegistry serviceRegistry;
	private ProxyHelper proxyManager;
	
	public ServiceRegistryMap() {
		super();
		serviceRegistryMonitor = new ServiceRegistryMonitor();
		serviceRegistryMonitor.setServiceActivityNotifiable(this);
		proxyManager = new ProxyHelper();
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
		String contributionKeyName = (String) filter.getContributionKeyName(ref);
		
		if (contributionKeyName != null) {
			Object beanObject = ref.getBean();
			
			final Object proxyObject = maybeGetProxy(ref);
			final V proxy = castBean(proxyObject);
	
			externalContributions.put(contributionKeyName, proxy);
			if (logger.isDebugEnabled()) {
				logger.debug("Service " + beanObject + " added for contribution key " + contributionKeyName + " for filter " + filter);
			}
		}
	}
	
	public void remove(ServiceRegistryReference ref) {
		if (externalContributions.containsValue(ref.getBean())) {
			
			String contributionKeyName = (String) filter.getContributionKeyName(ref);
			V removed = externalContributions.remove(contributionKeyName);
			
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
	
	public Set<java.util.Map.Entry<String, V>> entrySet() {
		Set<Entry<String, V>> externalSet = this.externalContributions.entrySet();
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
	
	public Set<String> keySet() {
		Set<String> externalSet = this.externalContributions.keySet();
		return externalSet;
	}
	
	public int size() {
		int externalSize = this.externalContributions.size();
		return externalSize;
	}
	
	public Collection<V> values() {
		Collection<V> externalValues = this.externalContributions.values();
		return externalValues;
	}
	
	/* **************** Unsupported interface operations *************** */
	
	public V put(String key, V value) {
		throw new UnsupportedOperationException();
	}
	
	public void putAll(Map<? extends String, ? extends V> t) {
		throw new UnsupportedOperationException();
	}
	
	public V remove(Object key) {
		throw new UnsupportedOperationException();
	}

	/* ******************* Private and package method ******************** */
	
	protected Object maybeGetProxy(ServiceRegistryReference reference) {
		
		Object bean = reference.getBean();
		return proxyManager.maybeGetProxy(bean);
	}
	
	private V castBean(Object beanObject) {
		V bean = null;
		try {
			bean = (V) beanObject;
		} catch (ClassCastException e) {
			throw new InvalidStateException("bean " + beanObject + " coudl not be cast to the correct type", e);
		}
		return bean;
	}
	
	Map<String, V> getExternalContributions() {
		return externalContributions;
	}
	
	/* ******************* Protected getters ******************** */
	
	protected ServiceRegistry getServiceRegistry() {
		return serviceRegistry;
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
	
	public void setProxyEntries(boolean proxyEntries) {
		this.proxyManager.setProxyEntries(proxyEntries);
	}
	
	public void setProxyInterfaces(Class<?>[] proxyInterfaces) {
		this.proxyManager.setProxyInterfaces(proxyInterfaces);
	}
	
	public void setServiceRegistryMonitor(ServiceRegistryMonitor serviceRegistryMonitor) {
		this.serviceRegistryMonitor = serviceRegistryMonitor;
	}
	
	@Override
	public java.lang.String toString() {
		StringBuffer sb = new StringBuffer();
		String externalString = (String) externalContributions.toString();
		sb.append(externalString);
		return (java.lang.String) sb.toString();
	}
}
