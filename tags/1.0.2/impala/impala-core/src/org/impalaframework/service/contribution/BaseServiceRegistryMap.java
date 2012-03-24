/*
 * Copyright 2007-2010 the original author or authors.
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
import org.impalaframework.service.ServiceRegistryEventListener;
import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.service.filter.ldap.LdapServiceReferenceFilter;

/**
 * Map implementation which is dynamically backed by the service registry. It
 * implements {@link ServiceRegistryEventListener} so that it can pick up and
 * respond to changes in the service registry. By default, uses
 * {@link LdapServiceReferenceFilter} to filter out relevant service entries
 * from the service registry. Alternatively, a {@link ServiceReferenceFilter}
 * can be wired in directly.
 * 
 * An entry is eligible for contribution to this map if it matches the
 * {@link ServiceReferenceFilter} associated with this instance, and if it has a
 * non-null "map key" attribute. The default name for this attribute is "mapkey"
 * but can be changed using {@link #setMapKey(String)}. The value of this
 * attribute in {@link ServiceReferenceFilter} is used as the key for a
 * contribution added to this map.
 * 
 * All direct mutation methods from the {@link Map} throw
 * {@link UnsupportedOperationException}. Read-only methods delegate directly to
 * the underlying private {@link Map} instance.
 * 
 * @see org.impalaframework.service.filter.ldap.LdapServiceReferenceFilter
 * @see BaseServiceRegistryList
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
     * The {@link ServiceRegistryEntry} attribute which is used 
     * as a key for contributions added to the key of this map. By default, specified using the
     * key "mapkey" in the {@link ServiceRegistryEntry} instance.
     */
    private String mapKey = "mapkey";
    
    public BaseServiceRegistryMap() {
        super();
    }
    
    /* ******************* Implementation of ServiceRegistryNotifiable ******************** */
    
    public boolean add(ServiceRegistryEntry entry) {
        
        final Map<String, ?> attributes = entry.getAttributes();
        final Object contributionKeyName = attributes.get(mapKey);
        
        if (contributionKeyName != null) {
            Object beanObject = entry.getServiceBeanReference().getService();
            
            final Object proxyObject = maybeGetProxy(entry);
    
            this.contributions.put(contributionKeyName.toString(), proxyObject);
            if (logger.isDebugEnabled()) {
                logger.debug("Service " + beanObject + " added for contribution key " + contributionKeyName + " for filter " + getFilter());
            }
            return true;
        } else {
            logger.warn("Service with bean name " + entry.getBeanName() 
                    + " from contributing module " + entry.getContributingModule()
                    + " of class " + entry.getServiceBeanReference().getService().getClass().getName() 
                    + " does not have a '" + mapKey 
                    + "' attribute, so cannot be used in service registry map");
        }
        return false;
    }
    
    public boolean remove(ServiceRegistryEntry entry) {
        if (contributions.containsValue(entry.getServiceBeanReference().getService())) {
            final Map<String, ?> attributes = entry.getAttributes();
            final Object contributionKeyName = attributes.get(mapKey);
            
            return (this.contributions.remove(contributionKeyName) != null);
        }
        return false;
    }
    
    @Override
    public void destroy() {
        super.destroy();
        contributions.clear();
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
    
    protected abstract Object maybeGetProxy(ServiceRegistryEntry entry);
    
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
        sb.append(this.getClass().getName());
        sb.append(": ");
        String externalString = contributions.toString();
        sb.append(externalString);
        return sb.toString();
    }

}
