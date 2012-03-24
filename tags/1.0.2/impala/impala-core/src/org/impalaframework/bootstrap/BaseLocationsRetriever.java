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

import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.config.PrefixedCompositePropertySource;
import org.impalaframework.config.PropertiesHolder;
import org.impalaframework.config.PropertiesLoader;
import org.impalaframework.config.PropertySource;
import org.impalaframework.config.PropertySourceHolder;
import org.springframework.util.Assert;

/**
 * Implements base functionality for {@link LocationsRetriever} interface. Main responsibility is to 
 * provide a final implementation of the {@link #getContextLocations()} method, allowing 
 * subclasses to override {@link #getPropertySources(Properties)}. Delegates the job of 
 * finding the locations from the {@link PropertySource}s to a {@link ContextLocationResolver} delegate.
 * @author Phil Zoio
 */
public abstract class BaseLocationsRetriever implements LocationsRetriever {

    private static final Log logger = LogFactory.getLog(BaseLocationsRetriever.class);
    
    private final ContextLocationResolver delegate;
    private final PropertiesLoader propertiesLoader;
    
    public BaseLocationsRetriever(ContextLocationResolver delegate, PropertiesLoader propertiesLoader) {
        super();
        Assert.notNull(delegate, "ContextLocationResolver delegate cannot be null");
        Assert.notNull(delegate, "propertiesLoader cannot be null");
        this.delegate = delegate;
        this.propertiesLoader = propertiesLoader;
    }

    /**
     * Returns non-null list of Spring context locations
     */
    public final List<String> getContextLocations() {
        
        Properties properties = getProperties();
        List<PropertySource> propertySources = getPropertySources(properties);
        
        PrefixedCompositePropertySource compositePropertySource = new PrefixedCompositePropertySource("impala.", propertySources);

        final ConfigurationSettings configSettings = new ConfigurationSettings();
        delegate.addContextLocations(configSettings, compositePropertySource);

        logger.info(configSettings);
        
        PropertySourceHolder.getInstance().setPropertySource(compositePropertySource);
        logger.info("Property source: " + compositePropertySource);
        
        return configSettings.getContextLocations();
    }

    protected abstract List<PropertySource> getPropertySources(Properties properties);
    
    protected Properties getProperties() {
        final Properties properties = propertiesLoader.loadProperties();
        PropertiesHolder.getInstance().setProperties(properties);
        return properties;
    }
}
