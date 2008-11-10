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

package org.impalaframework.osgi.extender;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.net.URL;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.osgi.startup.OsgiContextStarter;
import org.osgi.framework.BundleContext;

public class ImpalaActivatorTest extends TestCase {

	private BundleContext bundleContext;
	private OsgiContextStarter contextStarter;
	private TestActivator activator;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		bundleContext = createMock(BundleContext.class);
		contextStarter = createMock(OsgiContextStarter.class);
		activator = new TestActivator(contextStarter);
	}

	@SuppressWarnings("unchecked")
	public void testActivate() throws Exception {
		contextStarter.setBundleContext(bundleContext);	
		expect(contextStarter.startContext((List<String>) isA(Object.class))).andReturn(null);
		replayMocks();
		
		activator.start(bundleContext);
		
		verifyMocks();
	}
	
	private void replayMocks() {
		replay(bundleContext);
		replay(contextStarter);
	}
	
	private void verifyMocks() {
		verify(bundleContext);
		verify(contextStarter); 
	}

}

class TestActivator extends ImpalaActivator {

	private OsgiContextStarter contextStarter;

	public TestActivator(OsgiContextStarter contextStarter) {
		super();
		this.contextStarter = contextStarter;
	}

	@Override
	OsgiContextStarter newContextStarter() {
		return contextStarter;
	}

	@Override
	URL getBootstrapLocationsResourceURL(BundleContext bundleContext) {
		return this.getClass().getClassLoader().getResource("impala.properties");
	}
}
