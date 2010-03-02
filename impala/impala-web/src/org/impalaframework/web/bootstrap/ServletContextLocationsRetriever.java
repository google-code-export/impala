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

package org.impalaframework.web.bootstrap;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.impalaframework.bootstrap.BaseLocationsRetriever;
import org.impalaframework.bootstrap.ContextLocationResolver;
import org.impalaframework.config.PropertiesLoader;
import org.impalaframework.config.PropertySource;
import org.impalaframework.config.StaticPropertiesPropertySource;
import org.impalaframework.config.SystemPropertiesPropertySource;
import org.impalaframework.web.config.ContextPathAwareSystemPropertySource;
import org.impalaframework.web.config.ServletContextPropertySource;
import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public class ServletContextLocationsRetriever extends BaseLocationsRetriever {
    
    private final ServletContext servletContext;
    
    public ServletContextLocationsRetriever(ServletContext servletContext, ContextLocationResolver delegate, PropertiesLoader propertiesLoader) {
        super(delegate, propertiesLoader);
        Assert.notNull(servletContext, "servletContext cannot be null");
        this.servletContext = servletContext;
    }

    protected List<PropertySource> getPropertySources(Properties properties) {
        List<PropertySource> propertySources = new ArrayList<PropertySource>();
        
        propertySources.add(new ContextPathAwareSystemPropertySource(servletContext));
        
        //property value sought first in system property
        propertySources.add(new SystemPropertiesPropertySource());
        
        //then in impala properties file
        propertySources.add(new StaticPropertiesPropertySource(properties));
        
        //then as servlet context init param
        propertySources.add(new ServletContextPropertySource(servletContext));
        return propertySources;
    }

    protected Properties getProperties() {
        return super.getProperties();
    }
}
