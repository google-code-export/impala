package org.impalaframework.service.registry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.impalaframework.service.registry.contribution.ContributionMap;


public class ContributionMapTest extends TestCase {

	private ContributionMap<String, String> map;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		map = new ContributionMap<String, String>();
	}
	
	public void testEmpty() throws Exception {
		assertFalse(map.containsKey("key"));
		assertFalse(map.containsValue("value"));
		assertNull(map.get("key"));
		assertTrue(map.entrySet().isEmpty());
		assertTrue(map.keySet().isEmpty());
		assertTrue(map.isEmpty());
		assertTrue(map.values().isEmpty());
		assertEquals(0, map.size());
		assertNull(map.remove("key"));
	}
	
	public void testWithValues() throws Exception {
		map.put("key", "value");
		assertTrue(map.containsKey("key"));
		assertTrue(map.containsValue("value"));
		assertNotNull(map.get("key"));
		assertFalse(map.entrySet().isEmpty());
		assertFalse(map.keySet().isEmpty());
		assertFalse(map.isEmpty());
		assertFalse(map.values().isEmpty());
		assertEquals(1, map.size());
		assertNotNull(map.remove("key"));
		
		Map<String,String> m = new HashMap<String, String>();
		m.put("a'", "b");
		map.putAll(m);
		assertEquals(1, map.size());
	}
	
	public void testListener() {
		ServiceRegistryImpl registry = new ServiceRegistryImpl();
		ContributionMap<String, String> listener1 = new ContributionMap<String, String>();
		listener1.setTagName("tag");
		
		registry.addEventListener(listener1);

		String service1 = "some service1";
		String service2 = "some service2";
		
		registry.addService("bean1", "module1", service1, Collections.singletonList("tag"), Collections.singletonMap("contributedBeanName", "bean1"));
		registry.addService("bean2", "module1", service2, Collections.singletonList("tag"), Collections.singletonMap("contributedBeanName", "bean2"));
		
		registry.remove(service1);
		registry.remove(service2);
		
	}
	
}
