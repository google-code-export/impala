package org.impalaframework.service.registry;

import junit.framework.TestCase;

public class ServiceRegistryImplTest extends TestCase {
	public void testRegistry() throws Exception {
		ServiceRegistryImpl impl = new ServiceRegistryImpl();
		impl.addService("bean1", "module1", "some service");
		
		ServiceReference service = impl.getService("bean1");
		assertEquals("some service", service.getBean());
		assertEquals("module1", service.getContributingModule());
		
		impl.remove("bean1", "module1", "some service");
		assertNull(impl.getService("bean1"));
	}
}
