package net.java.impala.spring.plugin;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class SimpleBeansetAwarePluginTest extends TestCase {

	public void testEqualsObject() {
		Map<String, String> map1 = new HashMap<String, String>();
		SimpleBeansetAwarePlugin p1a = new SimpleBeansetAwarePlugin("p1", map1);
		Map<String, String> map2 = new HashMap<String, String>();
		SimpleBeansetAwarePlugin p1b = new SimpleBeansetAwarePlugin("p1", map2);
		assertEquals(p1a, p1b);

		SimpleBeansetAwarePlugin p2b = new SimpleBeansetAwarePlugin("p2", map2);
		assertFalse(p1b.equals(p2b));
		
		map1.put("bean1", "context1-a.xml");
		map1.put("bean2", "context2-a.xml");
		
		map2.put("bean1", "context1-a.xml");
		map2.put("bean2", "context2-a.xml");
		
		//these contain the same overrides, so use these
		p1a = new SimpleBeansetAwarePlugin("p1", map1);
	    p1b = new SimpleBeansetAwarePlugin("p1", map2);
		assertEquals(p1a, p1b);
		
		//now change bean2 in map2
		map2.put("bean2", "context2-b.xml");
	    p1b = new SimpleBeansetAwarePlugin("p1", map2);
		assertFalse(p1b.equals(p2b));
		
		map2.remove("bean2");
	    p1b = new SimpleBeansetAwarePlugin("p1", map2);
		assertFalse(p1b.equals(p2b));
	}

}
