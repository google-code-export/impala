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

import org.impalaframework.web.bootstrap.AbridgedExternalBootstrapLocationResolutionStrategyTest;
import org.impalaframework.web.bootstrap.DefaultBootstrapLocationResolutionStrategyTest;
import org.impalaframework.web.bootstrap.ExternalBootstrapLocationResolutionStrategyTest;
import org.impalaframework.web.config.FrameworkServletContextCreatorTest;
import org.impalaframework.web.config.SystemPropertyServletContextParamFactoryBeanTest;
import org.impalaframework.web.integration.ModuleRedirectingServletTest;
import org.impalaframework.web.integration.ServletFactoryBeanTest;
import org.impalaframework.web.loader.BaseImpalaContextLoaderTest;
import org.impalaframework.web.loader.ContextLoaderIntegrationTest;
import org.impalaframework.web.loader.ExternalContextLoaderTest;
import org.impalaframework.web.loader.ExternalModuleContextLoaderTest;
import org.impalaframework.web.loader.ImpalaContextLoaderListenerTest;
import org.impalaframework.web.loader.WebPlaceholderDelegatingContextLoaderTest;
import org.impalaframework.web.module.ServletModuleLoaderTest;
import org.impalaframework.web.module.WebModuleChangeListenerTest;
import org.impalaframework.web.module.WebModuleReloaderTest;
import org.impalaframework.web.module.WebPlaceholderModuleDefinitionTest;
import org.impalaframework.web.module.WebRootModuleLoaderTest;
import org.impalaframework.web.module.WebXmlRootDefinitionBuilderTest;
import org.impalaframework.web.resolver.ServletContextModuleLocationResolverTest;
import org.impalaframework.web.resource.ServletContextResourceLoaderTest;
import org.impalaframework.web.servlet.ExternalModuleServletTest;
import org.impalaframework.web.servlet.WebModuleServletTest;
import org.impalaframework.web.servlet.WebRootModuleServletTest;
import org.impalaframework.web.type.WebTypeReaderRegistryFactoryBeanTest;

public class AutomatedWebTests {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(AbridgedExternalBootstrapLocationResolutionStrategyTest.class);
		suite.addTestSuite(BaseImpalaContextLoaderTest.class);
		suite.addTestSuite(ContextLoaderIntegrationTest.class);
		suite.addTestSuite(DefaultBootstrapLocationResolutionStrategyTest.class);
		suite.addTestSuite(ExternalBootstrapLocationResolutionStrategyTest.class);
		suite.addTestSuite(ExternalContextLoaderTest.class);
		suite.addTestSuite(ExternalModuleServletTest.class);
		suite.addTestSuite(ExternalModuleContextLoaderTest.class);
		suite.addTestSuite(FrameworkServletContextCreatorTest.class);
		suite.addTestSuite(ImpalaContextLoaderListenerTest.class);
		suite.addTestSuite(ModuleRedirectingServletTest.class);
		suite.addTestSuite(ServletFactoryBeanTest.class);
		suite.addTestSuite(ServletModuleLoaderTest.class);
		suite.addTestSuite(ServletContextModuleLocationResolverTest.class);
		suite.addTestSuite(ServletContextResourceLoaderTest.class);
		suite.addTestSuite(SystemPropertyServletContextParamFactoryBeanTest.class);
		suite.addTestSuite(WebPlaceholderDelegatingContextLoaderTest.class);
		suite.addTestSuite(WebPlaceholderModuleDefinitionTest.class);
		suite.addTestSuite(WebRootModuleLoaderTest.class);
		suite.addTestSuite(WebModuleChangeListenerTest.class);
		suite.addTestSuite(WebModuleReloaderTest.class);
		suite.addTestSuite(WebModuleServletTest.class);
		suite.addTestSuite(WebRootModuleServletTest.class);
		suite.addTestSuite(WebTypeReaderRegistryFactoryBeanTest.class);
		suite.addTestSuite(WebXmlRootDefinitionBuilderTest.class);
		return suite;
	}
}
