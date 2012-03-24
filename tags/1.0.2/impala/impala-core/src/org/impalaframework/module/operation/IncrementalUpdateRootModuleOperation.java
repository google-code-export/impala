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

package org.impalaframework.module.operation;

import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.modification.StickyModificationExtractor;
import org.impalaframework.module.spi.ModificationExtractorType;

/**
 * Extends {@link ReloadRootModuleOperation}. The main difference is that it uses the {@link StickyModificationExtractor}
 * or similar class, with the result that modules not present in newly supplied {@link RootModuleDefinition} are not unloaded.
 * However, modified modules are reloaded, and new modules are added.
 * 
 * @author Phil Zoio
 */
public class IncrementalUpdateRootModuleOperation extends ReloadRootModuleOperation {

    protected IncrementalUpdateRootModuleOperation() {
        super();
    }

    protected ModificationExtractorType getModificationExtractorType() {
        return ModificationExtractorType.STICKY;
    }

}
