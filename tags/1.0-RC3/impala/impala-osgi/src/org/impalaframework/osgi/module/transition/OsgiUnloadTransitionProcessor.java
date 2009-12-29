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

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.transition.UnloadTransitionProcessor;
import org.impalaframework.osgi.util.OsgiUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.springframework.osgi.context.BundleContextAware;

/**
 * Extends {@link UnloadTransitionProcessor} by uninstalling bundle, but only after performing the 
 * superclass's {@link #process(Application, RootModuleDefinition, ModuleDefinition)} operation.
 * @author Phil Zoio
 */
public class OsgiUnloadTransitionProcessor extends UnloadTransitionProcessor implements BundleContextAware {
    
    private BundleContext bundleContext;

    public OsgiUnloadTransitionProcessor() {
        super();
    }

    @Override
    public void process(Application application,
            RootModuleDefinition newRootDefinition, ModuleDefinition currentDefinition) {
        
        super.process(application, newRootDefinition, currentDefinition);
        
        findAndUnloadBundle(currentDefinition);
    }

    boolean findAndUnloadBundle(ModuleDefinition currentDefinition) {
        boolean process = true;
        
        //find bundle with name
        Bundle bundle = findBundle(currentDefinition);
        
        if (bundle != null) {
            process = OsgiUtils.stopBundle(bundle);
        }
        
        return process;
    }

    Bundle findBundle(ModuleDefinition currentDefinition) {
        Bundle bundle = OsgiUtils.findBundle(bundleContext, currentDefinition.getName());
        return bundle;
    }

    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

}
