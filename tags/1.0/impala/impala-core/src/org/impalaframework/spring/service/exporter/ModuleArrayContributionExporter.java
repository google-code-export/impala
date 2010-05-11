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

package org.impalaframework.spring.service.exporter;

import java.util.Arrays;

import org.impalaframework.service.NamedServiceEndpoint;
import org.springframework.util.Assert;

/**
 * Subclass of <code>BaseModuleContributionExporter</code> which will register
 * beans as services with the <code>ServiceRegistry</code> only if they are named in the 
 * <code>contributions</code> property, and their parent Spring context contains same
 * named beans which implement {@link NamedServiceEndpoint}
 * 
 * @see AutoRegisteringModuleContributionExporter
 * @author Phil Zoio
 */
public class ModuleArrayContributionExporter extends BaseModuleContributionExporter {
    
    private String[] contributions;

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(contributions, "contributions cannot be null");

        processContributions(Arrays.asList(contributions));
    }

    public void setContributions(String[] contributions) {
        this.contributions = contributions;
    }

}
