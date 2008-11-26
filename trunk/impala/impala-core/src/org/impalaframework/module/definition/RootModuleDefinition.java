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

//TODO Ticket #103 - add Freezable interface to cover methods for freezing
//TODO Ticket #103 - extend MutableRootModuleDefinition subinterface to cover mutation
// operations
/**
 * Extension of {@link ModuleDefinition} with methods specific to the "root"
 * module. Note that only one module can be considered the root module. However,
 * not all modules need to have the root module as a direct ancestors. Modules
 * which fall into this group are treated as "siblings" of the root module, and
 * can be retrieved using {@link RootModuleDefinition#getSiblings()} and
 * {@link RootModuleDefinition#hasSibling(String)}.
 * 
 * The root module also supports the capability of dynamic adding module
 * definitions. If one {@link RootModuleDefinition} instance contains all the
 * same context locations as an alternative, then
 * {@link #containsAll(RootModuleDefinition)} returns true.
 * {@link #addContextLocations(RootModuleDefinition)} can be used to dynamically
 * append context locations to the root module without requiring the root module
 * to reload.
 */
public interface RootModuleDefinition extends ModuleDefinition {

	/**
	 * Returns true if current {@link RootModuleDefinition} instance contains
	 * all the same context locations as <code>alternative</code>. Otherwise
	 * returns false.
	 */
	boolean containsAll(RootModuleDefinition alternative);

	/**
	 * Can be used to dynamically add the context locations contained by the
	 * alternative to the current {@link RootModuleDefinition} instance.
	 */
	void addContextLocations(RootModuleDefinition alternative);

	/**
	 * Returns sibling modules, that is modules for which the current
	 * {@link RootModuleDefinition} instance is not an ancestor.
	 */
	List<ModuleDefinition> getSiblings();

	/**
	 * Returns true if this {@link RootModuleDefinition} instance has a sibling
	 * module with the specified name.
	 */
	boolean hasSibling(String name);

	/**
	 * Returns the named sibling module if present, otherwise returns null.
	 */
	ModuleDefinition getSiblingModule(String name);

	/**
	 * Adds a sibling to the root module definition
	 */
	void addSibling(ModuleDefinition siblingDefinition);

}
