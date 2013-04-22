package org.impalaframework.service.contribution;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.service.ServiceReferenceFilter;
import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.service.ServiceRegistryEventListener;
import org.impalaframework.service.filter.ldap.LdapServiceReferenceFilter;
import org.springframework.util.ObjectUtils;

/**
 * {@link Set} implementation which is dynamically backed by the service
 * registry. It implements {@link ServiceRegistryEventListener} so that it can
 * pick up and respond to changes in the service registry. By default, uses
 * {@link LdapServiceReferenceFilter} to filter out relevant service entries
 * from the service registry. Alternatively, a {@link ServiceReferenceFilter}
 * can be wired in directly.
 * 
 * An entry is eligible for contribution to this list if it matches the
 * {@link ServiceReferenceFilter} associated with this instance. Each time a new
 * item is added or removed from the list is sorted.
 * 
 * All direct mutation methods from the {@link Set} throw
 * {@link UnsupportedOperationException}. Read-only methods delegate directly to
 * the underlying private {@link Set} instance.
 * 
 * Note that this implementation honours the {@link Set} contract - duplicates
 * of the contributed object will not appear in the set. It also does not provide
 * any guarrantees on the ordering of contributed beans.
 * 
 * @see BaseServiceRegistryMap
 * @author Phil Zoio
 */
@SuppressWarnings("unchecked")
public abstract class BaseServiceRegistrySet<T extends Object> extends BaseServiceRegistryTarget implements Set<T> {
    
    private static Log logger = LogFactory.getLog(BaseServiceRegistrySet.class);
    
    private Object lock = new Object();
    
    //using unsynchronized list as synchronisation is within code
    /**
     * Holds contributions, typically proxied objects
     */
    private Set<T> contributions = new LinkedHashSet<T>();
    
    /**
     * Holds mapping of {@link ServiceRegistryEntry}
     */
    private Set<ServiceRegistryEntry> services = new LinkedHashSet<ServiceRegistryEntry>();
    
    /**
     * Holds identify mapping of beans to proxies, to remove need for proxy recreation 
     * when bean is repopulated
     */
    private Map<Object,Object> proxyMap = new IdentityHashMap<Object, Object>();
    
    /* ******************* Implementation of ServiceRegistryNotifiable ******************** */

    public boolean add(ServiceRegistryEntry entry) {
        
        synchronized (lock) {
            if (!this.services.contains(entry)) {
                
                Set<ServiceRegistryEntry> services = new LinkedHashSet<ServiceRegistryEntry>(this.services);
                //add the reference and sort
                
                services.add(entry);
                repopulate(services);

                if (logger.isDebugEnabled()) {
                    logger.debug("Service " + entry + " added to " + ObjectUtils.identityToString(this));
                }
                
                return true;
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Service " + entry + " not added as it already present in " + ObjectUtils.identityToString(this));
                }
            }
        }
        return false;
    }

    public boolean remove(ServiceRegistryEntry entry) {
        
        synchronized (lock) {
            Set<ServiceRegistryEntry> services = new LinkedHashSet<ServiceRegistryEntry>(this.services);
            boolean removedRef = services.remove(entry);
            
            proxyMap.remove(entry.getServiceBeanReference().getService());
            
            if (removedRef) {
                repopulate(services); 
                if (logger.isDebugEnabled()) {
                    logger.debug("Service " + entry + " successfully removed from " + ObjectUtils.identityToString(this));
                }   
                return true;
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Service " + entry + " not removed as it was not present in " + ObjectUtils.identityToString(this));
                }
                return false;
            }
        }
    }
    
    @Override
    public void destroy() {
        super.destroy();
        contributions.clear();
        proxyMap.clear();
        services.clear();
    }

    protected abstract Object maybeGetProxy(ServiceRegistryEntry ref);

    private void repopulate(Set<ServiceRegistryEntry> services) {
        this.services.clear();
        this.contributions.clear();
        this.proxyMap.clear();
        
        //repopulate the services
        this.services.addAll(services);
        
        //repopulate the contributions list
        for (ServiceRegistryEntry entry : this.services) {
            Object bean = entry.getServiceBeanReference().getService();
            
            Object proxyObject = proxyMap.get(bean);
            if (proxyObject == null) {
                proxyObject = maybeGetProxy(entry);
                proxyMap.put(bean, proxyObject);
            }
            
            this.contributions.add((T)proxyObject);
        }
    }

    public boolean contains(Object object) {
        return contributions.contains(object);
    }

    public boolean containsAll(Collection<?> collections) {
        return contributions.containsAll(collections);
    }

    public boolean isEmpty() {
        return contributions.isEmpty();
    }

    public Iterator<T> iterator() {
        return contributions.iterator();
    }

    public int size() {
        return contributions.size();
    }
    
    public Object[] toArray() {
        return contributions.toArray();
    }

    public Object[] toArray(Object[] array) {
        return contributions.toArray(array);
    }
    
    /* **************** Unsupported list operations *************** */
    
    public boolean add(Object arg0) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection<? extends T> collection) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean remove(Object object) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    /* ******************* protected methods ******************** */

    protected Set<ServiceRegistryEntry> getServices() {
        return services;
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
