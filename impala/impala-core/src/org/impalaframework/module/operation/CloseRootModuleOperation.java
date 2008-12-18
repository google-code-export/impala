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

package org.impalaframework.module.operation;

import org.impalaframework.module.ModificationExtractor;
import org.impalaframework.module.ModificationExtractorType;
import org.impalaframework.module.ModuleStateHolder;
import org.impalaframework.module.TransitionSet;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.modification.ModificationExtractorRegistry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * {@link ModuleOperation} implementation which encapsulates operation for shutting down entire module
 * hierarchy/graph, starting from the root module.
 * 
 * @author Phil Zoio
 */
public class CloseRootModuleOperation extends BaseModuleOperation {

	private static final Log logger = LogFactory.getLog(CloseRootModuleOperation.class);

	protected CloseRootModuleOperation() {
		super();
	}

	public ModuleOperationResult doExecute(ModuleOperationInput moduleOperationInput) {
		
		ModuleStateHolder moduleStateHolder = getModuleStateHolder();
		ModificationExtractorRegistry modificationExtractorRegistry = getModificationExtractorRegistry();
		ModificationExtractor calculator = modificationExtractorRegistry
				.getModificationExtractor(ModificationExtractorType.STRICT);
		RootModuleDefinition rootModuleDefinition = moduleStateHolder.getRootModuleDefinition();
		
		if (rootModuleDefinition != null) {
			logger.info("Shutting down application context");
			TransitionSet transitions = calculator.getTransitions(rootModuleDefinition, null);
			moduleStateHolder.processTransitions(transitions);
			return ModuleOperationResult.TRUE;
		}
		else {
			return ModuleOperationResult.FALSE;
		}
	}

}
