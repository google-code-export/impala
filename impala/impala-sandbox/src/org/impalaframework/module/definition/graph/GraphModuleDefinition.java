package org.impalaframework.module.definition.graph;

import org.impalaframework.module.definition.ModuleDefinition;

public interface GraphModuleDefinition extends ModuleDefinition {
	
	public String[] getDependentModuleNames();

}
