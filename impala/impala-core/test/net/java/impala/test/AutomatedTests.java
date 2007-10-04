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

package net.java.impala.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.java.impala.location.PropertyClassLocationResolverTest;
import net.java.impala.location.StandaloneClassLocationResolverFactoryTest;
import net.java.impala.monitor.FileMonitorImplTest;
import net.java.impala.spring.beanset.BeanSetTest;
import net.java.impala.spring.externalconfig.AlternativeLocationPlaceholderConfigurerTest;
import net.java.impala.spring.missingbean.MissingBeanTest;
import net.java.impala.spring.monitor.ContextReloaderTest;
import net.java.impala.spring.plugin.ParentWithChildContextTest;
import net.java.impala.spring.plugin.PluginBeanPostProcessorTest;
import net.java.impala.spring.plugin.PluginProxyFactoryBeanTest;
import net.java.impala.spring.plugin.SimpleParentContextTest;
import net.java.impala.spring.shared.CustomClassLoaderTest;
import net.java.impala.spring.shared.ParentClassLoaderTest;
import net.java.impala.spring.util.ApplicationContextLoaderTest;
import net.java.impala.util.FileUtilsTest;
import net.java.impala.util.PathUtilsTest;

public class AutomatedTests {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(BeanSetTest.class);
		suite.addTestSuite(AlternativeLocationPlaceholderConfigurerTest.class);
		suite.addTestSuite(MissingBeanTest.class);
		suite.addTestSuite(FileMonitorImplTest.class);
		suite.addTestSuite(ParentWithChildContextTest.class);
		suite.addTestSuite(PluginProxyFactoryBeanTest.class);
		suite.addTestSuite(SimpleParentContextTest.class);
		suite.addTestSuite(CustomClassLoaderTest.class);
		suite.addTestSuite(ParentClassLoaderTest.class);
		suite.addTestSuite(ContextReloaderTest.class);
		suite.addTestSuite(PluginBeanPostProcessorTest.class);
		suite.addTestSuite(PropertyClassLocationResolverTest.class);
		suite.addTestSuite(StandaloneClassLocationResolverFactoryTest.class);
		suite.addTestSuite(PathUtilsTest.class);
		suite.addTestSuite(FileUtilsTest.class);
		suite.addTestSuite(ApplicationContextLoaderTest.class);

		return suite;
	}
}
