package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.modification.ModificationExtractorType;
import org.impalaframework.module.modification.ModificationExtractor;
import org.impalaframework.module.modification.TransitionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class RemoveModuleOperation implements ModuleOperation {

	final Logger logger = LoggerFactory.getLogger(RemoveModuleOperation.class);

	private final ModuleManagementSource factory;

	private final String pluginToRemove;

	public RemoveModuleOperation(final ModuleManagementSource factory, final String pluginToRemove) {
		super();
		Assert.notNull(factory);
		Assert.notNull(pluginToRemove);
		this.factory = factory;
		this.pluginToRemove = pluginToRemove;
	}

	public boolean execute() {
		
		//FIXME comment and test
		
		ModuleStateHolder moduleStateHolder = factory.getModuleStateHolder();
		ModificationExtractor calculator = factory.getPluginModificationCalculatorRegistry().getPluginModificationCalculator(ModificationExtractorType.STRICT);
		return removePlugin(moduleStateHolder, calculator, pluginToRemove);
	}
	
	public static boolean removePlugin(ModuleStateHolder moduleStateHolder, ModificationExtractor calculator,
			String plugin) {
		
		RootModuleDefinition oldSpec = moduleStateHolder.getRootModuleDefinition();
		
		if (oldSpec == null) {
			return false;
		}
		
		RootModuleDefinition newSpec = moduleStateHolder.cloneRootModuleDefinition();
		ModuleDefinition pluginToRemove = newSpec.findChildDefinition(plugin, true);

		if (pluginToRemove != null) {
			if (pluginToRemove instanceof RootModuleDefinition) {
				//we're removing the parent context, so new pluginSpec is null
				TransitionSet transitions = calculator.getTransitions(oldSpec, null);
				moduleStateHolder.processTransitions(transitions);
				return true;
			}
			else {
				ModuleDefinition parent = pluginToRemove.getRootDefinition();
				if (parent != null) {
					ModuleDefinition remove = parent.remove(plugin);
					System.out.println("Removed: " + remove.getName());
					
					pluginToRemove.setParentDefinition(null);

					TransitionSet transitions = calculator.getTransitions(oldSpec, newSpec);
					moduleStateHolder.processTransitions(transitions);
					return true;
				}
				else {
					throw new IllegalStateException("Plugin to remove does not have a parent plugin. "
							+ "This is unexpected state and may indicate a bug");
				}
			}
		}
		return false;
	}
	
}
