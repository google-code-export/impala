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

package org.impalaframework.osgiroot.test;

import org.impalaframework.osgi.test.BaseBundleLocationConfiguration;
import org.impalaframework.osgi.test.BundleLocationConfiguration;
import org.impalaframework.osgi.test.OsgiIntegrationTest;

public abstract class OsgiContextTest extends OsgiIntegrationTest {

    @Override
    protected BundleLocationConfiguration newBundleLocationConfiguration() {
        return new TestBundleConfigurationLocation();
    }

    class TestBundleConfigurationLocation extends BaseBundleLocationConfiguration {

        protected String[] getArtifactLocatorFolders() {
            return new String[] {"osgi"};
        }
        
        protected String[] getTestBundleFolders() {
            return new String[] {"osgi", "main"};
        }

        protected String getTestBundleExcludes() {
            return "main: build,extender,jmx";
        }

        protected String getTestBundleIncludes() {
            return "osgi: *; main: impala";
        }

        protected String[] getExtenderBundleFolders() {
            return new String[] {"dist", "main"};
        }

        protected String getExtenderBundleExcludes() {
            return null;
        }

        protected String getExtenderBundleIncludes() {
            return "dist:extender;main:extender";
        }

        protected String getRepositoryRootDirectory() {
            return "../osgi-repository";
        }
    }
    
}
