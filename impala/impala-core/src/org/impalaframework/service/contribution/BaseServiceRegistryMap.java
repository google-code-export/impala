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
import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.service.event.ServiceRegistryEventListener;
import org.impalaframework.service.filter.ldap.LdapServiceReferenceFilter;

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
public abstract class BaseServiceRegistryMap extends BaseServiceRegistryTarget implements Map {
    
    private static Log logger = LogFactory.getLog(BaseServiceRegistryMap.class);
    
    /**
     * Holds external contributions to this map, obtained from the service registry
     */
    private Map<String,Object> contributions = new ConcurrentHashMap<String,Object>();
    
    /**
     * The {@link ServiceRegistryReference} attribute which is used 
     * as a key for contributions added to the key of this map. By default, specified using the
     * key "mapkey" in the {@link ServiceRegistryReference} instance.
     */
    private String mapKey = "mapkey";
    
    public BaseServiceRegistryMap() {
        super();
    }
    
    /* ******************* Implementation of ServiceRegistryNotifiable ******************** */
    
    public void add(ServiceRegistryReference ref) {
        
        final Map<String, ?> attributes = ref.getAttributes();
        final Object contributionKeyName = attributes.get(mapKey);
        
        if (contributionKeyName != null) {
            Object beanObject = ref.getBean();
            
            final Object proxyObject = maybeGetProxy(ref);
    
            this.contributions.put(contributionKeyName.toString(), proxyObject);
            if (logger.isDebugEnabled()) {
                logger.debug("Service " + beanObject + " added for contribution key " + contributionKeyName + " for filter " + getFilter());
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
    
    /* **************** Unsupported map operations *************** */

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
    
    Map<String, Object> getContributions() {
        return contributions;
    }
    
    /* ******************* Injected setters ******************** */
    
    public void setMapKey(String mapKey) {
        this.mapKey = mapKey;
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
