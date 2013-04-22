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

package org.impalaframework.interactive.web.facade;

import org.impalaframework.bootstrap.CoreBootstrapProperties;
import org.impalaframework.interactive.facade.InteractiveOperationsFacade;
import org.impalaframework.interactive.web.bootstrap.WebTestContextLocationResolver;
import org.impalaframework.startup.ContextStarter;
import org.impalaframework.web.facade.WebContextStarter;

public class InteractiveWebOperationsFacade extends InteractiveOperationsFacade {

    @Override
    protected void init() {
        System.setProperty(CoreBootstrapProperties.EMBEDDED_MODE, "true");
        super.init();
    }
    
    @Override
    protected String getContextLocationResolverClassName() {
        return WebTestContextLocationResolver.class.getName();
    }
    
    @Override
    protected ContextStarter getContextStarter() {
        return new WebContextStarter();
    }
    
}
