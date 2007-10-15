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

package net.java.impala.web.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.java.impala.spring.web.DefaultWebApplicationContextLoaderTest;
import net.java.impala.spring.web.ImpalaContextLoaderTest;
import net.java.impala.spring.web.MultiServletImpalaContextLoaderTest;
import net.java.impala.spring.web.WebPluginLoaderTest;

public class AutomatedTests {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(DefaultWebApplicationContextLoaderTest.class);
		suite.addTestSuite(ImpalaContextLoaderTest.class);
		suite.addTestSuite(MultiServletImpalaContextLoaderTest.class);
		suite.addTestSuite(WebPluginLoaderTest.class);
		suite.addTestSuite(DefaultWebApplicationContextLoaderTest.class);
		return suite;
	}
}
