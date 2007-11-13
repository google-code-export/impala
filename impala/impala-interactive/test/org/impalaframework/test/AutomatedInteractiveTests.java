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

import org.impalaframework.command.CommandStateTest;
import org.impalaframework.command.CommandTest;
import org.impalaframework.command.impl.AlternativeInputCommandTest;
import org.impalaframework.command.impl.ClassFindCommandTest;
import org.impalaframework.command.impl.ClassFindFileRecurseHandlerTest;
import org.impalaframework.command.impl.ContextSpecAwareClassFilterTest;
import org.impalaframework.command.impl.FileFilterHandlerTest;
import org.impalaframework.command.impl.SearchClassCommandTest;
import org.impalaframework.command.impl.SelectMethodCommandTest;
import org.impalaframework.testrun.ContextLoaderFactoryTest;
import org.impalaframework.testrun.DynamicContextHolderTest;
import org.impalaframework.testrun.DynamicStateHolderTest;
import org.impalaframework.testrun.PluginTestRunnerTest;
import org.impalaframework.testrun.TestClassLoaderTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AutomatedInteractiveTests {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		
		suite.addTestSuite(AlternativeInputCommandTest.class);
		suite.addTestSuite(ClassFindCommandTest.class);
		suite.addTestSuite(ClassFindFileRecurseHandlerTest.class);
		suite.addTestSuite(CommandTest.class);
		suite.addTestSuite(CommandStateTest.class);
		suite.addTestSuite(ContextLoaderFactoryTest.class);
		suite.addTestSuite(ContextSpecAwareClassFilterTest.class);
		suite.addTestSuite(DynamicContextHolderTest.class);
		suite.addTestSuite(DynamicStateHolderTest.class);
		suite.addTestSuite(FileFilterHandlerTest.class);
		suite.addTestSuite(PluginTestRunnerTest.class);
		suite.addTestSuite(SearchClassCommandTest.class);
		suite.addTestSuite(SelectMethodCommandTest.class);
		suite.addTestSuite(TestClassLoaderTest.class);
		return suite;
	}
}
