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
import org.impalaframework.classloader.FileSystemModuleClassLoaderTest;
import org.impalaframework.file.handler.DefaultClassFilterTest;
import org.impalaframework.file.monitor.FileMonitorImplTest;
import org.impalaframework.module.beanset.BeanSetMapReaderTest;
import org.impalaframework.module.beanset.BeanSetPropertiesReaderTest;
import org.impalaframework.module.beanset.BeansetApplicationPluginLoaderTest;
import org.impalaframework.module.beanset.ImportingBeanSetTest;
import org.impalaframework.module.beanset.SimpleBeansetAwarePluginTest;
import org.impalaframework.module.bootstrap.BeanFactoryModuleManagementSourceTest;
import org.impalaframework.module.bootstrap.BootstrapContextTest;
import org.impalaframework.module.bootstrap.ModuleLoaderRegistryFactoryBeanTest;
import org.impalaframework.module.bootstrap.StandaloneModuleLocationResolverFactoryBeanTest;
import org.impalaframework.module.builder.SingleStringPluginSpecBuilderTest;
import org.impalaframework.module.builder.XmlPluginSpecBuilderTest;
import org.impalaframework.module.loader.ApplicationPluginLoaderTest;
import org.impalaframework.module.loader.BasePluginLoaderTest;
import org.impalaframework.module.loader.DefaultApplicationContextLoaderTest;
import org.impalaframework.module.loader.ManualReloadingParentPluginLoaderTest;
import org.impalaframework.module.loader.ParentPluginLoaderTest;
import org.impalaframework.module.loader.PluginLoaderRegistryTest;
import org.impalaframework.module.loader.PluginUtilsTest;
import org.impalaframework.module.modification.PluginModificationCalculatorRegistryTest;
import org.impalaframework.module.modification.PluginModificationCalculatorTest;
import org.impalaframework.module.modification.StickyPluginModificationCalculatorTest;
import org.impalaframework.module.monitor.BasePluginModificationListenerTest;
import org.impalaframework.module.monitor.ScheduledPluginMonitorBeanTest;
import org.impalaframework.module.monitor.ScheduledPluginMonitorTest;
import org.impalaframework.module.operation.RemovePluginOperationTest;
import org.impalaframework.module.spec.ChildModuleContainerTest;
import org.impalaframework.module.spec.ConstructedModuleDefinitionSourceTest;
import org.impalaframework.module.spec.ModuleDefinitionUtilsTest;
import org.impalaframework.module.spec.SimpleRootModuleDefinitionTest;
import org.impalaframework.module.spec.SimpleModuleDefinitionTest;
import org.impalaframework.module.spec.SimpleSpringContextTest;
import org.impalaframework.module.transition.AddLocationsTransitionProcessorTest;
import org.impalaframework.module.transition.PluginStateManagerMockTest;
import org.impalaframework.module.transition.PluginStateManagerTest;
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
		suite.addTestSuite(BeanFactoryModuleManagementSourceTest.class);
		suite.addTestSuite(BootstrapContextTest.class);
		suite.addTestSuite(BasePluginLoaderTest.class);	
		suite.addTestSuite(BasePluginModificationListenerTest.class);	
		suite.addTestSuite(BeansetApplicationPluginLoaderTest.class);
		suite.addTestSuite(BeanSetMapReaderTest.class);	
		suite.addTestSuite(BeanSetPropertiesReaderTest.class);
		suite.addTestSuite(ChildModuleContainerTest.class);	
		suite.addTestSuite(CompositeClassLoaderTest.class);
		suite.addTestSuite(ConstructedModuleDefinitionSourceTest.class);
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
		suite.addTestSuite(FileSystemModuleClassLoaderTest.class);
		suite.addTestSuite(ParentPluginLoaderTest.class);
		suite.addTestSuite(PathUtilsTest.class);
		suite.addTestSuite(PluginBeanPostProcessorTest.class);
		suite.addTestSuite(PluginMetadataPostProcessorTest.class);
		suite.addTestSuite(PluginLoaderRegistryTest.class);
		suite.addTestSuite(ModuleLoaderRegistryFactoryBeanTest.class);
		suite.addTestSuite(PluginModificationCalculatorTest.class);
		suite.addTestSuite(PluginModificationCalculatorRegistryTest.class);
		suite.addTestSuite(ModuleDefinitionUtilsTest.class);
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
		suite.addTestSuite(SimpleRootModuleDefinitionTest.class);
		suite.addTestSuite(SimpleModuleDefinitionTest.class);
		suite.addTestSuite(SimplePluginTargetSourceTest.class);
		suite.addTestSuite(SingleStringPluginSpecBuilderTest.class);
		suite.addTestSuite(StandaloneClassLocationResolverFactoryTest.class);
		suite.addTestSuite(StandaloneModuleLocationResolverFactoryBeanTest.class);
		suite.addTestSuite(StickyPluginModificationCalculatorTest.class);
		suite.addTestSuite(SystemPropertyBasedPlaceholderConfigurerTest.class);
		suite.addTestSuite(XmlPluginSpecBuilderTest.class);

		return suite;
	}
}
