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

package org.impalaframework.osgi.util;

import org.impalaframework.facade.InternalOperationsFacade;
import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.facade.OperationsFacade;
import org.impalaframework.util.ObjectUtils;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public abstract class ImpalaOsgiUtils {

    /**
     * Returns the Impala {@link ModuleManagementFacade} from the OSGi service registry.
     * Will be null if Impala has not been initialised, typically via an Impala {@link BundleActivator} instance.
     */
    public static ModuleManagementFacade getManagementFacade(BundleContext context) {
        
        InternalOperationsFacade facade = ImpalaOsgiUtils.getOperationsFacade(context);
        return facade.getModuleManagementFacade();
    }

    public static InternalOperationsFacade getOperationsFacade(BundleContext context) {
        
        ServiceReference serviceReference = context.getServiceReference(OperationsFacade.class.getName());
        InternalOperationsFacade facade = ObjectUtils.cast(context.getService(serviceReference), InternalOperationsFacade.class);
        return facade;
    }

}
