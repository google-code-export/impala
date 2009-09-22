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

package org.impalaframework.osgi.test.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.impalaframework.osgi.test.ConfigurableFileFilterTest;
import org.impalaframework.osgi.test.FileFetcherTest;
import org.impalaframework.osgi.test.InjectableModuleDefinitionSourceTest;
import org.impalaframework.osgi.test.RepositoryArtifactLocatorTest;

/**
 * @author Phil Zoio
 */
public class AutomatedOsgiTestTests {

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(ConfigurableFileFilterTest.class);
        suite.addTestSuite(FileFetcherTest.class);
        suite.addTestSuite(InjectableModuleDefinitionSourceTest.class);
        suite.addTestSuite(RepositoryArtifactLocatorTest.class);
        return suite;
    }
}
