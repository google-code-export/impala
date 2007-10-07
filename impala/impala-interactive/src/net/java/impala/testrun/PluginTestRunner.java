/*
 * Copyright 2007 the original author or authors.
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

package net.java.impala.testrun;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import net.java.impala.classloader.ImpalaTestContextResourceHelper;
import net.java.impala.classloader.TestContextResourceHelper;
import net.java.impala.location.ClassLocationResolver;
import net.java.impala.location.StandaloneClassLocationResolverFactory;
import net.java.impala.spring.plugin.SpringContextSpec;
import net.java.impala.spring.util.ApplicationContextLoader;
import net.java.impala.testrun.spring.TestApplicationContextLoader;
import net.java.impala.util.MemoryUtils;
import net.java.impala.util.PathUtils;

import org.springframework.util.ClassUtils;
import org.springframework.util.StopWatch;

public class PluginTestRunner {

	public static void main(String[] args) {
		new PluginTestRunner().start(null);
	}

	public static void run(Class testClass) {
		new PluginTestRunner().start(testClass);
	}

	public PluginTestRunner() {
		super();
		if (System.getProperty("impala.plugin.prefix") == null) {
			System.setProperty("impala.plugin.prefix", PathUtils.getCurrentDirectoryName());
		}

		final ApplicationContextLoader loader = DynamicContextHolder.getContextLoader();
		if (loader == null) {
			DynamicContextHolder.setContextLoader(newContextLoader());
		}
	}

	private TestApplicationContextLoader newContextLoader() {
		ClassLocationResolver classLocationResolver = new StandaloneClassLocationResolverFactory()
				.getClassLocationResolver();
		TestContextResourceHelper resourceHelper = new ImpalaTestContextResourceHelper(classLocationResolver);
		return new TestApplicationContextLoader(classLocationResolver, resourceHelper);
	}

	/**
	 * Runs a suite extracted from a TestCase subclass.
	 */
	public void start(Class testClass) {

		PluginDataHolder holder = new PluginDataHolder();
		holder.testClass = testClass;

		setParentClassLoader(holder);

		while (true) {

			try {

				String command = readCommand(holder);

				if (command.equals("r")) {
					runCommand(holder, holder.lastCommand);
				}
				else {
					runCommand(holder, command);
					holder.lastCommand = command;
				}

			}
			catch (Exception e) {
				System.out.println("An error occurred: " + e.getMessage());
				e.printStackTrace();
			}
		}

	}

	private void runCommand(PluginDataHolder holder, String command) {
		if (command.startsWith("test")) {
			holder.methodName = command;
			runTest(holder);
		}
		else if (command.equals("t")) {
			runTest(holder);
		}
		else if (command.equals("s")) {
			showTestMethods(holder.testClass);
		}
		else if (command.equals("e")) {
			exit();
		}
		else if (command.equals("u")) {
			usage();
		}
		else if (command.startsWith("reload")) {
			String pluginName = command.substring("reload".length());
			String pluginToReload = pluginName.trim();
			if (pluginToReload.length() > 0) {
				reloadPlugin(pluginToReload);
			}
			else {
				reloadParent(holder);
			}
		}
		else if (command.startsWith("l")) {
			String pluginName = command.substring("l".length());
			String testClassName = pluginName.trim();
			if (testClassName.length() > 0) {
				loadTestClass(holder, testClassName);
			}
			else {
				System.out.println("Please specify test class name");
			}
		}
		else {
			System.out.println("Unrecognised command: " + command);
		}
	}

	private void reloadPlugin(String pluginToReload) {
		StopWatch watch = startWatch();

		if (DynamicContextHolder.reload(pluginToReload)) {
			watch.stop();
			System.out.println("Plugin " + pluginToReload + " loaded in " + watch.getTotalTimeSeconds() + " seconds");
			System.out.println(MemoryUtils.getMemoryInfo());
		}
	}

	private boolean reloadParent(PluginDataHolder holder) {
		StopWatch watch = startWatch();
		boolean reload = false;

		setParentClassLoader(holder);

		if (holder.pluginSpec != null)
			reload = DynamicContextHolder.reloadParent(holder.parentClassLoader, holder.pluginSpec);
		else
			reload = DynamicContextHolder.reloadParent(holder.parentClassLoader);

		if (reload) {
			watch.stop();
			System.out.println("Parent context loaded in " + watch.getTotalTimeSeconds() + " seconds");
			System.out.println(MemoryUtils.getMemoryInfo());
		}

		return reload;
	}

	private StopWatch startWatch() {
		StopWatch watch = new StopWatch();
		watch.start();
		return watch;
	}

	private void loadTestClass(PluginDataHolder holder, String testClassName) {
		Class c = null;
		try {
			c = Class.forName(testClassName);
			holder.testClass = c;

			try {
				Object o = c.newInstance();
				if (o instanceof SpringContextSpecAware) {
					SpringContextSpecAware p = (SpringContextSpecAware) o;
					holder.pluginSpec = p.getPluginSpec();
					reloadParent(holder);
				}
			}
			catch (Exception e) {
				System.out.println("Unable to instantiate " + testClassName);
			}
		}
		catch (ClassNotFoundException e) {
			System.out.println("Unable to find test class " + testClassName);
		}
	}

	private void exit() {
		System.out.println("Exiting");
		System.exit(0);
	}

	private void runTest(PluginDataHolder holder) {

		System.out.println("Running test " + holder.testClass.getName());

		ClassLoader testClassLoader = DynamicContextHolder.getContextLoader().getTestClassLoader(holder.parentClassLoader, holder.testClass);
		ClassLoader existingClassLoader = ClassUtils.getDefaultClassLoader();

		try {

			Thread.currentThread().setContextClassLoader(testClassLoader);

			Class<?> loadedTestClass = testClassLoader.loadClass(holder.testClass.getName());
			TestRunner runner = new TestRunner();
			Test test = TestSuite.createTest(loadedTestClass, holder.methodName);
			runner.doRun(test);

		}
		catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		finally {
			Thread.currentThread().setContextClassLoader(existingClassLoader);
		}
	}

	private String readCommand(PluginDataHolder holder) {

		String commandString = null;
		System.out.println();
		System.out.println("--------------------------------");

		if (holder.testClass == null)
			System.out.println("WARNING: test class not loaded");

		System.out.println("Enter u to show usage");

		System.out.print(">");
		try {
			commandString = printInput();
		}
		catch (Exception e) {
		}
		System.out.println();
		return commandString;
	}

	private static void usage() {
		System.out.println("l [testClass] to load test class");
		System.out.println("[testName] to run test");
		System.out.println("reload [plugin name] to reload plugin");
		System.out.println("reload to reload parent context");
		System.out.println("s to show test methods");
		System.out.println("r to rerun last command");
		System.out.println("r to rerun last run test");
		System.out.println("e to exit");
	}

	private static void showTestMethods(Class testClass) {
		System.out.println("Available test methods:");
		List<String> testMethods = getTestMethods(testClass);

		for (String name : testMethods) {
			System.out.println("    " + name);
		}
	}

	private void setParentClassLoader(PluginDataHolder holder) {
		holder.parentClassLoader = DynamicContextHolder.getContextLoader().newParentClassLoader();
	}

	private static List<String> getTestMethods(Class testClass) {
		Method[] methods = testClass.getMethods();
		List<String> toReturn = new ArrayList<String>();
		for (Method method : methods) {
			if (method.getParameterTypes().length == 0 && method.getName().startsWith("test")) {
				toReturn.add(method.getName());
			}
		}
		return toReturn;
	}

	public static String printInput() throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String input = in.readLine();
		return input;
	}
}

class PluginDataHolder {
	SpringContextSpec pluginSpec;

	String lastCommand;

	Class testClass;

	String methodName;

	ClassLoader parentClassLoader;
}