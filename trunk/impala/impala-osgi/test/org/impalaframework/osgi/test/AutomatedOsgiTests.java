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

package org.impalaframework.osgi.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.impalaframework.osgi.bootstrap.OsgiContextLocationResolverTest;
import org.impalaframework.osgi.classloader.OsgiClassLoaderFactoryTest;
import org.impalaframework.osgi.module.spring.loader.OsgiModuleLoaderTest;
import org.impalaframework.osgi.module.transition.OsgiLoadTransitionProcessorTest;
import org.impalaframework.osgi.module.transition.OsgiUnloadTransitionProcessorTest;
import org.impalaframework.osgi.spring.ImpalaOsgiApplicationContextTest;
import org.impalaframework.osgi.util.ImpalaOsgiUtilsTest;
import org.impalaframework.osgi.util.OsgiUtilsTest;

/**
 * @author Phil Zoio
 */
public class AutomatedOsgiTests {

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(ImpalaOsgiApplicationContextTest.class);
        suite.addTestSuite(ImpalaOsgiUtilsTest.class);
        suite.addTestSuite(OsgiClassLoaderFactoryTest.class);
        suite.addTestSuite(OsgiContextLocationResolverTest.class);
        suite.addTestSuite(OsgiBootstrapIntegrationTest.class);
        suite.addTestSuite(OsgiLoadTransitionProcessorTest.class);
        suite.addTestSuite(OsgiModuleLoaderTest.class);
        suite.addTestSuite(OsgiUnloadTransitionProcessorTest.class);
        suite.addTestSuite(OsgiUtilsTest.class);
        return suite;
    }
}
