/*
 * Copyright 2007-2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.command.interactive;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.impalaframework.command.framework.CommandPropertyValue;
import org.impalaframework.command.framework.GlobalCommandState;
import org.impalaframework.constants.LocationConstants;
import org.impalaframework.exception.NoServiceException;
import org.impalaframework.facade.Impala;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.util.MemoryUtils;
import org.springframework.util.StopWatch;

public class InteractiveCommandUtils {
	
	public static void printException(Throwable e) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		e.printStackTrace(printWriter);
		System.out.println(stringWriter.toString());
	}

	public static void printReloadInfo(String suppliedName, String actualName, StopWatch watch) {
		if (actualName != null) {
			System.out.println("Module '" + actualName + "' loaded in " + watch.getTotalTimeSeconds() + " seconds");
			System.out.println(MemoryUtils.getMemoryInfo());
		}
		else {
			System.out.println("No module found which matches the text '" + suppliedName + "'");
		}
	}
	
	public static boolean isRootProject(String directoryName) {
		List<String> projectList = getRootProjectList();
		if (projectList.contains(directoryName.trim())) {
			return true;
		}
		return false;
	}

	static List<String> getRootProjectList() {
		String rootProjectsString = System.getProperty(LocationConstants.ROOT_PROJECTS_PROPERTY);
		
		if (rootProjectsString == null) {
			RootModuleDefinition md;
			try {
				md = Impala.getRootModuleDefinition();
				return md.getRootProjectNames();
			} catch (NoServiceException e) {
				CommandPropertyValue property = GlobalCommandState.getInstance().getProperty(CommandStateConstants.DEBUG_MODE);
				if (property != null && "true".equalsIgnoreCase(property.getValue())) {
					e.printStackTrace();
				}
			}		
			return Collections.emptyList();
		}
		
		String[] projects = rootProjectsString.split(",");
		for (int i = 0; i < projects.length; i++) {
			projects[i] = projects[i].trim();
		}
		
		List<String> projectList = Arrays.asList(projects);
		return projectList;
	}
	
}
