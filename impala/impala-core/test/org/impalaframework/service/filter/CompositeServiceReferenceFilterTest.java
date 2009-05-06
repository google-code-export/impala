package org.impalaframework.service.filter;

import java.util.LinkedList;

import junit.framework.TestCase;

import org.impalaframework.service.ServiceReferenceFilter;
import org.impalaframework.service.reference.StaticServiceRegistryEntry;
import org.springframework.util.ClassUtils;

public class CompositeServiceReferenceFilterTest extends TestCase {
    
    public void testMatches() {
        
        CompositeServiceReferenceFilter composite = new CompositeServiceReferenceFilter();
        TypeServiceReferenceFilter filter1 = new TypeServiceReferenceFilter();
        filter1.setType(String.class);
        TypeServiceReferenceFilter filter2 = new TypeServiceReferenceFilter();
        filter2.setType(Integer.class);
        LinkedList<ServiceReferenceFilter> list = new LinkedList<ServiceReferenceFilter>();
        list.add(filter1);
        list.add(filter2);
        
        ClassLoader loader = ClassUtils.getDefaultClassLoader();
        composite.setMatchAny(false);
        assertFalse(composite.matches(new StaticServiceRegistryEntry("value1", "beanName", "moduleName", loader)));
          
        composite.setFilters(list);
        assertFalse(composite.matches(new StaticServiceRegistryEntry("value1", "beanName", "moduleName", loader)));
        assertFalse(composite.matches(new StaticServiceRegistryEntry(new Integer(1), "beanName", "moduleName", loader)));

        composite.setMatchAny(true);
        assertTrue(composite.matches(new StaticServiceRegistryEntry("value1", "beanName", "moduleName", loader)));
        assertTrue(composite.matches(new StaticServiceRegistryEntry(new Integer(1), "beanName", "moduleName", loader)));
    }
}
