package org.impalaframework.module.monitor;

/**
 * @author Phil Zoio
 */
public class ModuleChangeInfo {
	private String moduleName;

	public ModuleChangeInfo(String moduleName) {
		super();
		this.moduleName = moduleName;
	}

	public String getModuleName() {
		return moduleName;
	}

	public String toString() {
		return this.getClass().getName() + ": " + moduleName;
	}
	
}
