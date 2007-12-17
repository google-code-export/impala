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

package org.impalaframework.module.spec;

import org.impalaframework.module.spec.ChildSpecContainer;
import org.impalaframework.module.spec.ChildSpecContainerImpl;
import org.impalaframework.module.spec.PluginSpec;
import org.impalaframework.module.spec.SimplePluginSpec;

import junit.framework.TestCase;

/**
 * @author Phil Zoio
 */
public class ChildSpecContainerTest extends TestCase {

	public void testChildSpecContainer() {
		final PluginSpec[] strings = new PluginSpec[] { new SimplePluginSpec("p1"), new SimplePluginSpec("p2") };
		ChildSpecContainer spec = new ChildSpecContainerImpl(strings);

		assertTrue(spec.hasPlugin("p1"));
		assertTrue(spec.hasPlugin("p2"));
		assertFalse(spec.hasPlugin("p3"));

		assertEquals(2, spec.getPluginNames().size());
		
		//fail("To implement add() and remove() tests");
	}

}
