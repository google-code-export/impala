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

package org.impalaframework.module.holder;

import org.impalaframework.module.modification.ModuleStateChange;
import org.impalaframework.module.modification.Transition;

/**
 * Classes implementing this interface and subscribing to a {@link ModuleStateChangeNotifier} can be notified of module state changes events.
 * 
 * @see ModuleStateChangeNotifier
 * @author Phil Zoio
 */
public interface ModuleStateChangeListener {

	public void moduleStateChanged(ModuleStateHolder moduleStateHolder, ModuleStateChange change);

	/**
	 * If listener is only interested in a partiuclar module, then listener
	 * implementation can return the name of the interested module
	 * @return name of module to receive only events relating to a particular
	 * module. Otherwise, null
	 */
	public String getModuleName();

	/**
	 * If listener is only interested in a partiuclar transition, then listener
	 * implementation can return this transition
	 * @return transition to receive events relating to a particular module.
	 * Otherwise, null
	 */
	public Transition getTransition();
}
