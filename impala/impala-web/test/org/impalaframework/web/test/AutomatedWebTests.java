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
import org.impalaframework.web.helper.ImpalaServletUtilsTest;
import org.impalaframework.web.integration.ExternalFrameworkIntegrationServletTest;
import org.impalaframework.web.integration.FilterFactoryBeanTest;
import org.impalaframework.web.integration.IntegrationFilterConfigTest;
import org.impalaframework.web.integration.InternalFrameworkIntegrationFilterTest;
import org.impalaframework.web.integration.InternalFrameworkIntegrationServletFactoryBeanTest;
import org.impalaframework.web.integration.InternalFrameworkIntegrationServletTest;
import org.impalaframework.web.integration.ModuleProxyFilterTest;
import org.impalaframework.web.integration.ModuleProxyServletTest;
import org.impalaframework.web.integration.ModuleProxyUtilsTest;
import org.impalaframework.web.integration.ServletFactoryBeanTest;
import org.impalaframework.web.loader.BaseImpalaContextLoaderTest;
import org.impalaframework.web.loader.ContextLoaderIntegrationTest;
import org.impalaframework.web.loader.ExternalContextLoaderTest;
import org.impalaframework.web.loader.ExternalModuleContextLoaderTest;
import org.impalaframework.web.loader.ImpalaContextLoaderListenerTest;
import org.impalaframework.web.loader.WebPlaceholderDelegatingContextLoaderTest;
import org.impalaframework.web.module.WebModuleLoaderTest;
import org.impalaframework.web.module.WebModuleChangeListenerTest;
import org.impalaframework.web.module.WebModuleReloaderTest;
import org.impalaframework.web.module.WebPlaceholderModuleDefinitionTest;
import org.impalaframework.web.module.WebRootModuleDefinitionTest;
import org.impalaframework.web.module.WebRootModuleLoaderTest;
import org.impalaframework.web.module.WebXmlRootDefinitionBuilderTest;
import org.impalaframework.web.module.path.ServletPathRequestModuleMapperTest;
import org.impalaframework.web.resolver.ServletContextModuleLocationResolverTest;
import org.impalaframework.web.resource.ServletContextResourceLoaderTest;
import org.impalaframework.web.servlet.ExternalModuleServletTest;
import org.impalaframework.web.servlet.invoker.ServletInvokerUtilsTest;
import org.impalaframework.web.servlet.invoker.ThreadContextClassLoaderHttpServiceInvokerTest;
import org.impalaframework.web.servlet.wrapper.HttpRequestWrapperFactoryTest;
import org.impalaframework.web.servlet.wrapper.ModuleAwareWrapperHttpServletRequestTest;
import org.impalaframework.web.servlet.wrapper.ModuleAwareWrapperHttpSessionTest;
import org.impalaframework.web.servlet.wrapper.ModuleAwareWrapperServletContextTest;
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
		suite.addTestSuite(ExternalFrameworkIntegrationServletTest.class);
		suite.addTestSuite(ExternalModuleServletTest.class);
		suite.addTestSuite(ExternalModuleContextLoaderTest.class);
		suite.addTestSuite(FilterFactoryBeanTest.class);
		suite.addTestSuite(FrameworkServletContextCreatorTest.class);
		suite.addTestSuite(HttpRequestWrapperFactoryTest.class);
		suite.addTestSuite(ImpalaContextLoaderListenerTest.class);
		suite.addTestSuite(ImpalaServletUtilsTest.class);
		suite.addTestSuite(IntegrationFilterConfigTest.class);
		suite.addTestSuite(InternalFrameworkIntegrationFilterTest.class);
		suite.addTestSuite(InternalFrameworkIntegrationServletFactoryBeanTest.class);
		suite.addTestSuite(InternalFrameworkIntegrationServletTest.class);
		suite.addTestSuite(InternalFrameworkIntegrationServletFactoryBeanTest.class);
		suite.addTestSuite(ModuleAwareWrapperHttpServletRequestTest.class);
		suite.addTestSuite(ModuleAwareWrapperHttpSessionTest.class);
		suite.addTestSuite(ModuleAwareWrapperServletContextTest.class);
		suite.addTestSuite(ModuleProxyFilterTest.class);
		suite.addTestSuite(ModuleProxyServletTest.class);
		suite.addTestSuite(ModuleProxyUtilsTest.class);
		suite.addTestSuite(ServletFactoryBeanTest.class);
		suite.addTestSuite(ServletContextModuleLocationResolverTest.class);
		suite.addTestSuite(ServletContextResourceLoaderTest.class);
		suite.addTestSuite(ServletInvokerUtilsTest.class);
		suite.addTestSuite(ServletPathRequestModuleMapperTest.class);
		suite.addTestSuite(SystemPropertyServletContextParamFactoryBeanTest.class);
		suite.addTestSuite(ThreadContextClassLoaderHttpServiceInvokerTest.class);
		suite.addTestSuite(WebPlaceholderDelegatingContextLoaderTest.class);
		suite.addTestSuite(WebPlaceholderModuleDefinitionTest.class);
		suite.addTestSuite(WebRootModuleLoaderTest.class);
		suite.addTestSuite(WebRootModuleDefinitionTest.class);
		suite.addTestSuite(WebModuleChangeListenerTest.class);
		suite.addTestSuite(WebModuleLoaderTest.class);
		suite.addTestSuite(WebModuleReloaderTest.class);
		suite.addTestSuite(WebTypeReaderRegistryFactoryBeanTest.class);
		suite.addTestSuite(WebXmlRootDefinitionBuilderTest.class);
		return suite;
	}
}
