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
import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.TestCase;

import org.apache.tools.ant.Project;
import org.impalaframework.build.ant.DownloadTask;

/**
 * @author Phil Zoio
 */
public class DownloadTaskManualTest extends TestCase {

    private File downloadDir;

    private DownloadTask task;

    public void setUp() {
        File tmpDir = new File(System.getProperty("java.io.tmpdir"));
        downloadDir = new File(tmpDir, "downloads");
        downloadDir.mkdir();

        task = new DownloadTask();
        task.setBaseSourceUrls("http://impala.googlecode.com/svn/maven/repo,http://ibiblio.org/pub/packages/maven2/");
        task.setToDir(tmpDir);
        task.setDependencies(new File("resources/download-dependencies.txt"));
        task.setProject(new Project());
    }

    public void tearDown() {
        File[] listFiles = downloadDir.listFiles();
        if (listFiles != null) {
            for (int i = 0; i < listFiles.length; i++) {
                System.out.println("Deleting " + listFiles[i]);
                listFiles[i].delete();
            }
        }
        downloadDir.delete();
    }

    public void testExecute() {
        task.execute();
        File[] listFiles = downloadDir.listFiles();
        assertEquals(1, listFiles.length);
    }
    
    public void testExecuteWithSource() {
        task.setDownloadSources(true);
        task.execute();
        File[] listFiles = downloadDir.listFiles();
        assertEquals(2, listFiles.length);
    }
    

    public void testExecuteWithMultipeRepos() throws MalformedURLException {
        File file = new File("C:\\Documents and Settings\\phil\\.m2\\repository");
        URL url = file.toURI().toURL();
        String urlString = url.toString();
        task.setBaseSourceUrls(urlString+",http://ibiblio.org/pub/packages/maven2/");
        task.setDownloadSources(true);
        task.execute();
        File[] listFiles = downloadDir.listFiles();
        assertEquals(2, listFiles.length);
    }

}
