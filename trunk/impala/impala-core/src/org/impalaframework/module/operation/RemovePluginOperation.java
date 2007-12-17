package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.modification.ModificationCalculationType;
import org.impalaframework.module.modification.ModuleModificationCalculator;
import org.impalaframework.module.modification.ModuleTransitionSet;
import org.impalaframework.module.spec.RootModuleDefinition;
import org.impalaframework.module.spec.ModuleDefinition;
import org.impalaframework.module.transition.PluginStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class RemovePluginOperation implements PluginOperation {

	final Logger logger = LoggerFactory.getLogger(RemovePluginOperation.class);

	private final ModuleManagementSource factory;

	private final String pluginToRemove;

	public RemovePluginOperation(final ModuleManagementSource factory, final String pluginToRemove) {
		super();
		Assert.notNull(factory);
		Assert.notNull(pluginToRemove);
		this.factory = factory;
		this.pluginToRemove = pluginToRemove;
	}

	public boolean execute() {
		
		//FIXME comment and test
		
		PluginStateManager pluginStateManager = factory.getPluginStateManager();
		ModuleModificationCalculator calculator = factory.getPluginModificationCalculatorRegistry().getPluginModificationCalculator(ModificationCalculationType.STRICT);
		return removePlugin(pluginStateManager, calculator, pluginToRemove);
	}
	
	public static boolean removePlugin(PluginStateManager pluginStateManager, ModuleModificationCalculator calculator,
			String plugin) {
		
		RootModuleDefinition oldSpec = pluginStateManager.getParentSpec();
		
		if (oldSpec == null) {
			return false;
		}
		
		RootModuleDefinition newSpec = pluginStateManager.cloneParentSpec();
		ModuleDefinition pluginToRemove = newSpec.findPlugin(plugin, true);

		if (pluginToRemove != null) {
			if (pluginToRemove instanceof RootModuleDefinition) {
				//we're removing the parent context, so new pluginSpec is null
				ModuleTransitionSet transitions = calculator.getTransitions(oldSpec, null);
				pluginStateManager.processTransitions(transitions);
				return true;
			}
			else {
				ModuleDefinition parent = pluginToRemove.getParent();
				if (parent != null) {
					ModuleDefinition remove = parent.remove(plugin);
					System.out.println("Removed: " + remove.getName());
					
					pluginToRemove.setParent(null);

					ModuleTransitionSet transitions = calculator.getTransitions(oldSpec, newSpec);
					pluginStateManager.processTransitions(transitions);
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
