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

package org.impalaframework.build.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.impalaframework.build.ant.AntForeachTaskTest;
import org.impalaframework.build.ant.ArtifactPathTaskTest;
import org.impalaframework.build.ant.ConditionalPropertyTaskTest;
import org.impalaframework.build.ant.CopyPackageMapperTest;
import org.impalaframework.build.ant.DownloadTaskTest;
import org.impalaframework.build.ant.GetTaskResultTest;
import org.impalaframework.build.ant.GetTaskTest;
import org.impalaframework.build.ant.MavenPublishSignTaskTest;
import org.impalaframework.build.ant.MavenPublishTaskTest;
import org.impalaframework.build.ant.SVNRevisionTaskTest;

/**
 * @author Phil Zoio
 */
public class AutomatedBuildTests {

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(AntForeachTaskTest.class);
        suite.addTestSuite(ArtifactPathTaskTest.class);
        suite.addTestSuite(ConditionalPropertyTaskTest.class);
        suite.addTestSuite(CopyPackageMapperTest.class);
        suite.addTestSuite(DownloadTaskTest.class);
        suite.addTestSuite(GetTaskTest.class);
        suite.addTestSuite(GetTaskResultTest.class);
        suite.addTestSuite(MavenPublishTaskTest.class);
        suite.addTestSuite(MavenPublishSignTaskTest.class);
        suite.addTestSuite(SVNRevisionTaskTest.class);

        return suite;
    }
}
