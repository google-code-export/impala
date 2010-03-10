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

import java.util.ArrayList;
import java.util.Collection;

import org.impalaframework.module.RootModuleDefinition;
import org.springframework.util.Assert;

public class TransitionSet {

    private Collection<? extends ModuleStateChange> moduleTransitions = new ArrayList<ModuleStateChange>();

    private RootModuleDefinition newDefinition;

    public TransitionSet(Collection<? extends ModuleStateChange> transitions, RootModuleDefinition newDefinition) {
        super();
        this.moduleTransitions = transitions;
        this.newDefinition = newDefinition;
        if (this.newDefinition != null) {
            Assert.isTrue(newDefinition.isFrozen());
        }
    }

    public RootModuleDefinition getNewRootModuleDefinition() {
        return newDefinition;
    }

    public Collection<? extends ModuleStateChange> getModuleTransitions() {
        return moduleTransitions;
    }

}
