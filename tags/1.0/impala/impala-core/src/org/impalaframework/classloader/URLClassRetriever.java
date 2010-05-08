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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.NoSuchElementException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.util.URLUtils;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;

/**
 * Implementation of {@link ClassRetriever} which delegates to the
 * {@link URLClassLoader#findResource(String)} method for location both the
 * bytes for classes as well as resources. Works both for both directories and
 * jar files located on the file system. Does not explicitly support any other
 * kinds of URLs (e.g. classes loaded from remote locations).
 * 
 * @author Phil Zoio
 */
public class URLClassRetriever implements ClassRetriever {

    private static Log logger = LogFactory.getLog(URLClassRetriever.class);
    
    private URL[] urls;
    private URLClassLoader urlClassLoader;
    
    public URLClassRetriever(File[] files) {
        super();
        Assert.notNull(files, "files cannot be null");
        this.urls = URLUtils.createUrls(files);
        this.urlClassLoader = new URLClassLoader(urls);
    }

    /**
     * Returns the bytes for a particular named class
     */
    public byte[] getClassBytes(String className) {
        
        if (logger.isTraceEnabled()) {
            logger.trace("Attempting to find class " + className + " from " + this);
        }
        
        String resourceName = className.replace('.','/') + ".class";
        URL resource = urlClassLoader.findResource(resourceName);
        if (resource != null) {
            InputStream stream = null;
            try {
                stream = resource.openConnection().getInputStream();
                byte[] bytes = FileCopyUtils.copyToByteArray(stream);
                
                if (logger.isTraceEnabled()) {
                    logger.trace("Successfully found bytes for " + className + " from " + this);
                }
                
                return bytes;
            } catch (IOException e) {
                logger.warn("Error attempting to read from resource " + resource + ". Returning null");
            } finally {
                try {
                    if (stream != null) stream.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    public Enumeration<URL> findResources(String resourceName) {
        
        Enumeration<URL> findResource = null;
        try {
            findResource = urlClassLoader.findResources(resourceName);

            if (logger.isTraceEnabled()) {
                
                if (findResource != null) {
                    logger.trace("Successfully found URL " + findResource + " from " + this);
                } else {
                    logger.trace("Unable to find URL for " + resourceName + " from " + this);
                }
            }
        } catch (IOException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("IO exception caught in findResources(): ", e);
            }
            
            return new Enumeration<URL>() {
                public boolean hasMoreElements() {
                    return false;
                }
                public URL nextElement() {
                    throw new NoSuchElementException();
                }
            };
        }
        
        return findResource;
    }
    
    /**
     * Returns a URL representing a particular resource
     */
    public URL findResource(String resourceName) {
        URL findResource = urlClassLoader.findResource(resourceName);

        if (logger.isTraceEnabled()) {
            
            if (findResource != null) {
                logger.trace("Successfully found URL " + findResource + " from " + this);
            } else {
                logger.trace("Unable to find URL for " + resourceName + " from " + this);
            }
        }
        
        return findResource;
    }
    
    /**
     * Returns the underlying {@link URLClassLoader} used to retrieve local classes and resources
     */
    public URLClassLoader getUrlClassLoader() {
        return urlClassLoader;
    }
    
    @Override
    public String toString() {
        String string = super.toString();
        StringBuffer buffer = new StringBuffer(string);
        buffer.append(", URLs: ");
        buffer.append(Arrays.toString(urls));
        return buffer.toString();
    }
    
}
