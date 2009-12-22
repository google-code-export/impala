/*
 * Copyright 2007-2008 the original author or authors.
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

package org.impalaframework.build.ant;

import org.apache.tools.ant.BuildException;

import junit.framework.TestCase;

public class ArtifactPathTaskTest extends TestCase {

    public void testCheckArgs() {
        ArtifactPathTask task = new ArtifactPathTask();
        failCheckArgs(task, "'organisation' cannot be null");
        task.setOrganisation("org.impalaframework");
        failCheckArgs(task, "'artifact' cannot be null");
        task.setArtifact("impala-build");
        failCheckArgs(task, "'version' cannot be null");
        task.setVersion("1.0");
        failCheckArgs(task, "'property' cannot be null");
        task.setProperty("artifact.path");
        
        task.checkArgs();
    }

    private void failCheckArgs(ArtifactPathTask task, String message) {
        try {
            task.checkArgs();
            fail();
        }
        catch (BuildException e) {
            assertEquals(message, e.getMessage());
        }
    }

}
