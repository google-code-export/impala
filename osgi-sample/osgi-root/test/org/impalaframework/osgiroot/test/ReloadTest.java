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

package org.impalaframework.osgiroot.test;

import org.impalaframework.facade.OperationsFacade;
import org.impalaframework.interactive.definition.source.TestDefinitionSource;
import org.impalaframework.module.RootModuleDefinition;
import org.osgi.framework.ServiceReference;

public class ReloadTest extends OsgiContextTest {

    public ReloadTest() {
        super();
        System.setProperty("impala.module.class.dir", "target/classes");
    }
    
    public void testOsgiEnvironment() throws Exception {
        
        System.out.println("Starting reload of --------------------- ");
        
        ServiceReference serviceReference = bundleContext.getServiceReference(OperationsFacade.class.getName());
        OperationsFacade facade = (OperationsFacade) bundleContext.getService(serviceReference);
        facade.reloadModule("osgi-root");
        
        System.out.println("Finished reloading module --------------------- ");
    }

    public RootModuleDefinition getModuleDefinition() {
        return new TestDefinitionSource("osgi-root", "osgi-module1").getModuleDefinition();
    }
    
}
