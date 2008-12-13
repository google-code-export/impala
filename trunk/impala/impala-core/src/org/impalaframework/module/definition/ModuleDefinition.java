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

/**
 * {@link ModuleDefinition} exposes a number of methods which return information
 * on a module, such as name, type and the identity of parent and child modules.
 * ModuleDefinition has a similar role in Impala to the role of
 * {@link org.springframework.beans.factory.config.BeanDefinition}s in Spring.
 * 
 * A hierarchy of {@link ModuleDefinition}s can be considered an abstract
 * representation of the module hierarchy, which can be manipulated separately
 * from the runtime module hierarchy (which is essentially a hierarchy of Spring
 * application contexts). At the root of the a hierarchy of module definitions
 * is an instance of {@link RootModuleDefinition}, which exposes another couple
 * of methods specific to root modules.
 * 
 * @see RootModuleDefinition
 * @author Phil Zoio
 */
public interface ModuleDefinition extends ChildModuleContainer, ToStringAppendable {

	String getType();
	
	String getName();

	//FIXME renamed to getConfigLocations
	List<String> getContextLocations();
	
	ModuleDefinition getParentDefinition();

	ModuleDefinition findChildDefinition(String moduleName, boolean exactMatch);

	List<String> getDependentModuleNames();
	
	void setParentDefinition(ModuleDefinition moduleDefinition);

	void setState(ModuleState state);
	
	ModuleState getState();
	
	void freeze();
	
	void unfreeze();

	boolean isFrozen();

}
