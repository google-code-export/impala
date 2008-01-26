package org.impalaframework.module.holder;

import org.impalaframework.module.modification.ModuleStateChange;

//FIXME rename
public interface IModuleStateChangeNotifier {

	void notify(ModuleStateHolder moduleStateHolder, ModuleStateChange change);

}