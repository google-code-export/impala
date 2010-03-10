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

package org.impalaframework.spring.module.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.holder.graph.GraphModuleStateHolder;
import org.springframework.context.ApplicationContext;

/**
 * With this strategy implementation, the parent hierarchy is effectively traversed in attempting to obtain a bean, in the same 
 * way as regular Spring {@link ApplicationContext} implementations. The difference is that if the bean is not 
 * found in the parent hierarchy, a search is performed on remaining application contexts representing modules
 * on which the client module depends. The search excludes {@link ApplicationContext}s in the parent hierarchy, and
 * searches up the dependency chain.
 * 
 * For example if module C has parent B, which has parent A, and C is also dependent on F, which itself depends on E, then the 
 * search order will be C -> B -> A -> F -> E
 * 
 * This features should be used with caution. For example, if E has parent A and both contain bean definitions for a bean
 * named "myBean", then the definition of A's will be visible to C, because A is a direct ancestor of C.
 * 
 * @author Phil Zoio
 */
public class ParentFirstBeanGraphInheritanceStrategy extends BaseBeanGraphInheritanceStrategy {

    protected boolean getDelegateGetBeanCallsToParent() {
        return true;
    }
    
    /**
     * Returns the dependent {@link ApplicationContext}s for the current module, excluding those which are direct dependents
     */
    protected List<ApplicationContext> getDependentApplicationContexts(
            ModuleDefinition definition,
            ApplicationContext parentApplicationContext,
            GraphModuleStateHolder graphModuleStateHolder) {
        
        final List<ApplicationContext> applicationContexts = getDependentApplicationContexts(
                definition, graphModuleStateHolder);
        
        List<ApplicationContext> parentList = new ArrayList<ApplicationContext>();
        parentList.add(parentApplicationContext);
        
        if (parentApplicationContext != null) {
            ApplicationContext hierarchyParent = parentApplicationContext;
            while ((hierarchyParent = hierarchyParent.getParent()) != null) {
                parentList.add(hierarchyParent);
            }
        }
        
        applicationContexts.removeAll(parentList);

        //reverse the ordering so that the closest dependencies appear first
        Collections.reverse(applicationContexts);
        return applicationContexts;
    }
    
}
