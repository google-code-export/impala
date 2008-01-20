package org.impalaframework.command.interactive;

import java.io.File;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.impalaframework.classloader.ModuleTestClassLoader;
import org.impalaframework.classloader.TestClassLoader;
import org.impalaframework.command.framework.Command;
import org.impalaframework.command.framework.CommandDefinition;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.resolver.StandaloneModuleLocationResolverFactory;
import org.impalaframework.testrun.DynamicContextHolder;
import org.impalaframework.util.PathUtils;
import org.impalaframework.util.ResourceUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

public abstract class BaseRunTestCommand implements Command {

	private ModuleLocationResolver moduleLocationResolver;

	public BaseRunTestCommand() {
		super();
		ModuleLocationResolver moduleLocationResolver = new StandaloneModuleLocationResolverFactory()
				.getClassLocationResolver();
		this.moduleLocationResolver = moduleLocationResolver;
	}

	public boolean execute(CommandState commandState) {

		Class<?> testClass = (Class<?>) GlobalCommandState.getInstance().getValue(CommandStateConstants.TEST_CLASS);
		if (testClass == null) {
			System.out.println("No test class set.");
			return false;
		}

		String testClassName = testClass.getName();
		ClassLoader testClassLoader = getTestClassLoader(testClassName);
		ClassLoader existingClassLoader = ClassUtils.getDefaultClassLoader();

		try {

			Thread.currentThread().setContextClassLoader(testClassLoader);

			Class<?> loadedTestClass = testClassLoader.loadClass(testClassName);
			GlobalCommandState.getInstance().addValue(CommandStateConstants.TEST_CLASS, loadedTestClass);
			
			String methodName = getMethodName(commandState, loadedTestClass);

			if (methodName != null) {
				GlobalCommandState.getInstance().addValue(CommandStateConstants.TEST_METHOD_NAME, methodName);

				TestRunner runner = new TestRunner();

				System.out.println("Running test " + methodName);
				Test test = TestSuite.createTest(loadedTestClass, methodName);
				runner.doRun(test);
				return true;
			}
			else {
				return false;
			}
		}
		catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		finally {
			Thread.currentThread().setContextClassLoader(existingClassLoader);
		}

	}

	protected abstract String getMethodName(CommandState commandState, Class<?> testClass);

	private ClassLoader getTestClassLoader(String testClassName) {
		final ApplicationContext context = DynamicContextHolder.get();

		final ClassLoader parentClassLoader = context.getClassLoader();
		ClassLoader testClassLoader = getTestClassLoader(parentClassLoader, testClassName);
		return testClassLoader;
	}

	private ClassLoader getTestClassLoader(ClassLoader parentClassLoader, String name) {
		String currentDirectoryName = PathUtils.getCurrentDirectoryName();

		List<Resource> locationResources = moduleLocationResolver.getModuleTestClassLocations(currentDirectoryName);
		File[] locations = ResourceUtils.getFiles(locationResources);

		String parentProjectName = System.getProperty("impala.root.projects");
		if (parentProjectName != null && !currentDirectoryName.equals(parentProjectName)) {
			// if parent project has been specified and is not the same as the
			// current directory
			return new ModuleTestClassLoader(parentClassLoader, locations, name);
		}
		else {
			return new TestClassLoader(parentClassLoader, locations, name);
		}

	}

	public CommandDefinition getCommandDefinition() {
		return new CommandDefinition("Runs test");
	}

}
