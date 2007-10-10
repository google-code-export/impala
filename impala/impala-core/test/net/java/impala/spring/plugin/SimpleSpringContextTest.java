/*
 * Copyright 2007 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package net.java.impala.spring.plugin;

import junit.framework.TestCase;

public class SimpleSpringContextTest extends TestCase {

	public void testHasPlugin() {
		SimpleSpringContextSpec spec = new SimpleSpringContextSpec(new String[] { "l0", "l1", "l2" }, new String[] { "p1", "p2" });
		
		assertNotNull(spec.getParentSpec());
		assertEquals(3, spec.getParentSpec().getContextLocations().length);
		
		assertTrue(spec.hasPlugin("p1"));
		assertTrue(spec.hasPlugin("p2"));
		assertFalse(spec.hasPlugin("p3"));
		
		assertEquals(2, spec.getPluginNames().size());
	}

}
