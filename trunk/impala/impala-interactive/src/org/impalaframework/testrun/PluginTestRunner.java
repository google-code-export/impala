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

package org.impalaframework.testrun;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.impalaframework.command.Command;
import org.impalaframework.command.CommandLineInputCapturer;
import org.impalaframework.command.CommandState;
import org.impalaframework.command.impl.ClassFindCommand;
import org.impalaframework.command.impl.ContextSpecAwareClassFilter;
import org.impalaframework.command.impl.SearchClassCommand;
import org.impalaframework.command.impl.SelectMethodCommand;
import org.impalaframework.plugin.loader.ApplicationContextLoader;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpecProvider;
import org.impalaframework.resolver.ClassLocationResolver;
import org.impalaframework.resolver.StandaloneClassLocationResolverFactory;
import org.impalaframework.util.MemoryUtils;
import org.impalaframework.util.PathUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.StopWatch;

public class PluginTestRunner {
	
	private static final int DEFAULT_MAX_INACTIVITY_INTERVAL = 600;
	
	private long lastAccessed;

	private ClassLocationResolver classLocationResolver;

	public static void main(String[] args) {
		// autoReload is set to true by default
		boolean autoReload = true;
		if (args.length > 0) {
			autoReload = Boolean.parseBoolean(args[0]);
		}
		run(autoReload);
	}

	public static void run(boolean autoReload) {
		new PluginTestRunner(autoReload, true).start(null);
	}

	public static void run(Class testClass) {
		// autoreload not enabled by default
		new PluginTestRunner(false, true).start(testClass);
	}

	public static void run(Class testClass, boolean autoReload) {
		new PluginTestRunner(autoReload, true).start(testClass);
	}

	public PluginTestRunner(boolean autoreload, boolean reloadableParent) {
		super();
		if (System.getProperty("impala.parent.project") == null) {
			System.setProperty("impala.parent.project", PathUtils.getCurrentDirectoryName());
		}

		final ApplicationContextLoader loader = DynamicContextHolder.getContextLoader();
		if (loader == null) {
			ClassLocationResolver classLocationResolver = new StandaloneClassLocationResolverFactory()
					.getClassLocationResolver();
			this.classLocationResolver = classLocationResolver;
		}
	}

	/**
	 * Runs a suite extracted from a TestCase subclass.
	 */
	public void start(Class testClass) {
		
		DynamicContextHolder.init();
		
		PluginDataHolder holder = new PluginDataHolder();
		holder.testClass = testClass;

		lastAccessed = System.currentTimeMillis();

		int maxInactivityInterval = getMaxInactivityInterval();
		Thread stopChecker = new Thread(new StopCheckerDelegate(maxInactivityInterval));

		stopChecker.start();

		while (true) {

			try {

				if (holder.testClass == null) {
					changeClass(holder);
				}

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

	protected int getMaxInactivityInterval() {
		return DEFAULT_MAX_INACTIVITY_INTERVAL;
	}

	private void runCommand(PluginDataHolder holder, String command) {
		if (command.equals("t")) {
			if (holder.methodName == null) {
				setMethodName(holder, command);
			}
			runTest(holder);
		}
		else if (command.startsWith("test")) {
			setMethodName(holder, command);
			runTest(holder);
		}
		// FIXME add mechanism to change current project
		else if (command.equals("c")) {
			changeClass(holder);
		}
		else if (command.equals("s")) {
			showTestMethods(holder);
		}
		else if (command.equals("e")) {
			exit();
		}
		else if (command.equals("u")) {
			usage();
		}
		else if (command.startsWith("reload")) {
			if (holder.pluginSpec != null) {
				String pluginName = command.substring("reload".length());
				String pluginToReload = pluginName.trim();
				if (pluginToReload.length() > 0) {
					reloadPlugin(pluginToReload);
				}
				else {
					DynamicContextHolder.reloadParent();
				}
			}
			else {
				System.out.println("Run a test before executing this command");
			}
		}
		else {
			System.out.println("Unrecognised command: " + command);
		}
	}

	private void setMethodName(PluginDataHolder holder, String candidate) {
		final List<String> testMethods = getTestMethods(holder.testClass);

		if (testMethods.contains(candidate)) {
			holder.methodName = candidate;
		}
		else {
			ClassLoader testClassLoader = getTestClassLoader(holder);
			Class toUse = holder.testClass;
			try {
				toUse = testClassLoader.loadClass(holder.testClass.getName());
			}
			catch (Exception e) {
			}
			SelectMethodCommand command = new SelectMethodCommand(toUse);
			execute(command);
			String testName = command.getMethodName();
			System.out.println("Setting test to " + testName);
			holder.methodName = command.getMethodName();
		}
	}

	private boolean changeClass(PluginDataHolder holder) {
		final String currentDirectoryName = PathUtils.getCurrentDirectoryName();

		final File[] testClassLocations = classLocationResolver.getPluginTestClassLocations(currentDirectoryName);

		if (testClassLocations == null) {
			System.out.println("Unable to find any test class locations corresponding with " + currentDirectoryName);
			return false;
		}

		SearchClassCommand command = new SearchClassCommand() {

			@Override
			protected ClassFindCommand newClassFindCommand() {
				final ClassFindCommand classFindCommand = super.newClassFindCommand();
				classFindCommand.setDirectoryFilter(new ContextSpecAwareClassFilter());
				return classFindCommand;
			}

		};
		command.setClassDirectories(Arrays.asList(testClassLocations));
		execute(command);
		loadTestClass(holder, command.getClassName());
		return true;
	}

	private void reloadPlugin(String pluginToReload) {
		StopWatch watch = startWatch();

		if (DynamicContextHolder.reload(pluginToReload)) {
			watch.stop();
			printReloadInfo(pluginToReload, watch);
		}
		else {
			String actual = DynamicContextHolder.reloadLike(pluginToReload);
			printReloadInfo(actual, watch);
		}
	}

	private void printReloadInfo(String pluginToReload, StopWatch watch) {
		if (pluginToReload != null) {
			System.out.println("Plugin " + pluginToReload + " loaded in " + watch.getTotalTimeSeconds() + " seconds");
			System.out.println(MemoryUtils.getMemoryInfo());
		}
		else {
			System.out.println("No matching plugin found to reload");
		}
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
				if (o instanceof PluginSpecProvider) {
					PluginSpecProvider p = (PluginSpecProvider) o;
					holder.pluginSpec = p.getPluginSpec();
					DynamicContextHolder.init(p);
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

		if (DynamicContextHolder.get() == null) {
			loadTestClass(holder, holder.testClass.getName());
		}

		ClassLoader testClassLoader = getTestClassLoader(holder);

		ClassLoader existingClassLoader = ClassUtils.getDefaultClassLoader();

		try {

			Thread.currentThread().setContextClassLoader(testClassLoader);

			Class<?> loadedTestClass = testClassLoader.loadClass(holder.testClass.getName());
			TestRunner runner = new TestRunner();

			System.out.println("Running test " + holder.methodName);
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

	private ClassLoader getTestClassLoader(PluginDataHolder holder) {
		final ApplicationContext context = DynamicContextHolder.get();

		if (context == null)
			return null;

		final ClassLoader parentClassLoader = context.getClassLoader();
		ClassLoader testClassLoader = getTestClassLoader(parentClassLoader, holder.testClass.getName());
		return testClassLoader;
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
			lastAccessed = System.currentTimeMillis();
		}
		catch (Exception e) {
		}
		System.out.println();
		return commandString;
	}

	private static void usage() {
		System.out.println("c load or change test class");
		System.out.println("[testName] to run test");
		System.out.println("reload [plugin name] to reload plugin");
		System.out.println("reload to reload parent context");
		System.out.println("s to show test methods");
		System.out.println("r to rerun last command");
		System.out.println("r to rerun last run test");
		System.out.println("e to exit");
	}

	private void showTestMethods(PluginDataHolder holder) {

		Class testClass = holder.testClass;

		final ClassLoader testClassLoader = getTestClassLoader(holder);

		if (testClassLoader != null) {
			try {
				testClass = Class.forName(testClass.getName(), false, testClassLoader);
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Available test methods:");

		List<String> testMethods = getTestMethods(testClass);

		for (String name : testMethods) {
			System.out.println("    " + name);
		}
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

	private ClassLoader getTestClassLoader(ClassLoader parentClassLoader, String name) {
		File[] locations = classLocationResolver.getPluginTestClassLocations(PathUtils.getCurrentDirectoryName());

		TestClassLoader cl = new TestClassLoader(parentClassLoader, locations, name);
		return cl;
	}

	private void execute(Command command) {
		CommandState commandState = new CommandState();
		CommandLineInputCapturer inputCapturer = new CommandLineInputCapturer();
		commandState.setInputCapturer(inputCapturer);
		command.execute(commandState);
	}

	private long getLastAccessed() {
		return lastAccessed;
	}

	final class StopCheckerDelegate implements Runnable {

		private final int maxInactiveSeconds;
		
		private boolean isStopped;

		public StopCheckerDelegate(int maxInactiveSeconds) {
			super();
			this.maxInactiveSeconds = maxInactiveSeconds;
		}

		public void run() {
			while (!isStopped) {
				if ((System.currentTimeMillis() - getLastAccessed()) > 1000 * maxInactiveSeconds) {
					System.out.println();
					System.out.println("Terminating test runner as it has been inactive for more than " + maxInactiveSeconds + " seconds.");
					System.exit(0);
				}
				try {
					//sleep for 10 seconds before checking again
					Thread.sleep(10000);
				}
				catch (InterruptedException e) {
				}
			}
		}
		
		void stop() {
			this.isStopped = true;
		}
	}

}

class PluginDataHolder {
	ParentSpec pluginSpec;

	String lastCommand;

	Class testClass;

	String methodName;
}