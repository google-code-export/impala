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
import org.impalaframework.module.builder.SingleStringModuleDefinitionSourceTest;
import org.impalaframework.module.builder.XmlModuleDefinitionSourceTest;
import org.impalaframework.module.definition.ChildModuleContainerTest;
import org.impalaframework.module.definition.ConstructedModuleDefinitionSourceTest;
import org.impalaframework.module.definition.ModuleDefinitionUtilsTest;
import org.impalaframework.module.definition.SimpleModuleDefinitionTest;
import org.impalaframework.module.definition.SimpleRootModuleDefinitionTest;
import org.impalaframework.module.definition.SimpleSpringContextTest;
import org.impalaframework.module.holder.ModuleStateHolderMockTest;
import org.impalaframework.module.holder.ModuleStateHolderTest;
import org.impalaframework.module.loader.ApplicationModuleLoaderTest;
import org.impalaframework.module.loader.BaseModuleLoaderTest;
import org.impalaframework.module.loader.DefaultApplicationContextLoaderTest;
import org.impalaframework.module.loader.ManualReloadingRootModuleLoaderTest;
import org.impalaframework.module.loader.RootModuleLoaderTest;
import org.impalaframework.module.loader.ModuleLoaderRegistryTest;
import org.impalaframework.module.loader.ModuleUtilsTest;
import org.impalaframework.module.modification.ModuleModificationExtractorRegistryTest;
import org.impalaframework.module.modification.ModuleModificationExtractorTest;
import org.impalaframework.module.modification.StickyModuleModificationExtractorTest;
import org.impalaframework.module.monitor.BaseModuleChangeListenerTest;
import org.impalaframework.module.monitor.ScheduledModuleChangeMonitorBeanTest;
import org.impalaframework.module.monitor.ScheduledModuleChangeMonitorTest;
import org.impalaframework.module.operation.RemoveModuleOperationTest;
import org.impalaframework.module.transition.AddLocationsTransitionProcessorTest;
import org.impalaframework.resolver.PropertyModuleLocationResolverTest;
import org.impalaframework.resolver.StandaloneModuleLocationResolverFactoryTest;
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
		suite.addTestSuite(ApplicationModuleLoaderTest.class);
		suite.addTestSuite(BeanFactoryModuleManagementSourceTest.class);
		suite.addTestSuite(BootstrapContextTest.class);
		suite.addTestSuite(BaseModuleLoaderTest.class);	
		suite.addTestSuite(BaseModuleChangeListenerTest.class);	
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
		suite.addTestSuite(ManualReloadingRootModuleLoaderTest.class);
		suite.addTestSuite(MemoryUtilsTest.class);
		suite.addTestSuite(MissingBeanTest.class);
		suite.addTestSuite(NamedFactoryBeanTest.class);
		suite.addTestSuite(ParentWithChildContextTest.class);
		suite.addTestSuite(FileSystemModuleClassLoaderTest.class);
		suite.addTestSuite(RootModuleLoaderTest.class);
		suite.addTestSuite(PathUtilsTest.class);
		suite.addTestSuite(PluginBeanPostProcessorTest.class);
		suite.addTestSuite(PluginMetadataPostProcessorTest.class);
		suite.addTestSuite(ModuleLoaderRegistryTest.class);
		suite.addTestSuite(ModuleLoaderRegistryFactoryBeanTest.class);
		suite.addTestSuite(ModuleModificationExtractorTest.class);
		suite.addTestSuite(ModuleModificationExtractorRegistryTest.class);
		suite.addTestSuite(ModuleDefinitionUtilsTest.class);
		suite.addTestSuite(ModuleUtilsTest.class);
		suite.addTestSuite(PluginProxyFactoryBeanTest.class);
		suite.addTestSuite(ModuleStateHolderTest.class);
		suite.addTestSuite(RemoveModuleOperationTest.class);
		suite.addTestSuite(ModuleStateHolderMockTest.class);
		suite.addTestSuite(PropertyModuleLocationResolverTest.class);
		suite.addTestSuite(PropertyUtilsTest.class);
		suite.addTestSuite(DefaultApplicationContextLoaderTest.class);
		suite.addTestSuite(ResourceUtilsTest.class);
		suite.addTestSuite(ScheduledModuleChangeMonitorBeanTest.class);
		suite.addTestSuite(ScheduledModuleChangeMonitorTest.class);
		suite.addTestSuite(SimpleBeansetAwarePluginTest.class);
		suite.addTestSuite(SimpleParentContextTest.class);
		suite.addTestSuite(SimpleSpringContextTest.class);
		suite.addTestSuite(SimpleRootModuleDefinitionTest.class);
		suite.addTestSuite(SimpleModuleDefinitionTest.class);
		suite.addTestSuite(SimplePluginTargetSourceTest.class);
		suite.addTestSuite(SingleStringModuleDefinitionSourceTest.class);
		suite.addTestSuite(StandaloneModuleLocationResolverFactoryTest.class);
		suite.addTestSuite(StandaloneModuleLocationResolverFactoryBeanTest.class);
		suite.addTestSuite(StickyModuleModificationExtractorTest.class);
		suite.addTestSuite(SystemPropertyBasedPlaceholderConfigurerTest.class);
		suite.addTestSuite(XmlModuleDefinitionSourceTest.class);

		return suite;
	}
}
