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

package org.impalaframework.classloader;

import java.net.URL;
import java.util.Enumeration;

/**
 * Interface which abstracts the mechanism for retrieving the bytes for a Java
 * class or resource
 * 
 * @author Phil Zoio
 */
public interface ClassRetriever {
    
    /**
     * Returns the bytes for a particular named class
     */
    byte[] getClassBytes(String className);
    
    /**
     * Returns a URL representing a particular resource
     */
    URL findResource(String resourceName);
    
    /**
     * Returns a URL representing a particular resource
     */
    Enumeration<URL> findResources(String resourceName);
    
}
