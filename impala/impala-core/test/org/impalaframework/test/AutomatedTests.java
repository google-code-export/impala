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

package org.impalaframework.test;

import org.impalaframework.classloader.CompositeClassLoaderTest;
import org.impalaframework.file.handler.DefaultClassFilterTest;
import org.impalaframework.file.monitor.FileMonitorImplTest;
import org.impalaframework.plugin.MissingBeanTest;
import org.impalaframework.plugin.SpringContextHolderTest;
import org.impalaframework.plugin.SystemPropertyBasedPlaceholderConfigurerTest;
import org.impalaframework.plugin.beanset.BeanSetMapReaderTest;
import org.impalaframework.plugin.beanset.BeanSetPropertiesReaderTest;
import org.impalaframework.plugin.beanset.BeansetApplicationPluginLoaderTest;
import org.impalaframework.plugin.beanset.ImportingBeanSetTest;
import org.impalaframework.plugin.beanset.SimpleBeansetAwarePluginTest;
import org.impalaframework.plugin.monitor.BasePluginModificationListenerTest;
import org.impalaframework.plugin.monitor.ScheduledPluginMonitorTest;
import org.impalaframework.plugin.plugin.ApplicationPluginLoaderTest;
import org.impalaframework.plugin.plugin.BasePluginLoaderTest;
import org.impalaframework.plugin.plugin.ChildSpecContainerTest;
import org.impalaframework.plugin.plugin.ManualReloadingParentPluginLoaderTest;
import org.impalaframework.plugin.plugin.ParentPluginLoaderTest;
import org.impalaframework.plugin.plugin.ParentWithChildContextTest;
import org.impalaframework.plugin.plugin.PluginBeanPostProcessorTest;
import org.impalaframework.plugin.plugin.PluginLoaderRegistryTest;
import org.impalaframework.plugin.plugin.PluginProxyFactoryBeanTest;
import org.impalaframework.plugin.plugin.PluginUtilsTest;
import org.impalaframework.plugin.plugin.SimpleParentContextTest;
import org.impalaframework.plugin.plugin.SimpleParentSpecTest;
import org.impalaframework.plugin.plugin.SimplePluginSpecTest;
import org.impalaframework.plugin.plugin.SimplePluginTargetSourceTest;
import org.impalaframework.plugin.plugin.SimpleSpringContextTest;
import org.impalaframework.plugin.shared.CustomClassLoaderTest;
import org.impalaframework.plugin.shared.ParentClassLoaderTest;
import org.impalaframework.resolver.PropertyClassLocationResolverTest;
import org.impalaframework.resolver.StandaloneClassLocationResolverFactoryTest;
import org.impalaframework.util.FileUtilsTest;
import org.impalaframework.util.MemoryUtilsTest;
import org.impalaframework.util.PathUtilsTest;
import org.impalaframework.util.PropertyUtilsTest;
import org.impalaframework.util.ResourceUtilsTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Phil Zoio
 */
public class AutomatedTests {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(SystemPropertyBasedPlaceholderConfigurerTest.class);
		suite.addTestSuite(ApplicationPluginLoaderTest.class);
		suite.addTestSuite(BasePluginLoaderTest.class);	
		suite.addTestSuite(BasePluginModificationListenerTest.class);	
		suite.addTestSuite(BeanSetMapReaderTest.class);	
		suite.addTestSuite(BeanSetPropertiesReaderTest.class);
		suite.addTestSuite(ChildSpecContainerTest.class);	
		suite.addTestSuite(CompositeClassLoaderTest.class);
		suite.addTestSuite(CustomClassLoaderTest.class);	
		suite.addTestSuite(DefaultClassFilterTest.class);
		suite.addTestSuite(FileMonitorImplTest.class);
		suite.addTestSuite(FileUtilsTest.class);
		suite.addTestSuite(ImportingBeanSetTest.class);
		suite.addTestSuite(ManualReloadingParentPluginLoaderTest.class);
		suite.addTestSuite(MemoryUtilsTest.class);
		suite.addTestSuite(MissingBeanTest.class);
		suite.addTestSuite(ParentWithChildContextTest.class);
		suite.addTestSuite(ParentClassLoaderTest.class);
		suite.addTestSuite(ParentPluginLoaderTest.class);
		suite.addTestSuite(PathUtilsTest.class);
		suite.addTestSuite(PluginBeanPostProcessorTest.class);
		suite.addTestSuite(PluginLoaderRegistryTest.class);
		suite.addTestSuite(PluginUtilsTest.class);
		suite.addTestSuite(PluginProxyFactoryBeanTest.class);
		suite.addTestSuite(PropertyClassLocationResolverTest.class);
		suite.addTestSuite(PropertyUtilsTest.class);
		suite.addTestSuite(ResourceUtilsTest.class);
		suite.addTestSuite(ScheduledPluginMonitorTest.class);
		suite.addTestSuite(SimpleBeansetAwarePluginTest.class);
		suite.addTestSuite(SimpleParentContextTest.class);
		suite.addTestSuite(SimpleSpringContextTest.class);
		suite.addTestSuite(SimpleParentSpecTest.class);
		suite.addTestSuite(SimplePluginSpecTest.class);
		suite.addTestSuite(SimplePluginTargetSourceTest.class);
		suite.addTestSuite(SpringContextHolderTest.class);
		suite.addTestSuite(StandaloneClassLocationResolverFactoryTest.class);
		suite.addTestSuite(BeansetApplicationPluginLoaderTest.class);


		return suite;
	}
}
