package org.impalaframework.service.registry;
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
	
}
