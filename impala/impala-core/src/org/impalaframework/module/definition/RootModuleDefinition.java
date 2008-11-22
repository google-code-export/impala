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

package org.impalaframework.module.definition;

import java.util.List;

//FIXME document
//FIXME add graph related methods
//FIXME add Freezable interface to cover methods for freezing
//FIXME extend MutableRootModuleDefinition subinterface to cover mutation operations
public interface RootModuleDefinition extends ModuleDefinition {
	
	boolean containsAll(RootModuleDefinition alternative);

	void addContextLocations(RootModuleDefinition alternative);
	
	List<ModuleDefinition> getSiblings();
	
	boolean hasSibling(String name);
	
	ModuleDefinition getSiblingModule(String name);

}
