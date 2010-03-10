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

package org.impalaframework.bootstrap;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.impalaframework.config.PropertiesLoader;
import org.impalaframework.config.PropertySource;
import org.impalaframework.config.StaticPropertiesPropertySource;
import org.impalaframework.config.SystemPropertiesPropertySource;

/**
 * Overrides {@link BaseLocationsRetriever} to provide an implementation of
 * {@link #getPropertySources(Properties)} which can pick up Impala
 * configuration settings from either System properties or from an Impala
 * properties file (by default <i>impala.properties</i>).
 * 
 * @author Phil Zoio
 */
public class SimpleLocationsRetriever extends BaseLocationsRetriever {
    
    public SimpleLocationsRetriever(ContextLocationResolver delegate, PropertiesLoader propertiesLoader) {
        super(delegate, propertiesLoader);
    }

    protected List<PropertySource> getPropertySources(Properties properties) {
        List<PropertySource> propertySources = new ArrayList<PropertySource>();
        
        //property value sought first in system property
        propertySources.add(new SystemPropertiesPropertySource());
        
        //then in impala properties file
        propertySources.add(new StaticPropertiesPropertySource(properties));
        
        return propertySources;
    }

}
