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

import java.util.Collection;

import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * Implements resource loader according to the composite pattern. The Composite
 * resource loader will return the first resource which exists using the
 * available set of resource loaders
 * @author Phil Zoio
 */
public class CompositeResourceLoader implements ResourceLoader {

    private Collection<ResourceLoader> resourceLoaders;

    public CompositeResourceLoader(Collection<ResourceLoader> resourceLoaders) {
        super();
        Assert.notNull(resourceLoaders);
        this.resourceLoaders = resourceLoaders;
    }

    public Resource getResource(String location, ClassLoader classLoader) {
        Resource resource = null;
        for (ResourceLoader resourceLoader : resourceLoaders) {
            resource = resourceLoader.getResource(location, classLoader);
            if (resource != null && resource.exists()) {
                return resource;
            }
        }
        return null;
    }

}
