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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.spi.ModuleStateChange;
import org.impalaframework.module.spi.ModuleStateChangeListener;
import org.impalaframework.module.spi.ModuleStateChangeNotifier;
import org.impalaframework.module.spi.ModuleStateHolder;

public class DefaultModuleStateChangeNotifier implements ModuleStateChangeNotifier {

	private List<ModuleStateChangeListener> listeners = new LinkedList<ModuleStateChangeListener>();

	public void notify(ModuleStateHolder moduleStateHolder, ModuleStateChange change) {
		ModuleDefinition moduleDefinition = change.getModuleDefinition();

		for (ModuleStateChangeListener moduleStateChangeListener : listeners) {
			String moduleName = moduleStateChangeListener.getModuleName();

			boolean notify = true;

			if (moduleName != null) {
				if (!moduleName.equals(moduleDefinition.getName())) {
					notify = false;
				}
			}

			if (notify) {

				String transition = moduleStateChangeListener.getTransition();

				if (transition != null) {
					if (!transition.equals(change.getTransition())) {
						notify = false;
					}
				}

			}

			if (notify) {
				moduleStateChangeListener.moduleStateChanged(moduleStateHolder, change);
			}
		}
	}

	public void setListeners(List<ModuleStateChangeListener> listeners) {
		this.listeners.clear();
		this.listeners.addAll(listeners);
	}

	public void addListener(ModuleStateChangeListener listener) {
		this.listeners.add(listener);
	}

	public boolean removeListener(ModuleStateChangeListener listener) {
		return this.listeners.remove(listener);
	}

	List<ModuleStateChangeListener> getListeners() {
		return Collections.unmodifiableList(listeners);
	}

}
