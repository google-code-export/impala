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

package org.impalaframework.jmx.bootstrap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.bootstrap.ConfigurationSettings;
import org.impalaframework.bootstrap.ContextLocationResolver;
import org.impalaframework.config.BooleanPropertyValue;
import org.impalaframework.config.IntPropertyValue;
import org.impalaframework.config.PropertySource;

public class JMXContextLocationResolver implements ContextLocationResolver {

    private static Log logger = LogFactory.getLog(JMXContextLocationResolver.class);
    
    public boolean addContextLocations(ConfigurationSettings configSettings,
            PropertySource propertySource) {

        //add jmx operartions
        addJmxOperations(configSettings, propertySource);
        
        //whether to add mx4j adaptor
        addMx4jAdaptorContext(configSettings, propertySource);
        
        return false;
    }

    void addMx4jAdaptorContext(ConfigurationSettings configSettings,
            PropertySource propertySource) {
        
        BooleanPropertyValue exposeMx4jAdaptor = new BooleanPropertyValue(propertySource, JMXBootstrapProperties.EXPOSE_MX4J_ADAPTOR, JMXBootstrapProperties.EXPOSE_MX4J_ADAPTOR_DEFAULT);
        configSettings.addProperty(JMXBootstrapProperties.EXPOSE_MX4J_ADAPTOR, exposeMx4jAdaptor);

        if (exposeMx4jAdaptor.getValue()) {
            
            IntPropertyValue adaptorPort = new IntPropertyValue(propertySource, JMXBootstrapProperties.JMX_ADAPTOR_PORT, JMXBootstrapProperties.JMX_ADAPTOR_PORT_DEFAULT);
            configSettings.addProperty(JMXBootstrapProperties.JMX_ADAPTOR_PORT, adaptorPort);
            
            if (configSettings.getContextLocations().contains("META-INF/impala-jmx-bootstrap.xml")) {

                boolean mx4jPresent = isMX4JPresent();

                if (mx4jPresent) {
                    configSettings.add("META-INF/impala-jmx-adaptor-bootstrap.xml");
                } else {
                    logger.warn("'expose.mx4j.adaptor' set to true MX4J classes not present. Not exposing Mx4j adaptor");
                }
            } else {
                logger.warn("'expose.mx4j.adaptor' set to true but 'expose.jmx.operations' set to false. Not exposing Mx4j adaptor");
            }
        }
    }
    
    protected void addJmxOperations(ConfigurationSettings configSettings,
            PropertySource propertySource) {
        BooleanPropertyValue exposeJmx = new BooleanPropertyValue(propertySource, JMXBootstrapProperties.EXPOSE_JMX_OPERATIONS, JMXBootstrapProperties.EXPOSE_JMX_OPERATIONS_DEFAULT);
        configSettings.addProperty(JMXBootstrapProperties.EXPOSE_JMX_OPERATIONS, exposeJmx);

        BooleanPropertyValue locateExistingMbeanServer = new BooleanPropertyValue(propertySource, JMXBootstrapProperties.JMX_LOCATE_EXISTING_SERVER, JMXBootstrapProperties.JMX_LOCATE_EXISTING_SERVER_DEFAULT);
        configSettings.addProperty(JMXBootstrapProperties.JMX_LOCATE_EXISTING_SERVER, locateExistingMbeanServer);
        
        if (exposeJmx.getValue()) {
            configSettings.add("META-INF/impala-jmx-bootstrap.xml");
        }
    }

    boolean isMX4JPresent() {
        boolean mx4jPresent = false;
        
        //Check for presence of MX4J packages
        try {
            Class.forName("mx4j.tools.adaptor.http.XSLTProcessor");
            mx4jPresent = true;
        } catch (ClassNotFoundException e) {
            mx4jPresent = false;
        }
        return mx4jPresent;
    }

}
