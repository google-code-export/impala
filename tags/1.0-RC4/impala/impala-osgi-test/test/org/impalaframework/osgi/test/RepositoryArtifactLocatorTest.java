/*
 * Copyright 2007-2010 the original author or authors.
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

import junit.framework.TestCase;

public class RepositoryArtifactLocatorTest extends TestCase {

    private RepositoryArtifactLocator locator;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        locator = new RepositoryArtifactLocator("../impala-repository", new String[]{"main", "jetty"});
    }
    
    public void testLocateArtifact() {
        assertNotNull(locator.locateArtifact(null, "commons-io", "1.4"));
        assertNotNull(locator.locateArtifact(null, "jetty", "6.1.21"));
        
        assertNull(locator.locateArtifact(null, "commons-lang", "5.3"));
        assertNull(locator.locateArtifact(null, "jesty", "6.1.11"));
    }
    
    public void testLocateArtifactWithType() {
        assertNotNull(locator.locateArtifact(null, "commons-io", "1.4", "sources"));
        assertNotNull(locator.locateArtifact(null, "jetty", "6.1.21", "sources"));
        
        assertNull(locator.locateArtifact(null, "commons-io", "5.3", "sources"));
        assertNull(locator.locateArtifact(null, "jesty", "6.1.21", "sources"));
    }

}
