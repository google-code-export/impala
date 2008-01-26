package org.impalaframework.module.holder;

import org.impalaframework.module.modification.ModuleStateChange;

public interface ModuleStateChangeNotifier {

	void notify(ModuleStateHolder moduleStateHolder, ModuleStateChange change);

}