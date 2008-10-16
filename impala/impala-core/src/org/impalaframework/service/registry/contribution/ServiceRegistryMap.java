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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.service.registry.ServiceRegistryAware;
import org.impalaframework.service.registry.event.ServiceAddedEvent;
import org.impalaframework.service.registry.event.ServiceRegistryEvent;
import org.impalaframework.service.registry.event.ServiceRegistryEventListener;
import org.impalaframework.service.registry.event.ServiceRemovedEvent;
import org.impalaframework.util.ReflectionUtils;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Map implementation which is dynamically backed by the service registry. It implements
 * <code>ServiceRegistryEventListener</code> so that it can pick up and respond to changes in the service registry.
 * Uses the <code>ServiceRegistryContributionMapFilter</code> to filter out relevant service entries from the
 * service registry.
 * @see org.impalaframework.service.registry.contribution.ServiceRegistryContributionMapFilter
 * @author Phil Zoio
 */
public class ServiceRegistryMap<K,V> implements Map<K,V>, ServiceRegistryEventListener, InitializingBean, ServiceRegistryAware {
	
	private static Log logger = LogFactory.getLog(ServiceRegistryMap.class);
	
	private Map<K,V> externalContributions = new ConcurrentHashMap<K, V>();
	private ServiceRegistryContributionMapFilter<K> filter = new ServiceRegistryContributionMapFilter<K>();
	private ServiceRegistry serviceRegistry;
	
	//whether to proxy entries or not. By default true
	private boolean proxyEntries = true;
	
	//the interfaces to use for proxies
	private Class<?>[] proxyInterfaces;

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
	
	/* **************** ServiceRegistryEventListener implementation *************** */

	public void handleServiceRegistryEvent(ServiceRegistryEvent event) {
		//add or remove from external contribution map
		if (event instanceof ServiceAddedEvent) {
			handleEventAdded(event);
		} else if (event instanceof ServiceRemovedEvent) {
			handleEventRemoved(event);
		}
	}
	
	/* **************** InitializingBean implementation *************** */
	
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(serviceRegistry);
		Collection<ServiceRegistryReference> services = serviceRegistry.getServices(filter);
		for (ServiceRegistryReference serviceReference : services) {
			addService(serviceReference);
		}
	}
	
	/* ******************* Private and package method ******************** */
	
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
			
			final Object proxyObject = maybeGetProxy(bean);
			final V proxy = castBean(proxyObject);

			externalContributions.put(contributionKeyName, proxy);
			if (logger.isDebugEnabled()) {
				logger.debug("Service " + bean + " added for contribution key " + contributionKeyName + " for filter " + filter);
			}
		}
	}

	/**
	 * Possibly wraps bean with proxy. Based on following rules:
	 * <ul>
	 * <li>if proxyEntries is false, then does not wrap
	 * <li>if the bean does not implement any interface, then does not wrap
	 * <li>if proxyInterfaces is not empty, but bean does not implement any of the specified interface, then does not wrap
	 * <li>if proxyInterfaces is not empty, then wraps with proxy, exposing with interfaces which are both are in proxyInterfaces and implemented by bean
	 * <li>if proxyInterfaces is null or empty, then wraps with proxy, exposing with all interfaces implemented by bean
	 * </ul>
	 */
	Object maybeGetProxy(V bean) {
		
		if (!proxyEntries) {
			return bean;
		}
		
		final List<Class<?>> interfaces = ReflectionUtils.findInterfaceList(bean);
		
		if (interfaces.size() == 0) {
			logger.warn("Bean of instance " + bean.getClass().getName() + " could not be backed by a proxy as it does not implement an interface");
			//TODO should be try to use class-based proxy here using CGLIB
			return bean;
		}
		
		final List<Class<?>> filteredInterfaces = new ArrayList<Class<?>>();
		
		if (proxyInterfaces != null && proxyInterfaces.length > 0) {
			
			//only proxy if we have a match
			for (int i = 0; i < proxyInterfaces.length; i++) {
				final Class<?> proxyInterface = proxyInterfaces[i];
				if (interfaces.contains(proxyInterface)) {
					filteredInterfaces.add(proxyInterface);
				}
			}
			
			if (filteredInterfaces.size() == 0) {
				logger.warn("Bean of instance " + bean.getClass().getName() + " does not implement any of the specified interfaces: " + proxyInterfaces);
				//TODO should be try to use class-based proxy here using CGLIB
				return bean;
			}
		} else {
			filteredInterfaces.addAll(interfaces);
		}
		
		ProxyFactory proxyFactory = new ProxyFactory();
		proxyFactory.setInterfaces(filteredInterfaces.toArray(new Class[0]));
		proxyFactory.setTarget(bean);
		final Object proxyObject = proxyFactory.getProxy();
		return proxyObject;
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
	
	/* ******************* Injected setters ******************** */

	public void setTagName(String tagName) {
		this.filter.setTagName(tagName);
	}

	public void setContributedBeanAttributeName(String attributeName) {
		this.filter.setContributedBeanAttributeName(attributeName);
	}

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}	
	
	public void setProxyEntries(boolean proxyEntries) {
		this.proxyEntries = proxyEntries;
	}

	public void setProxyInterfaces(Class<?>[] proxyInterfaces) {
		this.proxyInterfaces = proxyInterfaces;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		String externalString = externalContributions.toString();
		sb.append(externalString);
		return sb.toString();
	}

}
