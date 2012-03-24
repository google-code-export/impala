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

package org.impalaframework.module.holder.graph;

import org.impalaframework.module.definition.DependencyManager;
import org.impalaframework.module.holder.DefaultModuleStateHolder;

/**
 * Extension of {@link DefaultModuleStateHolder}, which also holds a reference
 * to the {@link DependencyManager} used to arrange modules in a graph.
 */
public class GraphModuleStateHolder extends DefaultModuleStateHolder {

    private DependencyManager dependencyManager;

    public void setDependencyManager(DependencyManager dependencyManager) {
        this.dependencyManager = dependencyManager;
    }

    public DependencyManager getDependencyManager() {
        return dependencyManager;
    }

}
