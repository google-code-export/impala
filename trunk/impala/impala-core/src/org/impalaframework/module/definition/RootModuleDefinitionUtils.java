package org.impalaframework.module.definition;

import java.util.Collections;
import java.util.List;

import org.impalaframework.constants.LocationConstants;
import org.impalaframework.exception.ConfigurationException;

public abstract class RootModuleDefinitionUtils {

	public static List<String> getProjectNameList(ModuleDefinition moduleDefinition) {
		if (!(moduleDefinition instanceof RootModuleDefinition)) {
			throw new ConfigurationException("Attempting to get root project names from module definition instance [" + moduleDefinition + "], an instance of " + moduleDefinition.getClass().getName() + ", which is not an instance of " + RootModuleDefinition.class.getName());
		}
	
		RootModuleDefinition rootModuleDefinition = (RootModuleDefinition) moduleDefinition;
		List<String> projectNameList = Collections.singletonList(rootModuleDefinition.getName());
		return projectNameList;
	}
	
	public static String getRootProject() {
		return System.getProperty(LocationConstants.ROOT_PROJECT_PROPERTY);
	}

}
