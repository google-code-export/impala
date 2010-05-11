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

package org.impalaframework.module.modification.graph;

import java.util.Collection;
import java.util.List;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionUtils;
import org.impalaframework.module.modification.StickyModificationExtractor;
import org.impalaframework.module.modification.StrictModificationExtractor;
import org.impalaframework.module.spi.ModuleStateChange;

/**
 * Extends {@link GraphModificationExtractorDelegate}, allowing existing already present module definitions
 * to be retained even if they are not present in the new {@link RootModuleDefinition} hierarchy. 
 * This is useful when running a suite of integration tests. Modules which aren't explicitly declared
 * as being used in a test can be retained based on the assumption that they may be useful in subsequent
 * tests. Prevents unnecessary unloading and reloading of modules.
 * 
 * This implementation also allows context locations to be added to the root module definition without
 * requiring the root module definition to reload.
 * 
 * Note that this class is almost identical in content to {@link StickyModificationExtractor}. The difference is that
 * it extends different base classes. This class extends {@link GraphModificationExtractorDelegate}, while 
 * {@link StickyModificationExtractor} extends {@link StrictModificationExtractor}
 * 
 * @see StickyModificationExtractor
 * @see GraphModificationExtractorDelegate
 * @see StrictModificationExtractor
 * @author Phil Zoio
 */
public class StickyGraphModificationExtractorDelegate extends GraphModificationExtractorDelegate implements GraphAwareModificationExtractor {   
    
    @Override
    protected void checkOriginal(
            ModuleDefinition oldParent, 
            ModuleDefinition newParent, 
            Collection<ModuleDefinition> oldChildren, 
            Collection<ModuleDefinition> newChildren, 
            List<ModuleStateChange> transitions) {
    
        for (ModuleDefinition oldChild : oldChildren) {
            ModuleDefinition newChild = ModuleDefinitionUtils.getModuleFromCollection(newChildren, oldChild.getName());

            if (newChild == null) {
                newParent.addChildModuleDefinition(oldChild);
                oldChild.setParentDefinition(newParent);
            }
        }
    }

}
