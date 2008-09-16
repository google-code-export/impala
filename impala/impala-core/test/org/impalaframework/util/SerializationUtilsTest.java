package org.impalaframework.util;

import java.util.HashMap;

import junit.framework.TestCase;

public class SerializationUtilsTest extends TestCase {

	@SuppressWarnings("unchecked")
	public void testCloneSerializable() {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("key", new Integer(1));
		HashMap<String, Integer> clone = (HashMap<String, Integer>) SerializationUtils.clone(map);
		
		assertEquals(1, clone.get("key").intValue());
	}

}
