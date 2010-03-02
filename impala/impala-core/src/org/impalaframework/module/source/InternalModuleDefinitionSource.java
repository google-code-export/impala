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

package org.impalaframework.module.source;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.ModuleDefinitionSource;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.spi.ModuleElementNames;
import org.impalaframework.module.type.TypeReaderRegistry;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.springframework.util.Assert;

/**
 * Implementation of <code>ModuleDefinitionSource</code> which relies on the
 * presence of a "module.properties" file in the root of the module classpath
 * @author Phil Zoio
 */
public class InternalModuleDefinitionSource extends BaseInternalModuleDefinitionSource implements ModuleDefinitionSource {

    private static Log logger = LogFactory.getLog(InternalModuleDefinitionSource.class);    
    
    private String[] moduleNames;

    private String rootModuleName;
    
    private TypeReaderRegistry typeReaderRegistry;
    
    public InternalModuleDefinitionSource(TypeReaderRegistry typeReaderRegistry, ModuleLocationResolver resolver, String[] moduleNames) {
        this(typeReaderRegistry, resolver, moduleNames, true);
    }

    public InternalModuleDefinitionSource(TypeReaderRegistry typeReaderRegistry, ModuleLocationResolver resolver, String[] moduleNames, boolean loadDependendentModules) {
        super(resolver, loadDependendentModules);
        Assert.notNull(moduleNames, "moduleNames cannot be null");
        Assert.notNull(typeReaderRegistry, "typeReaderRegistry cannot be null");
        this.moduleNames = moduleNames;
        this.typeReaderRegistry = typeReaderRegistry;
    }

    public RootModuleDefinition getModuleDefinition() {
        inspectModules();
        return buildModules();
    }

    protected RootModuleDefinition buildModules() {
        ModuleDefinitionSource internalModuleBuilder = getModuleBuilder();
        return internalModuleBuilder.getModuleDefinition();
    }

    protected ModuleDefinitionSource getModuleBuilder() {
        
        Set<String> siblings = getSiblings();
        
        InternalPropertiesModuleDefinitionSource internalModuleBuilder = new InternalPropertiesModuleDefinitionSource(typeReaderRegistry, rootModuleName, getModuleProperties(), getChildren(), siblings);
        return internalModuleBuilder;
    }

    Set<String> getSiblings() {
        //siblings are simply the orphans, excluding the root project name
        Set<String> siblings = new LinkedHashSet<String>(getOrphans());
        siblings.remove(rootModuleName);
        return siblings;
    }

    void inspectModules() {
        buildMaps();
        this.rootModuleName = determineRootDefinition();
    }

    void buildMaps() {
        String[] thisModuleNames = this.moduleNames;
        buildMaps(thisModuleNames);
    }

    void buildMaps(String[] thisModuleNames) {
        String[] moduleNames = thisModuleNames;
        while (moduleNames.length != 0) {
            loadProperties(moduleNames);
            extractParentsAndChildren(moduleNames);
            String[] added = addDependentModuleProperties(moduleNames);
            extractParentsAndChildren(added);
            moduleNames = buildMissingModules();
        }
    }

    String determineRootDefinition() {  
        
        if (getOrphans().isEmpty()) {
            if (moduleNames.length == 0) {
                return null;
            }
            throw new ConfigurationException("Module hierarchy does not have a root module. At least one module with no parent needs to be specified");
        }
        
        String firstModule = getOrphans().iterator().next();
        if (getOrphans().size() == 1) {
            return firstModule;
        }
        
        String rootModuleName = null;
        
        //look got module of type root
        for (String orphan : getOrphans()) {
            Map<String, Properties> moduleProperties = getModuleProperties();
            Properties properties = moduleProperties.get(orphan);
            Assert.notNull(properties, "Module " + orphan + " has no associated properties");
            
            String type = properties.getProperty(ModuleElementNames.TYPE_ELEMENT);
            if (ModuleTypes.ROOT.equalsIgnoreCase(type)) {
                rootModuleName = orphan;
            }
        }
        
        if (rootModuleName != null) {
            return rootModuleName;
        }
        
        logger.warn("The module definition hierarchy contains more than one module which has no parent, " +
                "and hence could be used as a root module.");
        logger.warn("Modules with no parents: " + getOrphans() + ". Module selected as root module: " + firstModule);
        return firstModule;
    }

    protected String getRootModuleName() {
        return rootModuleName;
    }

}
