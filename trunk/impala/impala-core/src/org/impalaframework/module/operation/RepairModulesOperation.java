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
import org.impalaframework.module.modification.RepairModificationExtractor;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ModificationExtractorType;

/**
 * Implementation of {@link ModuleOperation} which uses {@link RepairModificationExtractor}
 * to fix modules which have previously failed to load.
 * 
 * @see ReloadModuleNamedLikeOperation
 * @see ReloadNamedModuleOperation
 * @author Phil Zoio
 */
public class RepairModulesOperation extends UpdateRootModuleOperation {

    protected RepairModulesOperation() {
        super();
    }

    @Override
    protected RootModuleDefinition getExistingModuleDefinitionSource(Application application) {
        return application.getModuleStateHolder().cloneRootModuleDefinition();
    }

    protected ModificationExtractorType getModificationExtractorType() {
        return ModificationExtractorType.REPAIR;
    }

}
