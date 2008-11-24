package org.impalaframework.module.definition;

import org.impalaframework.constants.LocationConstants;

public abstract class RootModuleDefinitionUtils {

	public static String getRootProject() {
		return System.getProperty(LocationConstants.ROOT_PROJECT_PROPERTY);
	}

}
