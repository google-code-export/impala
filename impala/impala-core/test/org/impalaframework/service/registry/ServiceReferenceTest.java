package org.impalaframework.service.registry;

import java.util.Collections;

import junit.framework.TestCase;

public class ServiceReferenceTest extends TestCase {

	public void testConstruct() throws Exception {
		ServiceReference serviceReference = new ServiceReference("service1","beanName",
				"moduleName");
		assertEquals(0, serviceReference.getTags().size());
		assertEquals(0, serviceReference.getAttributes().size());
	}
	
	public void testConstructTags() throws Exception {
		ServiceReference serviceReference = new ServiceReference("service1","beanName",
				"moduleName",  Collections.singletonList("one"));
		assertNotNull(serviceReference.getTags());
		assertEquals(0, serviceReference.getAttributes().size());
	}
	
	public void testConstructAttributes() throws Exception {
		ServiceReference serviceReference = new ServiceReference("service1","beanName",
				"moduleName", Collections.singletonMap("attribute","value"));
		assertEquals(0, serviceReference.getTags().size());
		assertEquals(1, serviceReference.getAttributes().size());
	}

	public void testConstructTagsAttributes() throws Exception {
		ServiceReference serviceReference = new ServiceReference("service1","beanName",
				"moduleName", Collections.singletonList("one"), Collections.singletonMap("attribute","value"));
		assertEquals(1, serviceReference.getTags().size());
		assertEquals(1, serviceReference.getAttributes().size());
	}

	public void testConstructTagsAttributesNull() throws Exception {
		ServiceReference serviceReference = new ServiceReference("service1","beanName",
				"moduleName", null, null);
		assertEquals(0, serviceReference.getTags().size());
		assertEquals(0, serviceReference.getAttributes().size());
	}	
}
