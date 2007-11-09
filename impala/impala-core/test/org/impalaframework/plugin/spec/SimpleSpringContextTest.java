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

package org.impalaframework.plugin.spec;

import junit.framework.TestCase;

import org.impalaframework.plugin.builder.SimplePluginSpecBuilder;

/**
 * @author Phil Zoio
 */
public class SimpleSpringContextTest extends TestCase {

	public void testHasPlugin() {
		SimplePluginSpecBuilder spec = new SimplePluginSpecBuilder(new String[] { "l0", "l1", "l2" }, new String[] { "p1", "p2" });
		
		assertNotNull(spec);
		final ParentSpec root = spec.getParentSpec();
		assertEquals(3, root.getContextLocations().size());
		
		assertTrue(root.hasPlugin("p1"));
		assertTrue(root.hasPlugin("p2"));
		assertFalse(root.hasPlugin("p3"));
		
		assertEquals(2, root.getPluginNames().size());
	}

}
