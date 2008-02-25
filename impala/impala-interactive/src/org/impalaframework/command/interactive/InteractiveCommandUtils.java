package org.impalaframework.command.interactive;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.resolver.LocationConstants;
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
		List<String> projectList = getProjectList();
		if (projectList.contains(directoryName.trim())) {
			return true;
		}
		return false;
	}

	private static List<String> getProjectList() {
		String rootProjectsString = System.getProperty(LocationConstants.ROOT_PROJECTS_PROPERTY);
		
		if (rootProjectsString == null) {
			throw new ConfigurationException("'" + LocationConstants.ROOT_PROJECTS_PROPERTY + "' has not been specified");
		}
		
		String[] projects = rootProjectsString.split(",");
		for (int i = 0; i < projects.length; i++) {
			projects[i] = projects[i].trim();
		}
		
		List<String> projectList = Arrays.asList(projects);
		return projectList;
	}
	
}
