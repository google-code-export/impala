package org.impalaframework.service.registry.contribution;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.impalaframework.service.registry.ServiceRegistryImpl;


public class ContributionMapTest extends TestCase {

	private ContributionMap<String, String> map;
	private ServiceRegistryImpl registry;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		map = new ContributionMap<String, String>();
		registry = new ServiceRegistryImpl();
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
		map.setTagName("tag");
		
		registry.addEventListener(map);

		String service1 = "some service1";
		String service2 = "some service2";
		
		registry.addService("bean1", "module1", service1, Collections.singletonList("tag"), Collections.singletonMap("contributedBeanName", "bean1"));
		registry.addService("bean2", "module1", service2, Collections.singletonList("tag"), Collections.singletonMap("contributedBeanName", "bean2"));
		assertEquals(2, map.getExternalContributions().size());
		registry.remove(service1);
		registry.remove(service2);		
		assertEquals(0, map.getExternalContributions().size());
	}
	
	public void testMapListener() {
		map.setTagName("tag");
		
		registry.addEventListener(map);

		String service1 = "value1";
		String service2 = "value2";
		
		registry.addService("bean1", "module1", service1, Collections.singletonList("tag"), Collections.singletonMap("contributedBeanName", "bean1"));
		registry.addService("bean2", "module1", service2, Collections.singletonList("tag"), Collections.singletonMap("contributedBeanName", "bean2"));
		assertEquals(2, map.getExternalContributions().size());

		Map<String,String> m = new HashMap<String, String>();
		m.put("bean2", "value2a");
		map.putAll(m);
		map.put("bean3", "value3");
		
		System.out.println(map);
		
		assertTrue(map.containsKey("bean1"));
		assertTrue(map.containsKey("bean2"));
		assertTrue(map.containsKey("bean3"));
		
		assertTrue(map.containsValue("value1"));
		assertTrue(map.containsValue("value2"));
		assertTrue(map.containsValue("value2a"));
		assertTrue(map.containsValue("value3"));
		
		assertNotNull(map.get("bean1"));
		assertNotNull(map.get("bean2"));
		assertNotNull(map.get("bean3"));
		assertFalse(map.entrySet().isEmpty());
		assertFalse(map.keySet().isEmpty());
		assertFalse(map.isEmpty());
		assertFalse(map.values().isEmpty());
		assertEquals(4, map.size());
		assertNotNull(map.remove("bean3"));
		assertEquals(3, map.size());
	}
	
}
