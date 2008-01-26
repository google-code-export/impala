package org.impalaframework.module.holder;

import org.impalaframework.module.modification.ModuleStateChange;
import org.impalaframework.module.modification.Transition;

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
