package org.impalaframework.module.definition.graph;

import java.util.List;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;

public class SimpleGraphModuleDefinition extends SimpleModuleDefinition implements GraphModuleDefinition {

	private static final long serialVersionUID = 1L;
	
	private List<String> dependencies;
	
	public SimpleGraphModuleDefinition(String name) {
		super(name);
	}
	
	public SimpleGraphModuleDefinition(String name, List<String> dependencies) {
		super(name);
		this.dependencies = dependencies;
	}

	public SimpleGraphModuleDefinition(ModuleDefinition parent, String name, List<String> dependencies) {
		super(parent, name);
		this.dependencies = dependencies;
	}
	
	public String[] getDependentModuleNames() {
		final String parentName = getParentDefinition().getName();
		if (dependencies == null) {
			return new String[] { parentName };
		}
		
		if (!dependencies.contains(parentName))
		{
			dependencies.add(0, parentName);
		}
		return dependencies.toArray(new String[0]);
	}

}
