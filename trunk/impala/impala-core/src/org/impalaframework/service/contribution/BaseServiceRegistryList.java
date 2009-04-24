package org.impalaframework.service.contribution;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

import org.impalaframework.service.ServiceReferenceFilter;
import org.impalaframework.service.ServiceRegistryEventListener;
import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.service.filter.ldap.LdapServiceReferenceFilter;

/**
 * List implementation which is dynamically backed by the service registry. It
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
public class BaseServiceRegistryList extends BaseServiceRegistryTarget implements List {

    private List contributions = new CopyOnWriteArrayList();
    
    /* ******************* Implementation of ServiceRegistryNotifiable ******************** */

    public void add(ServiceRegistryReference ref) {
        //FIXME complete implementation
    }

    public void remove(ServiceRegistryReference ref) {
        //FIXME complete implementation
    }
    
    /* **************** List implementation *************** */

    //FIXME test these methods
    
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
