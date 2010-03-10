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

package org.impalaframework.facade;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.bootstrap.ContextLocationResolver;
import org.impalaframework.bootstrap.SimpleContextLocationResolver;
import org.impalaframework.bootstrap.SimpleLocationsRetriever;
import org.impalaframework.config.SimplePropertiesLoader;
import org.impalaframework.util.InstantiationUtils;

public class BootstrappingOperationFacade extends BaseOperationsFacade {

    private static final Log logger = LogFactory.getLog(BootstrappingOperationFacade.class);
    
    @Override
    protected List<String> getBootstrapContextLocations() {
        
        final String defaultResourceName = getDefaultResourceName();
        final String string = getContextLocationResolverClassName();
        SimpleLocationsRetriever retriever = new SimpleLocationsRetriever(getContextLocationResolver(string), new SimplePropertiesLoader(defaultResourceName));
        
        return retriever.getContextLocations();
    }

    protected String getContextLocationResolverClassName() {
        return SimpleContextLocationResolver.class.getName();
    }

    private ContextLocationResolver getContextLocationResolver(String className) {
        ContextLocationResolver c = InstantiationUtils.instantiate(className);
        
        logger.info("Using " + ContextLocationResolver.class.getSimpleName() + ": " + c.getClass().getName());
        return c;
    }
    
    protected String getDefaultResourceName() {
        return "impala-interactive.properties";
    }

}
