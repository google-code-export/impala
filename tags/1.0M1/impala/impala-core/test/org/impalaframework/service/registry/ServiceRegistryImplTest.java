package org.impalaframework.service.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.service.registry.event.ServiceAddedEvent;
import org.impalaframework.service.registry.event.ServiceRegistryEvent;
import org.impalaframework.service.registry.event.ServiceRegistryEventListener;
import org.impalaframework.service.registry.event.ServiceRemovedEvent;

public class ServiceRegistryImplTest extends TestCase {

	private ServiceRegistryImpl registry;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		registry = new ServiceRegistryImpl();
	}

	public void testRegistry() throws Exception {
		assertNull(registry.getService("notregistered"));
		assertNull(registry.getService("notregistered", String.class));

		registry.addService("bean1", "module1", "some service");

		ServiceReference service = registry.getService("bean1");
		assertEquals("some service", service.getBean());
		assertEquals("module1", service.getContributingModule());
		assertSame(service, registry.getService("bean1", String.class));
		assertSame(service, registry.getService("bean1", CharSequence.class));

		registry.remove("some service");
		assertNull(registry.getService("bean1"));
	}
	
	public void testListener() {
		TestListener listener1 = new TestListener();
		TestListener listener2 = new TestListener();
		registry.addEventListener(listener1);
		registry.addEventListener(listener2);

		String service1 = "some service1";
		String service2 = "some service2";
		
		registry.addService("bean1", "module1", service1);
		registry.addService("bean2", "module2", service2);
		registry.remove(service2);
		
		assertEquals(3, listener1.getEvents().size());
		assertEquals(3, listener2.getEvents().size());
		assertTrue(listener1.getEvents().get(0) instanceof ServiceAddedEvent);
		assertTrue(listener2.getEvents().get(0) instanceof ServiceAddedEvent);
		assertTrue(listener1.getEvents().get(2) instanceof ServiceRemovedEvent);
		assertTrue(listener2.getEvents().get(2) instanceof ServiceRemovedEvent);
	}

	public void testTaggedListener() {
		TestListener listener1 = new TestListener();
		TestListener listener2 = new TestListener();
		registry.addEventListener("tag1", listener1);
		registry.addEventListener("tag1", listener2);
	
		String service1 = "some service1";
		String service2 = "some service2";
		
		registry.addService("bean1", "module1", service1, Collections.singletonList("tag1"));
		registry.addService("bean2", "module2", service2, Collections.singletonList("tag1"));
		registry.remove(service2);
		
		assertEquals(3, listener1.getEvents().size());
		assertEquals(3, listener2.getEvents().size());
		assertTrue(listener1.getEvents().get(0) instanceof ServiceAddedEvent);
		assertTrue(listener2.getEvents().get(0) instanceof ServiceAddedEvent);
		assertTrue(listener1.getEvents().get(2) instanceof ServiceRemovedEvent);
		assertTrue(listener2.getEvents().get(2) instanceof ServiceRemovedEvent);		
	}
	
}

class TestListener implements ServiceRegistryEventListener {

	private List<ServiceRegistryEvent> events = new ArrayList<ServiceRegistryEvent>();

	public void handleServiceRegistryEvent(ServiceRegistryEvent event) {
		events.add(event);
	}

	public void reset() {
		events.clear();
	}

	public List<ServiceRegistryEvent> getEvents() {
		return events;
	}

}
