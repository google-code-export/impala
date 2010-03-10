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

package org.impalaframework.module.source;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.ModuleDefinitionSource;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.util.PropertyUtils;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

/**
 * Base class for implementations of <code>ModuleDefinitionSource</code> which relies on the
 * presence of a "module.properties" file in the root of the module classpath
 * @author Phil Zoio
 */
public abstract class BaseInternalModuleDefinitionSource implements ModuleDefinitionSource {
    
    private static Log logger = LogFactory.getLog(BaseInternalModuleDefinitionSource.class);    
    
    private static final String PARENT_PROPERTY = "parent";
    
    private static final String DEPENDS_ON_PROPERTY = "depends-on";

    private static final String MODULE_PROPERTIES = "module.properties";

    private boolean loadDependendentModules;
    
    private Map<String, Properties> moduleProperties;
    
    private Map<String, String> parents;
    
    private Map<String, Set<String>> children;
    
    private Set<String> orphans;
    
    private ModuleLocationResolver moduleLocationResolver;

    public BaseInternalModuleDefinitionSource(ModuleLocationResolver resolver) {
        this(resolver, true);
    }

    public BaseInternalModuleDefinitionSource(ModuleLocationResolver resolver, boolean loadDependendentModules) {
        super();
        this.moduleLocationResolver = resolver;
        this.loadDependendentModules = loadDependendentModules;
        this.moduleProperties = new HashMap<String, Properties>();
        this.parents = new HashMap<String, String>();
        this.children = new HashMap<String, Set<String>>();
        this.orphans = new LinkedHashSet<String>();
    }

    String[] buildMissingModules() {
        List<String> missing = new ArrayList<String>();
        //go through and check that all modules have children but not parents
        for (String moduleName : children.keySet()) {
            
            if (!parents.containsKey(moduleName)) {
                if (!loadDependendentModules) {
                    throw new ConfigurationException("Module '" + moduleName + "' has not been explicitly mentioned, but loadDependentModules has been set to false");
                }
                missing.add(moduleName);
            }
        }
        return missing.toArray(new String[0]);
    }

    void extractParentsAndChildren(String[] moduleNames) {
        for (String moduleName : moduleNames) {
            Properties properties = moduleProperties.get(moduleName);
            
            String parent = properties.getProperty(PARENT_PROPERTY);
            if (parent != null) {
                parent = parent.trim();
                checkParent(parent, moduleName);
                Set<String> currentChildren = children.get(parent);
                if (currentChildren == null) {
                    currentChildren = new LinkedHashSet<String>();
                    children.put(parent, currentChildren);
                }
                currentChildren.add(moduleName);
            } else {
                //orphans are any modules with no parents
                orphans.add(moduleName);
            }
            parents.put(moduleName, parent);
            
        }
    }

    void checkParent(String parent, String moduleName) {
        if (moduleName.equals(parent)){
            throw new ConfigurationException("Module '" + moduleName + "' illegally declares itself as parent in " + MODULE_PROPERTIES);
        }
    }

    void loadProperties(String[] moduleNames) {
        for (String moduleName : moduleNames) {
            Properties properties = getPropertiesForModule(moduleName);
            moduleProperties.put(moduleName, properties);
        }
    }
    
    String[] addDependentModuleProperties(String[] moduleNames) {
        List<String> added = new ArrayList<String>();
        for (String moduleName : moduleNames) {
            final Properties properties = moduleProperties.get(moduleName);
            
            addDependentModuleProperties(moduleName, properties, added);
        }
        return added.toArray(new String[0]);
    }


    private void addDependentModuleProperties(String moduleName,
            Properties properties, List<String> added) {
        String dependsOnString = properties.getProperty(DEPENDS_ON_PROPERTY);
        if (StringUtils.hasText(dependsOnString)) {
            String[] dependents = StringUtils.tokenizeToStringArray(dependsOnString, " ,");
            for (String dependent : dependents) {
                if (!moduleProperties.containsKey(dependent)) {
                    Properties dependentProperties = getPropertiesForModule(dependent);
                    moduleProperties.put(dependent, dependentProperties);
                    added.add(dependent);
                }
            }
        }
    }

    protected Properties getPropertiesForModule(String moduleName) {
        URL resource = getResourceForModule(moduleName, MODULE_PROPERTIES);
        return PropertyUtils.loadProperties(resource);
    }

    URL getResourceForModule(String moduleName, String resourceName) {
        URL resource = ModuleResourceUtils.loadModuleResource(moduleLocationResolver, moduleName, resourceName);
        
        if (resource == null) {
            final List<Resource> classLocations = moduleLocationResolver.getApplicationModuleClassLocations(moduleName);
            
            logger.error("Problem location resources for module: " + moduleName + ". Locations being searched are " + (classLocations.isEmpty() ? "empty": "listed next:"));
            for (Resource classLocation : classLocations) {
                logger.error(classLocation.getDescription() + (classLocation.exists() ? ": is present on file system": " cannot be found"));
            }
            
            throw new ConfigurationException("Application is using internally defined module structure, but no " 
                    + MODULE_PROPERTIES + 
                    " file is present on the classpath for module '" + moduleName 
                    + "'. It must exist in one of the following locations: " + classLocations);
        }
        return resource;
    }

    protected Map<String, String> getParents() {
        return parents;
    }

    protected Map<String, Properties> getModuleProperties() {
        return moduleProperties;
    }

    protected Map<String, Set<String>> getChildren() {
        return children;
    }

    protected Set<String> getOrphans() {
        return orphans;
    }

}
