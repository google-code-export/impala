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

package tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AutomatedExampleTests {

    public static Test suite() {
        TestSuite suite = new TestSuite();
        //note some of these tests are repeated to simulated a larger test suite
        //and the effects of reloading/unloading
        suite.addTestSuite(InitialIntegrationTest.class);
        suite.addTestSuite(EntryDAOTest.class);
        suite.addTestSuite(EntryServiceTest.class);
        suite.addTestSuite(InitialIntegrationTest.class);
        suite.addTestSuite(EntryServiceTest.class);
        suite.addTestSuite(EntryDAOTest.class);
        suite.addTestSuite(ResourceTest.class);
        return suite;
    }
}
