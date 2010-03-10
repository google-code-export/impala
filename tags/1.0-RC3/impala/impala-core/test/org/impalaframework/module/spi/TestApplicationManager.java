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

package org.impalaframework.module.spi;

import org.impalaframework.service.ServiceRegistry;

/**
 * {@link ApplicationManager} which takes the {@link Application} to use in the constructor
 * @author Phil Zoio
 */
public class TestApplicationManager implements ApplicationManager {

    private Application application;

    public TestApplicationManager(Application application) {
        super();
        this.application = application;
    }
    
    public Application getCurrentApplication() {
        return application;
    }
    
    public Application getApplication(String id) {
        return application;
    }
    
    public static ApplicationManager newApplicationManager(
            ClassLoaderRegistry classLoaderRegistry,
            ModuleStateHolder moduleStateHolder,
            ServiceRegistry serviceRegistry) {
        return new TestApplicationManager(new TestApplication(classLoaderRegistry, moduleStateHolder, serviceRegistry));
    }

    public static ApplicationManager newApplicationManager() {
        return new TestApplicationManager(new TestApplication(null, null, null));
    }   
    
    public boolean close() {
        return true;
    }
    
}
