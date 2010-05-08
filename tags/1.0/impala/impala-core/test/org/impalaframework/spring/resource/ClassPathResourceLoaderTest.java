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

package org.impalaframework.spring.resource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

import junit.framework.TestCase;

public class ClassPathResourceLoaderTest extends TestCase {

    private ClassPathResourceLoader resourceLoader;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.resourceLoader = new ClassPathResourceLoader();
    }
    
    public final void testGetResource() {
        Resource resource = resourceLoader.getResource("beanset.properties", ClassUtils.getDefaultClassLoader());
        assertTrue(resource instanceof ClassPathResource);
    }
    
    public final void testGetResourceWithPrefix() {
        resourceLoader.setPrefix("beanset/");
        Resource resource = resourceLoader.getResource("imported-context.xml", ClassUtils.getDefaultClassLoader());
        assertTrue(resource instanceof ClassPathResource);
    }

}
