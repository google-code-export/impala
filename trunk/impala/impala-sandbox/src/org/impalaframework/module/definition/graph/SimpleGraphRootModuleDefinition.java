package org.impalaframework.module.definition.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionUtils;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.springframework.util.Assert;

//TODO when implementing extended modification extractor make sure to pick up
//changes in the siblings

//Also, modification extractor needs to make sure that dependees 
//are all included in the modification list
public class SimpleGraphRootModuleDefinition extends SimpleRootModuleDefinition
		implements GraphRootModuleDefinition {

	private static final long serialVersionUID = 1L;
	
	private List<String> dependencies;
	
	private List<ModuleDefinition> siblings;

	public SimpleGraphRootModuleDefinition(
			String projectName,
			List<String> contextLocations,
			List<String> dependencies,
			List<ModuleDefinition> siblings) {
		super(Collections.singletonList(projectName), contextLocations);
		
		Assert.notNull(dependencies, "dependencies cannot be null. Use empty list instead");
		Assert.notNull(siblings, "siblings cannot be null. Use empty list instead");

		this.siblings = new ArrayList<ModuleDefinition>(siblings);
		this.dependencies = new ArrayList<String>(dependencies);
	}

	public String[] getDependentModuleNames() {
		//FIXME test
		return dependencies.toArray(new String[0]);
	}

	public ModuleDefinition[] getSiblings() {
		//FIXME test
		return siblings.toArray(new ModuleDefinition[0]);
	}
	
	public boolean hasSibling(String name) {
		return (getSiblingModule(name) != null);
	}
	
	@Override
	public ModuleDefinition findChildDefinition(String moduleName,
			boolean exactMatch) {
		//FIXME test
		ModuleDefinition child = super.findChildDefinition(moduleName, exactMatch);
		
		if (child != null)	
			return child;
		
		for (ModuleDefinition moduleDefinition : siblings) {
			child = ModuleDefinitionUtils.findDefinition(moduleName, moduleDefinition, exactMatch);
			if (child != null) {
				return child;
			}
		}
		
		return null;		
	}

	public ModuleDefinition getSiblingModule(String name) {
		List<ModuleDefinition> newSibs = siblings;
		for (ModuleDefinition moduleDefinition : newSibs) {
			if (moduleDefinition.getName().equals(name)) {
				return moduleDefinition;
			}
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((dependencies == null) ? 0 : dependencies.hashCode());
		result = prime * result
				+ ((siblings == null) ? 0 : siblings.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		
		//FIXME test - check for example that different siblings does not prevent equality
		
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SimpleGraphRootModuleDefinition other = (SimpleGraphRootModuleDefinition) obj;
		if (dependencies == null) {
			if (other.dependencies != null)
				return false;
		} else if (!dependencies.equals(other.dependencies))
			return false;
		return true;
	}	

}
