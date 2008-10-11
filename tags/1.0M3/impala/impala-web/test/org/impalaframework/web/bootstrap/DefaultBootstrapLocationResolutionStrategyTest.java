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

package org.impalaframework.web.bootstrap;

import org.impalaframework.web.bootstrap.DefaultBootstrapLocationResolutionStrategy;

import junit.framework.TestCase;

public class DefaultBootstrapLocationResolutionStrategyTest extends TestCase {
	
	private DefaultBootstrapLocationResolutionStrategy strategy;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		strategy = new DefaultBootstrapLocationResolutionStrategy();
	}
	
	public final void testGetBootstrapContextLocations() {
		String[] locations = strategy.getBootstrapContextLocations(null);
		assertEquals(2, locations.length);
		assertEquals("META-INF/impala-bootstrap.xml", locations[0]);
	}

}
