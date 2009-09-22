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

import org.impalaframework.command.basic.AlternativeInputCommandTest;
import org.impalaframework.command.basic.ClassFindCommandTest;
import org.impalaframework.command.basic.ClassFindFileRecurseHandlerTest;
import org.impalaframework.command.basic.FileFilterHandlerTest;
import org.impalaframework.command.basic.ModuleDefinitionAwareClassFilterTest;
import org.impalaframework.command.basic.SearchClassCommandTest;
import org.impalaframework.command.basic.SelectMethodCommandTest;
import org.impalaframework.command.framework.CommandStateTest;
import org.impalaframework.command.framework.CommandTest;

public class AutomatedCommandTests {

    public static Test suite() {
        TestSuite suite = new TestSuite();
        
        suite.addTestSuite(AlternativeInputCommandTest.class);
        suite.addTestSuite(ClassFindCommandTest.class);
        suite.addTestSuite(ClassFindFileRecurseHandlerTest.class);
        suite.addTestSuite(CommandTest.class);
        suite.addTestSuite(CommandStateTest.class);
        suite.addTestSuite(FileFilterHandlerTest.class);
        suite.addTestSuite(ModuleDefinitionAwareClassFilterTest.class);
        suite.addTestSuite(SearchClassCommandTest.class);
        suite.addTestSuite(SelectMethodCommandTest.class);
        return suite;
    }
}
