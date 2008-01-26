package org.impalaframework.module.holder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.modification.ModuleStateChange;

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
