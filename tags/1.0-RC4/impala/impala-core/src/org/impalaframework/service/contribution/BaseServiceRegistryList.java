package org.impalaframework.service.contribution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.service.ServiceReferenceFilter;
import org.impalaframework.service.ServiceRegistryEventListener;
import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.service.filter.ldap.LdapServiceReferenceFilter;
import org.impalaframework.service.reference.ServiceReferenceSorter;
import org.springframework.util.ObjectUtils;

/**
 * {@link List} implementation which is dynamically backed by the service registry. It
 * implements {@link ServiceRegistryEventListener} so that it can pick up and
 * respond to changes in the service registry. By default, uses
 * {@link LdapServiceReferenceFilter} to filter out relevant service entries
 * from the service registry. Alternatively, a {@link ServiceReferenceFilter}
 * can be wired in directly.
 * 
 * An entry is eligible for contribution to this list if it matches the
 * {@link ServiceReferenceFilter} associated with this instance. Each time a new
 * item is added or removed from the list is sorted.
 * 
 * All direct mutation methods from the {@link List} throw
 * {@link UnsupportedOperationException}. Read-only methods delegate directly to
 * the underlying private {@link List} instance.
 * 
 * @see BaseServiceRegistryMap
 * @author Phil Zoio
 */
@SuppressWarnings("unchecked")
public abstract class BaseServiceRegistryList extends BaseServiceRegistryTarget implements List {
    
    private static Log logger = LogFactory.getLog(BaseServiceRegistryList.class);
    
    private Object lock = new Object();
    
    //using unsynchronized list as synchronisation is within code
    /**
     * Holds contributions, typically proxied objects
     */
    private List contributions = new ArrayList();
    
    /**
     * Holds list of {@link ServiceRegistryEntry}
     */
    private List<ServiceRegistryEntry> services = new ArrayList<ServiceRegistryEntry>();
    
    /**
     * Holds identify mapping of beans to proxies, to remove need for proxy recreation 
     * when bean is repopulated
     */
    private Map<Object,Object> proxyMap = new IdentityHashMap<Object, Object>();

    /**
     * Used to sort contributions each time repopulation occurs
     */
    private ServiceReferenceSorter sorter = new ServiceReferenceSorter();
    
    /* ******************* Implementation of ServiceRegistryNotifiable ******************** */

    public boolean add(ServiceRegistryEntry entry) {
        
        synchronized (lock) {
            if (!this.services.contains(entry)) {
                
                List<ServiceRegistryEntry> services = new ArrayList<ServiceRegistryEntry>(this.services);
                //add the reference and sort
                
                services.add(entry);
                sortAndRepopulate(services);

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
            List<ServiceRegistryEntry> services = new ArrayList<ServiceRegistryEntry>(this.services);
            boolean removedRef = services.remove(entry);
            
            proxyMap.remove(entry.getServiceBeanReference().getService());
            
            if (removedRef) {
                sortAndRepopulate(services); 
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

    private void sortAndRepopulate(List<ServiceRegistryEntry> services) {
        this.services.clear();
        this.contributions.clear();
        this.proxyMap.clear();

        //repopulate the services
        List<ServiceRegistryEntry> sorted = sorter.sort(services, true);
        this.services.addAll(sorted);
        
        //repopulate the contributions list
        for (ServiceRegistryEntry entry : sorted) {
            Object bean = entry.getServiceBeanReference().getService();
            
            Object proxyObject = proxyMap.get(bean);
            if (proxyObject == null) {
                proxyObject = maybeGetProxy(entry);
                proxyMap.put(bean, proxyObject);
            }
            
            this.contributions.add(proxyObject);
        }
    }

    public boolean contains(Object object) {
        return contributions.contains(object);
    }

    public boolean containsAll(Collection collections) {
        return contributions.containsAll(collections);
    }

    public Object get(int index) {
        return contributions.get(index);
    }

    public int indexOf(Object object) {
        return contributions.indexOf(object);
    }

    public boolean isEmpty() {
        return contributions.isEmpty();
    }

    public Iterator iterator() {
        return contributions.iterator();
    }

    public int lastIndexOf(Object object) {
        return contributions.lastIndexOf(object);
    }

    public ListIterator listIterator() {
        return contributions.listIterator();
    }

    public ListIterator listIterator(int index) {
        return contributions.listIterator(index);
    }

    public int size() {
        return contributions.size();
    }

    public List subList(int fromIndex, int toIndex) {
        return contributions.subList(fromIndex, toIndex);
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

    public void add(int index, Object object) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection collection) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(int index, Collection collection) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean remove(Object object) {
        throw new UnsupportedOperationException();
    }

    public Object remove(int index) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection collection) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection collection) {
        throw new UnsupportedOperationException();
    }

    public Object set(int index, Object object) {
        throw new UnsupportedOperationException();
    }    

    /* ******************* protected methods ******************** */

    protected List<ServiceRegistryEntry> getServices() {
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
