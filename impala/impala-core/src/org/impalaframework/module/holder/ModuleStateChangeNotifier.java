package org.impalaframework.module.holder;

import org.impalaframework.module.modification.ModuleStateChange;

public interface ModuleStateChangeNotifier {

	void notify(ModuleStateHolder moduleStateHolder, ModuleStateChange change);
	public void addListener(ModuleStateChangeListener listener);
	public boolean removeListener(ModuleStateChangeListener listener);
	
}