/*
 * Copyright 2007-2008 the original author or authors.
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

package org.impalaframework.spring.module;


import org.impalaframework.spring.module.SimpleContributionEndpointTargetSource;

import junit.framework.TestCase;

/**
 * @author Phil Zoio
 */
@Deprecated
//FIXME remove this from code base
public class SimpleContributionEndpointTargetSourceTest extends TestCase {

	public void testSimplePluginTargetSource() throws Exception {
		SimpleContributionEndpointTargetSource s = new SimpleContributionEndpointTargetSource();

		//non-state dependent assertions
		assertTrue(s.isStatic());
		assertNull(s.getTargetClass());
		
		//target not present
		assertFalse(s.hasTarget());
		assertNull(s.getTarget());

		//register object
		Object o = new Object();
		s.registerTarget(o);

		//target present
		assertTrue(s.hasTarget());
		assertEquals(o, s.getTarget());
		assertNull(s.getTargetClass());

		//release - not present
		s.releaseTarget(o);
		assertFalse(s.hasTarget());
		assertNull(s.getTarget());

		//register - present
		s.registerTarget(o);
		assertTrue(s.hasTarget());
		
		//deregister - not present
		s.deregisterTarget(o);
		assertFalse(s.hasTarget());
		assertNull(s.getTarget());
		

	}

}
