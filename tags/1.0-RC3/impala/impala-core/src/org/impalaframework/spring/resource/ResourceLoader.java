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

import org.springframework.core.io.Resource;

/**
 * Defines mechanism for accessing a resource from a given location. Similar to
 * the Spring <code>ResourceLoader</code> interface. The main difference is
 * that the <code>ClassLoader</code> is supplied in the
 * <code>getResource()</code> call. This has the advantage of allowing a
 * single resource loader to be used with different class loaders. It also
 * simplifies the implementation.
 * @author Phil Zoio
 */
public interface ResourceLoader {
    public Resource getResource(String location, ClassLoader classLoader);
}
