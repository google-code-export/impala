package org.impalaframework.service.registry.contribution;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.impalaframework.service.registry.event.ServiceRegistryEvent;
import org.impalaframework.service.registry.event.ServiceRegistryEventListener;

public class ContributionMap<K,V> implements Map<K,V>, ServiceRegistryEventListener {

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
	}

}
