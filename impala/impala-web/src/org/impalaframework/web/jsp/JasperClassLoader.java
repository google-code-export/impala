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

package org.impalaframework.web.jsp;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.Enumeration;

/**
 * {@link URLClassLoader} extension for integration with Tomcat Jasper JSP engine
 * @author Phil Zoio
 */
public class JasperClassLoader extends URLClassLoader {

    public JasperClassLoader(URL[] urls, 
            ClassLoader classLoader,
            URLStreamHandlerFactory arg2) {
        super(urls, classLoader, arg2);
    }

    public JasperClassLoader(URL[] urls, ClassLoader classLoader) {
        super(urls, classLoader);
    }

    public JasperClassLoader(URL[] urls) {
        super(urls);
    }
    
    @Override
    public URL getResource(String name) {
        return super.getResource(name);
    }
    
    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        return super.getResources(name);
    }
    
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name);
    }

}
