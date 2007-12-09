/*
 * Copyright 2006-2007 the original author or authors.
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

package org.impalaframework.web.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.impalaframework.plugin.web.ConfigurableImpalaContextLoaderTest;
import org.impalaframework.plugin.web.ContextLoaderIntegrationTest;
import org.impalaframework.plugin.web.DefaultBootstrapLocationResolutionStrategyTest;
import org.impalaframework.plugin.web.ExternalBootstrapLocationResolutionStrategyTest;
import org.impalaframework.plugin.web.ExternalXmlBasedImpalaContextLoaderTest;
import org.impalaframework.plugin.web.ImpalaContextLoaderTest;
import org.impalaframework.plugin.web.ImpalaPluginServletTest;
import org.impalaframework.plugin.web.ImpalaRootServletTest;
import org.impalaframework.plugin.web.RegistryBasedImpalaContextLoaderTest;
import org.impalaframework.plugin.web.ServletPluginLoaderTest;
import org.impalaframework.plugin.web.WebPluginLoaderTest;
import org.impalaframework.plugin.web.WebPluginModificationListenerTest;
import org.impalaframework.plugin.web.WebXmlPluginSpecBuilderTest;

public class AutomatedWebTests {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(ConfigurableImpalaContextLoaderTest.class);
		suite.addTestSuite(ContextLoaderIntegrationTest.class);
		suite.addTestSuite(DefaultBootstrapLocationResolutionStrategyTest.class);
		suite.addTestSuite(ExternalBootstrapLocationResolutionStrategyTest.class);
		suite.addTestSuite(ExternalXmlBasedImpalaContextLoaderTest.class);
		suite.addTestSuite(ImpalaContextLoaderTest.class);
		suite.addTestSuite(ImpalaPluginServletTest.class);
		suite.addTestSuite(ImpalaRootServletTest.class);
		suite.addTestSuite(RegistryBasedImpalaContextLoaderTest.class);
		suite.addTestSuite(ServletPluginLoaderTest.class);
		suite.addTestSuite(WebPluginLoaderTest.class);
		suite.addTestSuite(WebPluginModificationListenerTest.class);
		suite.addTestSuite(WebXmlPluginSpecBuilderTest.class);
		return suite;
	}
}
