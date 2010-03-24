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

package org.impalaframework.maven.plugin;

import java.util.Properties;

import org.apache.maven.model.Model;
import org.apache.maven.project.MavenProject;

import junit.framework.TestCase;

public class CopyModulesMojoTest extends TestCase {

    private CopyModulesMojo mojo;
    private Model model;
    private MavenProject project;
    private Properties properties;

    public void setUp() throws Exception {
        mojo = new CopyModulesMojo();
        model = new Model();
        project = new MavenProject(model);
        properties = new Properties();
        model.setProperties(properties);
        model.setPackaging("war");
    }

    public void testIsImpalaModule() throws Exception {
        assertEquals("war", model.getPackaging());
        
        mojo.setProject(project);
        assertTrue(mojo.isImpalaHost());
        
        properties.setProperty("impala.host", "false");
        assertFalse(mojo.isImpalaHost());
        
        properties.setProperty("impala.host", "true");
        assertTrue(mojo.isImpalaHost());
        
        model.setPackaging("jar");
        assertTrue(mojo.isImpalaHost());
    }

}
