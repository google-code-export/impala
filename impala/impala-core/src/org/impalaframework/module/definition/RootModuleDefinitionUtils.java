package org.impalaframework.module.definition;

import java.util.Arrays;
import java.util.List;

import org.impalaframework.constants.LocationConstants;
import org.impalaframework.exception.ConfigurationException;
import org.springframework.util.StringUtils;

public abstract class RootModuleDefinitionUtils {

	public static List<String> getProjectNameList(ModuleDefinition moduleDefinition) {
		if (!(moduleDefinition instanceof RootModuleDefinition)) {
			throw new ConfigurationException("Attempting to get root project names from module definition instance [" + moduleDefinition + "], an instance of " + moduleDefinition.getClass().getName() + ", which is not an instance of " + RootModuleDefinition.class.getName());
		}
	
		RootModuleDefinition rootModuleDefinition = (RootModuleDefinition) moduleDefinition;
		List<String> projectNameList = rootModuleDefinition.getRootProjectNames();
		return projectNameList;
	}
	
	public static List<String> getRootProjectList() {
		String rootProjects = System.getProperty(LocationConstants.ROOT_PROJECTS_PROPERTY);
		if (rootProjects != null) {
			List<String> rootProjectList = Arrays.asList(StringUtils.tokenizeToStringArray(rootProjects, ",", true, true));
			return rootProjectList;
		}
		return null;
	}

}
