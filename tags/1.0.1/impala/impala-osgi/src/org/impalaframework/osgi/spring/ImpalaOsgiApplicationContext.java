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

package org.impalaframework.osgi.spring;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.osgi.context.support.OsgiBundleXmlApplicationContext;
import org.springframework.util.Assert;

/**
 * Extends {@link OsgiBundleXmlApplicationContext}, providing the mechanism for 
 * {@link Resource} locations to be wired in separately, rather than having it 
 * figured out internally via a configLocations String array.
 * @author Phil Zoio
 */
public class ImpalaOsgiApplicationContext extends OsgiBundleXmlApplicationContext {

    private static Log logger = LogFactory.getLog(ImpalaOsgiApplicationContext.class);  
    
    private Resource[] resources;

    public ImpalaOsgiApplicationContext() {
        super();
    }   
    
    public ImpalaOsgiApplicationContext(ApplicationContext parent) {
        super(parent);
    }

    protected void loadBeanDefinitions(XmlBeanDefinitionReader reader)
            throws BeansException, IOException {

        Assert.notNull(resources, "resources cannot be null");
        Assert.notNull(reader, "bean definition reader cannot be null");
        
        if (logger.isDebugEnabled()) {
            logger.debug("Loading bean definitions from the following resources " + Arrays.toString(resources));
        }
        
        for (int i = 0; i < resources.length; i++) {
            final int count = reader.loadBeanDefinitions(resources[i]);
            if (logger.isDebugEnabled()) {
                logger.debug("Loaded " + count 
                        + " resources from resource '"+ resources[i].getDescription() + "'");
            }
        }
    }

    public void setConfigResources(Resource[] resources) {
        this.resources = resources;
    }
    
}

