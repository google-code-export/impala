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

import org.apache.tools.ant.BuildException;

import junit.framework.TestCase;

public class CopyPackageMapperTest extends TestCase {

    private CopyPackageMapper mapper;
    private String packageName;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mapper = new CopyPackageMapper();
        packageName = "com.myapp";
    }

    public void testNoTo() throws Exception {
        
        try {
            mapper.mapFileName(null);
            fail();
        } catch (BuildException e) {
            assertEquals("No 'to' attribute set for org.impalaframework.build.ant.CopyPackageMapper", e.getMessage());
        }
    }
    
    public void testMap() throws Exception {
        mapper.setTo(packageName);
        
        expectName("mydir/myfile.txt", "com/myapp/mydir/myfile.txt");
        expectName("myfile.txt", "com/myapp/myfile.txt");   

        packageName = "com/myapp/";
        
        expectName("mydir/myfile.txt", "com/myapp/mydir/myfile.txt");
        expectName("myfile.txt", "com/myapp/myfile.txt");   
    }

    private void expectName(final String source, final String expected) {
        final String[] mapFileName = mapper.mapFileName(source);
        assertEquals(1, mapFileName.length);
        assertEquals(expected, mapFileName[0]);
    }
    
}
