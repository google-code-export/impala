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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

public class CompositeResourceLoaderTest extends TestCase {

    private List<ResourceLoader> resourceLoaders;

    private CompositeResourceLoader resourceLoader;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        resourceLoaders = new ArrayList<ResourceLoader>();
        resourceLoaders.add(new FileSystemResourceLoader());
        resourceLoaders.add(new ClassPathResourceLoader());
        resourceLoader = new CompositeResourceLoader(resourceLoaders);
    }

    public final void testGetResourceOnFileSystem() {
        Resource resource = resourceLoader.getResource("../impala-core/files/MyTestClass.jar", ClassUtils
                .getDefaultClassLoader());
        assertTrue(resource instanceof FileSystemResource);
    }

    public final void testGetResourceOnClassPath() {
        Resource resource = resourceLoader.getResource("beanset.properties", ClassUtils.getDefaultClassLoader());
        assertTrue(resource instanceof ClassPathResource);
    }
}
