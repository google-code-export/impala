package org.impalaframework.service.registry.filter;

import java.util.LinkedList;

import org.impalaframework.service.registry.BasicServiceRegistryReference;
import org.impalaframework.service.registry.event.ServiceReferenceFilter;

import junit.framework.TestCase;

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
		
		composite.setMatchAny(false);
		assertFalse(composite.matches(new BasicServiceRegistryReference("value1", "beanName", "moduleName")));
		  
		composite.setFilters(list);
		assertFalse(composite.matches(new BasicServiceRegistryReference("value1", "beanName", "moduleName")));
		assertFalse(composite.matches(new BasicServiceRegistryReference(new Integer(1), "beanName", "moduleName")));

		composite.setMatchAny(true);
		assertTrue(composite.matches(new BasicServiceRegistryReference("value1", "beanName", "moduleName")));
		assertTrue(composite.matches(new BasicServiceRegistryReference(new Integer(1), "beanName", "moduleName")));
	}
}
