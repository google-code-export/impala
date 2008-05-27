package org.impalaframework.service.registry.filter;

import junit.framework.TestCase;

import org.impalaframework.service.registry.BasicServiceRegistryReference;

public class TypeServiceReferenceFilterTest extends TestCase {

	public void testMatches() {
		SingleTypeServiceReferenceFilter filter = new SingleTypeServiceReferenceFilter();
		assertFalse(filter.matches(new BasicServiceRegistryReference("value1", "beanName", "moduleName")));
		
		filter.setType(String.class);
		assertTrue(filter.matches(new BasicServiceRegistryReference("value1", "beanName", "moduleName")));
		assertFalse(filter.matches(new BasicServiceRegistryReference(new Integer(1), "beanName", "moduleName")));
	}

}
