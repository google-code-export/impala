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

public class ServiceRegistryMap<K,V> implements Map<K,V>, ServiceRegistryEventListener {
	
	private static Log logger = LogFactory.getLog(ServiceRegistryMap.class);
	
	private String contributedBeanAttributeName = "contributedBeanName";
	private String tagName;
	private Map<K,V> externalContributions = new ConcurrentHashMap<K, V>();
	
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
		
		//FIXME extract this into a ServiceReferenceFilter implementation
		
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
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		String externalString = externalContributions.toString();
		sb.append("external contributions: ").append(externalString);
		return sb.toString();
	}	

}
