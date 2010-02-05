/*
 * Copyright 2007-2008 the original author or authors.
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

import java.net.URL;
import java.net.URLClassLoader;

import org.apache.jasper.servlet.JspServlet;
import org.impalaframework.classloader.ClassRetriever;
import org.impalaframework.classloader.URLClassRetriever;
import org.impalaframework.classloader.graph.GraphClassLoader;
import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.web.spring.integration.ServletFactoryBean;
import org.springframework.beans.factory.BeanClassLoaderAware;

/**
 * Specialized {@link ServletFactoryBean} implementation used to initialize Jasper JSP engine
 * @author Phil Zoio
 */
public class JasperServletFactoryBean extends ServletFactoryBean implements BeanClassLoaderAware {

    private ClassLoader classLoader;

    @Override
    public void afterPropertiesSet() throws Exception {
        
        //FIXME add more tests
        setServletClass(JspServlet.class);
        
        final ClassLoader existingClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            
            if (!(existingClassLoader instanceof URLClassLoader)) {
                
                final JasperClassLoader jasperClassLoader = maybeCreateURLClassLoader();
                if (jasperClassLoader == null) {
                    throw new ConfigurationException("Cannot support JSP as the application is unable to create a JSPClassLoader instance for the current module");
                } else {
                    Thread.currentThread().setContextClassLoader(jasperClassLoader);
                }
            }
            
            super.afterPropertiesSet();
            
        } finally {
            super.afterPropertiesSet();
            Thread.currentThread().setContextClassLoader(existingClassLoader);
        }
        
    }

    private JasperClassLoader maybeCreateURLClassLoader() {
        if (classLoader instanceof GraphClassLoader) {
            GraphClassLoader gcl = (GraphClassLoader) classLoader;
            final ClassRetriever classRetriever = gcl.getClassRetriever();
            
            if (classRetriever instanceof URLClassRetriever) {
                URLClassRetriever retriever = (URLClassRetriever) classRetriever;
                final URLClassLoader urlClassLoader = retriever.getUrlClassLoader();
                final ClassLoader parent = gcl.getParent();
                
                if (parent instanceof URLClassLoader) {
                    URLClassLoader parentUrlClassLoader = (URLClassLoader) parent;
                    
                    URL[] urls = urlClassLoader.getURLs();
                    URL[] parentUrls = parentUrlClassLoader.getURLs();
                    URL[] combined = new URL[urls.length + parentUrls.length];
                    
                    System.arraycopy(urls, 0, combined, 0, urls.length);
                    System.arraycopy(parentUrls, 0, combined, urls.length, parentUrls.length);
                    return new JasperClassLoader(combined);
                }
                else {
                    return new JasperClassLoader(urlClassLoader.getURLs(), parent);
                }
            }
        }
        return null;
    }
    
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

}
