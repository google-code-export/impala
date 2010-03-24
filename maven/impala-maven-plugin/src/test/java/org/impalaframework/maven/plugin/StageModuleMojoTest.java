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

import junit.framework.TestCase;

import org.apache.maven.model.Model;
import org.apache.maven.project.MavenProject;

public class StageModuleMojoTest extends TestCase {
    
    private StageModuleMojo mojo;
    private Model model;
    private MavenProject project;
    private Properties properties;

    public void setUp() throws Exception {
        mojo = new StageModuleMojo();
        model = new Model();
        project = new MavenProject(model);
        properties = new Properties();
        model.setProperties(properties);
    }

    public void testIsImpalaModule() throws Exception {
        assertEquals("jar", model.getPackaging());
        
        mojo.setProject(project);
        assertTrue(mojo.isImpalaModule());
        
        properties.setProperty("impala.module", "false");
        assertFalse(mojo.isImpalaModule());
        
        properties.setProperty("impala.module", "true");
        assertTrue(mojo.isImpalaModule());
        
        model.setPackaging("war");
        assertTrue(mojo.isImpalaModule());
    }
    
}
