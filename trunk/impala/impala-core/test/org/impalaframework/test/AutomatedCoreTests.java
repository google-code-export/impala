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

import junit.framework.Test;
import junit.framework.TestSuite;

import org.impalaframework.bean.NamedFactoryBeanTest;
import org.impalaframework.classloader.CompositeClassLoaderTest;
import org.impalaframework.classloader.CustomClassLoaderTest;
import org.impalaframework.classloader.FileSystemPluginClassLoaderTest;
import org.impalaframework.file.handler.DefaultClassFilterTest;
import org.impalaframework.file.monitor.FileMonitorImplTest;
import org.impalaframework.plugin.beanset.BeanSetMapReaderTest;
import org.impalaframework.plugin.beanset.BeanSetPropertiesReaderTest;
import org.impalaframework.plugin.beanset.BeansetApplicationPluginLoaderTest;
import org.impalaframework.plugin.beanset.ImportingBeanSetTest;
import org.impalaframework.plugin.beanset.SimpleBeansetAwarePluginTest;
import org.impalaframework.plugin.bootstrap.BootstrapBeanFactoryTest;
import org.impalaframework.plugin.bootstrap.BootstrapContextTest;
import org.impalaframework.plugin.bootstrap.PluginLoaderRegistryFactoryBeanTest;
import org.impalaframework.plugin.bootstrap.StandaloneClassLocationResolverFactoryBeanTest;
import org.impalaframework.plugin.builder.SingleStringPluginSpecBuilderTest;
import org.impalaframework.plugin.builder.XmlPluginSpecBuilderTest;
import org.impalaframework.plugin.loader.ApplicationPluginLoaderTest;
import org.impalaframework.plugin.loader.BasePluginLoaderTest;
import org.impalaframework.plugin.loader.DefaultApplicationContextLoaderTest;
import org.impalaframework.plugin.loader.ManualReloadingParentPluginLoaderTest;
import org.impalaframework.plugin.loader.ParentPluginLoaderTest;
import org.impalaframework.plugin.loader.PluginLoaderRegistryTest;
import org.impalaframework.plugin.loader.PluginUtilsTest;
import org.impalaframework.plugin.modification.PluginModificationCalculatorRegistryTest;
import org.impalaframework.plugin.modification.PluginModificationCalculatorTest;
import org.impalaframework.plugin.modification.StickyPluginModificationCalculatorTest;
import org.impalaframework.plugin.monitor.BasePluginModificationListenerTest;
import org.impalaframework.plugin.monitor.ScheduledPluginMonitorBeanTest;
import org.impalaframework.plugin.monitor.ScheduledPluginMonitorTest;
import org.impalaframework.plugin.operation.RemovePluginOperationTest;
import org.impalaframework.plugin.spec.ChildSpecContainerTest;
import org.impalaframework.plugin.spec.ConstructedPluginSpecProviderTest;
import org.impalaframework.plugin.spec.PluginSpecUtilsTest;
import org.impalaframework.plugin.spec.SimpleParentSpecTest;
import org.impalaframework.plugin.spec.SimplePluginSpecTest;
import org.impalaframework.plugin.spec.SimpleSpringContextTest;
import org.impalaframework.plugin.transition.AddLocationsTransitionProcessorTest;
import org.impalaframework.plugin.transition.PluginStateManagerMockTest;
import org.impalaframework.plugin.transition.PluginStateManagerTest;
import org.impalaframework.resolver.PropertyClassLocationResolverTest;
import org.impalaframework.resolver.StandaloneClassLocationResolverFactoryTest;
import org.impalaframework.spring.MissingBeanTest;
import org.impalaframework.spring.SystemPropertyBasedPlaceholderConfigurerTest;
import org.impalaframework.spring.jmx.JMXBootstrapContextTest;
import org.impalaframework.spring.jmx.JMXPluginOperationsTest;
import org.impalaframework.spring.plugin.ParentWithChildContextTest;
import org.impalaframework.spring.plugin.PluginBeanPostProcessorTest;
import org.impalaframework.spring.plugin.PluginMetadataPostProcessorTest;
import org.impalaframework.spring.plugin.PluginProxyFactoryBeanTest;
import org.impalaframework.spring.plugin.SimpleParentContextTest;
import org.impalaframework.spring.plugin.SimplePluginTargetSourceTest;
import org.impalaframework.util.FileUtilsTest;
import org.impalaframework.util.MemoryUtilsTest;
import org.impalaframework.util.PathUtilsTest;
import org.impalaframework.util.PropertyUtilsTest;
import org.impalaframework.util.ResourceUtilsTest;

/**
 * @author Phil Zoio
 */
public class AutomatedCoreTests {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(AddLocationsTransitionProcessorTest.class);
		suite.addTestSuite(ApplicationPluginLoaderTest.class);
		suite.addTestSuite(BootstrapBeanFactoryTest.class);
		suite.addTestSuite(BootstrapContextTest.class);
		suite.addTestSuite(BasePluginLoaderTest.class);	
		suite.addTestSuite(BasePluginModificationListenerTest.class);	
		suite.addTestSuite(BeansetApplicationPluginLoaderTest.class);
		suite.addTestSuite(BeanSetMapReaderTest.class);	
		suite.addTestSuite(BeanSetPropertiesReaderTest.class);
		suite.addTestSuite(ChildSpecContainerTest.class);	
		suite.addTestSuite(CompositeClassLoaderTest.class);
		suite.addTestSuite(ConstructedPluginSpecProviderTest.class);
		suite.addTestSuite(CustomClassLoaderTest.class);	
		suite.addTestSuite(DefaultClassFilterTest.class);
		suite.addTestSuite(FileMonitorImplTest.class);
		suite.addTestSuite(FileUtilsTest.class);
		suite.addTestSuite(ImportingBeanSetTest.class);
		suite.addTestSuite(JMXBootstrapContextTest.class);
		suite.addTestSuite(JMXPluginOperationsTest.class);
		suite.addTestSuite(ManualReloadingParentPluginLoaderTest.class);
		suite.addTestSuite(MemoryUtilsTest.class);
		suite.addTestSuite(MissingBeanTest.class);
		suite.addTestSuite(NamedFactoryBeanTest.class);
		suite.addTestSuite(ParentWithChildContextTest.class);
		suite.addTestSuite(FileSystemPluginClassLoaderTest.class);
		suite.addTestSuite(ParentPluginLoaderTest.class);
		suite.addTestSuite(PathUtilsTest.class);
		suite.addTestSuite(PluginBeanPostProcessorTest.class);
		suite.addTestSuite(PluginMetadataPostProcessorTest.class);
		suite.addTestSuite(PluginLoaderRegistryTest.class);
		suite.addTestSuite(PluginLoaderRegistryFactoryBeanTest.class);
		suite.addTestSuite(PluginModificationCalculatorTest.class);
		suite.addTestSuite(PluginModificationCalculatorRegistryTest.class);
		suite.addTestSuite(PluginSpecUtilsTest.class);
		suite.addTestSuite(PluginUtilsTest.class);
		suite.addTestSuite(PluginProxyFactoryBeanTest.class);
		suite.addTestSuite(PluginStateManagerTest.class);
		suite.addTestSuite(RemovePluginOperationTest.class);
		suite.addTestSuite(PluginStateManagerMockTest.class);
		suite.addTestSuite(PropertyClassLocationResolverTest.class);
		suite.addTestSuite(PropertyUtilsTest.class);
		suite.addTestSuite(DefaultApplicationContextLoaderTest.class);
		suite.addTestSuite(ResourceUtilsTest.class);
		suite.addTestSuite(ScheduledPluginMonitorBeanTest.class);
		suite.addTestSuite(ScheduledPluginMonitorTest.class);
		suite.addTestSuite(SimpleBeansetAwarePluginTest.class);
		suite.addTestSuite(SimpleParentContextTest.class);
		suite.addTestSuite(SimpleSpringContextTest.class);
		suite.addTestSuite(SimpleParentSpecTest.class);
		suite.addTestSuite(SimplePluginSpecTest.class);
		suite.addTestSuite(SimplePluginTargetSourceTest.class);
		suite.addTestSuite(SingleStringPluginSpecBuilderTest.class);
		suite.addTestSuite(StandaloneClassLocationResolverFactoryTest.class);
		suite.addTestSuite(StandaloneClassLocationResolverFactoryBeanTest.class);
		suite.addTestSuite(StickyPluginModificationCalculatorTest.class);
		suite.addTestSuite(SystemPropertyBasedPlaceholderConfigurerTest.class);
		suite.addTestSuite(XmlPluginSpecBuilderTest.class);

		return suite;
	}
}
