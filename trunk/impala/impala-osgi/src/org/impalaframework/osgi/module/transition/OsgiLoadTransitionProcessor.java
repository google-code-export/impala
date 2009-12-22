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

package org.impalaframework.osgi.module.transition;

import java.util.List;

import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.transition.LoadTransitionProcessor;
import org.impalaframework.osgi.util.OsgiUtils;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.util.ResourceUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.springframework.core.io.Resource;
import org.springframework.osgi.context.BundleContextAware;
import org.springframework.util.Assert;

/**
 * Extends {@link LoadTransitionProcessor} by installing and, if necessary, starting the bundle applied by the currently
 * processed {@link ModuleDefinition} instance.
 * @see OsgiUnloadTransitionProcessor
 * @author Phil Zoio
 */
public class OsgiLoadTransitionProcessor extends LoadTransitionProcessor implements BundleContextAware {
    
    private BundleContext bundleContext;
    
    private ModuleLocationResolver moduleLocationResolver;

    public OsgiLoadTransitionProcessor() {
        super();
    }

    @Override
    public void process(Application application,
            RootModuleDefinition newRootDefinition, ModuleDefinition currentDefinition) {
        
        findAndStartBundle(currentDefinition);
        super.process(application, newRootDefinition, currentDefinition);
    }

    void findAndStartBundle(ModuleDefinition currentDefinition) {
        Assert.notNull(currentDefinition, "moduleDefinition cannot be null");
                
        List<Resource> locations = moduleLocationResolver.getApplicationModuleClassLocations(currentDefinition.getName());
        final Resource[] bundleLocations = ResourceUtils.toArray(locations);
        
        //find bundle with name
        Bundle bundle = findBundle(currentDefinition);

        if (bundleLocations == null || bundleLocations.length == 0) {
            throw new InvalidStateException("Module location resolver '" + moduleLocationResolver.getClass().getName() 
                    + "' returned " + (bundleLocations != null ? "empty": "null") + 
                    " bundle class location array. Cannot install bundle for module '" 
                    + currentDefinition.getName() + "'");
        }

        //if bundle is not present, then install otherwise update
        if (bundle == null) {
            bundle = OsgiUtils.installBundle(bundleContext, bundleLocations[0]);
        } else {
            OsgiUtils.updateBundle(bundle, bundleLocations[0]);
        }
        
        final int bundleState = bundle.getState();
        if (bundleState != Bundle.ACTIVE) {
            OsgiUtils.startBundle(bundle);
        }
    }

    Bundle findBundle(ModuleDefinition currentDefinition) {
        Bundle bundle = OsgiUtils.findBundle(bundleContext, currentDefinition.getName());
        return bundle;
    }
    
    public void setModuleLocationResolver(ModuleLocationResolver moduleLocationResolver) {
        this.moduleLocationResolver = moduleLocationResolver;
    }

    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

}
