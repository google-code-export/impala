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

package org.impalaframework.build.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.openpgp.ant.OpenPgpSignerTask;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Delete;

public class MavenPublishSignTaskTest extends TestCase {

    private TestSignTask task;
    private File destDir;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        task = new TestSignTask();
        destDir = new File("../maven/testrepo");
    }    
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        Delete delete = new Delete();
        delete.setProject(new Project());
        delete.setDir(destDir);
        delete.setFailOnError(false);
        
        //comment this out to inspect output
        delete.execute();
    }
    
    public void testCheckArgs() throws Exception {
        task = new TestSignTask();
        expectFailure("'keyId' cannot be null");
        task.setKeyId("keyid");
        expectFailure("'pubring' cannot be null");
        task.setPubring(new File("pubring"));
        expectFailureContains("pubring does not exist");
        task.setPubring(new File("./"));
        expectFailure("'secring' cannot be null");
        task.setSecring(new File("secring"));
        expectFailureContains("secring does not exist");
        task.setSecring(new File("./"));
        expectFailure("'password' cannot be null");
        task.setPassword("pwd");
        task.checkSignTaskArgs();       
    }
    
    public void testExecute() throws Exception {

        task.setProject(new Project());
        task.setSourceDir(new File("nonexistent"));
        task.setArtifacts("impala-core,impala-build");
        task.setOrganisation("org.impalaframework");
        task.setSourceDir(new File("../impala-repository/dist"));
        task.setDestDir(destDir);
        
        task.setKeyId("keyid");
        task.setPubring(new File("./"));
        task.setSecring(new File("./"));
        task.setPassword("pwd");
        
        task.execute();
        
        final List<File> signedFiles = task.getSignedFiles();
        for (File file : signedFiles) {
            assertTrue(file.exists());
        }
        assertEquals(6, signedFiles.size());
        
    }

    private void expectFailure(String expectedMessage) {
        try {
            task.checkSignTaskArgs();
            fail();
        } catch (BuildException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    private void expectFailureContains(String expectedMessage) {
        try {
            task.checkSignTaskArgs();
            fail();
        } catch (BuildException e) {
            System.out.println(e.getMessage());
            assertTrue(e.getMessage().contains(expectedMessage));
        }
    }
    
}

class TestSignTask extends MavenPublishSignTask {
    
    private List<File> signedFiles = new ArrayList<File>();
    
    @Override
    void sign(OpenPgpSignerTask task, File fileToSign) {
        System.out.println("SIGNING: " + fileToSign);
        signedFiles.add(fileToSign);
    }

    public List<File> getSignedFiles() {
        return signedFiles;
    }
}
