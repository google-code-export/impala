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

import org.impalaframework.module.web.ConfigurableWebXmlBasedContextLoaderTest;
import org.impalaframework.module.web.ContextLoaderIntegrationTest;
import org.impalaframework.module.web.DefaultBootstrapLocationResolutionStrategyTest;
import org.impalaframework.module.web.ExternalBootstrapLocationResolutionStrategyTest;
import org.impalaframework.module.web.ExternalXmlBasedImpalaContextLoaderTest;
import org.impalaframework.module.web.ImpalaContextLoaderListenerTest;
import org.impalaframework.module.web.ImpalaContextLoaderTest;
import org.impalaframework.module.web.ImpalaPluginServletTest;
import org.impalaframework.module.web.ImpalaRootServletTest;
import org.impalaframework.module.web.ServletPluginLoaderTest;
import org.impalaframework.module.web.WebPlaceholderDelegatingContextLoaderTest;
import org.impalaframework.module.web.WebPlaceholderPluginSpecTest;
import org.impalaframework.module.web.WebPluginLoaderTest;
import org.impalaframework.module.web.WebPluginModificationListenerTest;
import org.impalaframework.module.web.WebXmlPluginSpecBuilderTest;

public class AutomatedWebTests {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(ConfigurableWebXmlBasedContextLoaderTest.class);
		suite.addTestSuite(ContextLoaderIntegrationTest.class);
		suite.addTestSuite(DefaultBootstrapLocationResolutionStrategyTest.class);
		suite.addTestSuite(ExternalBootstrapLocationResolutionStrategyTest.class);
		suite.addTestSuite(ExternalXmlBasedImpalaContextLoaderTest.class);
		suite.addTestSuite(ImpalaContextLoaderTest.class);
		suite.addTestSuite(ImpalaContextLoaderListenerTest.class);
		suite.addTestSuite(ImpalaPluginServletTest.class);
		suite.addTestSuite(ImpalaRootServletTest.class);
		suite.addTestSuite(ServletPluginLoaderTest.class);
		suite.addTestSuite(WebPlaceholderDelegatingContextLoaderTest.class);
		suite.addTestSuite(WebPlaceholderPluginSpecTest.class);
		suite.addTestSuite(WebPluginLoaderTest.class);
		suite.addTestSuite(WebPluginModificationListenerTest.class);
		suite.addTestSuite(WebXmlPluginSpecBuilderTest.class);
		return suite;
	}
}
