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


/**
 * @author Phil Zoio
 */
public class DownloadTaskIndividualTest extends BaseDownloadTaskTest {

    public void testExecute() {
        //task.doDownload("web from javax.servlet.jsp:jsp-api:2.1", new String[]{"http://repo1.maven.org/maven2/"});        
        //task.doDownload("web from javax.servlet.jsp:jsp-api:2.0", new String[]{"http://repo1.maven.org/maven2/"});    
        //task.setupHelperTasks();
        task.doDownload("web from javax.servlet:servlet-api:2.4", new String[]{"http://repo1.maven.org/maven2/"});

        System.out.println(task.getResults());
    }

}
