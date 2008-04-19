package org.impalaframework.service.registry.contribution;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.service.registry.ServiceReference;
import org.impalaframework.service.registry.event.ServiceAddedEvent;
import org.impalaframework.service.registry.event.ServiceRegistryEvent;
import org.impalaframework.service.registry.event.ServiceRegistryEventListener;
import org.impalaframework.service.registry.event.ServiceRemovedEvent;

public class ContributionMap<K,V> implements Map<K,V>, ServiceRegistryEventListener {

	private static Log logger = LogFactory.getLog(ContributionMap.class);
	
	private String contributedBeanAttributeName = "contributedBeanName";
	private String tagName;
	private Map<K,V> localContributions = new ConcurrentHashMap<K, V>();
	private Map<K,V> externalContributions = new ConcurrentHashMap<K, V>();
	
	public void clear() {
		this.localContributions.clear();
	}

	public boolean containsKey(Object key) {
		boolean hasKey = this.localContributions.containsKey(key);
		return hasKey;
	}

	public boolean containsValue(Object value) {
		boolean hasValue = this.localContributions.containsValue(value);
		return hasValue;
	}

	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return this.localContributions.entrySet();
	}

	public V get(Object key) {
		return this.localContributions.get(key);
	}

	public boolean isEmpty() {
		return this.localContributions.isEmpty();
	}

	public Set<K> keySet() {
		return this.localContributions.keySet();
	}

	public V put(K key, V value) {
		return this.localContributions.put(key, value);
	}

	public void putAll(Map<? extends K, ? extends V> map) {
		this.localContributions.putAll(map);
	}

	public V remove(Object key) {
		return this.localContributions.remove(key);
	}

	public int size() {
		return this.localContributions.size();
	}

	public Collection<V> values() {
		return this.localContributions.values();
	}

	public void handleServiceRegistryEvent(ServiceRegistryEvent event) {
		//add or remove from external contribution map
		if (event instanceof ServiceAddedEvent) {
			handleEventAdded(event);
		} else if (event instanceof ServiceRemovedEvent) {
			ServiceReference ref = event.getServiceReference();
			if (externalContributions.containsValue(ref.getBean())) {
				K contributionKeyName = getContributionKeyName(ref);
				V removed = externalContributions.remove(contributionKeyName);
				
				if (logger.isDebugEnabled()) {
					logger.debug("Service " + removed + " removed for contribution key " + contributionKeyName + " for tag " + tagName);
				}
			}
		}
	}

	private void handleEventAdded(ServiceRegistryEvent event) {
		ServiceReference ref = event.getServiceReference();
		K contributionKeyName = getContributionKeyName(ref);
		
		if (contributionKeyName != null) {
			Object beanObject = ref.getBean();
			V bean = castBean(beanObject);
			externalContributions.put(contributionKeyName, bean);
			if (logger.isDebugEnabled()) {
				logger.debug("Service " + bean + " added for contribution key " + contributionKeyName + " for tag " + tagName);
			}
		}
	}

	private K getContributionKeyName(ServiceReference ref) {
		K contributionKeyName = null;
		List<String> tags = ref.getTags();
		if (tags.contains(tagName)) {
			Object keyName = ref.getAttributes().get(contributedBeanAttributeName);
			contributionKeyName = castKeyName(keyName);	
		}
		return contributionKeyName;
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

	@SuppressWarnings("unchecked")
	private K castKeyName(Object keyName) {
		K contributionKeyName = null;
		try {
			contributionKeyName = (K) keyName;
		} catch (RuntimeException e) {
			throw new InvalidStateException("key " + contributionKeyName + " coudld not be cast to the correct type", e);
		}
		return contributionKeyName;
	}

	Map<K, V> getExternalContributions() {
		return externalContributions;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public void setContributedBeanAttributeName(String attributeName) {
		this.contributedBeanAttributeName = attributeName;
	}

	public void setLocalContributions(Map<K, V> localContributions) {
		this.localContributions = localContributions;
	}
	
	

}
