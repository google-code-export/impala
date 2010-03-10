/*
 * Copyright 2007-2010 the original author or authors.
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

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.impalaframework.launcher.SystemPropertiesReader;

/**
 * A wrapper used to assemble the classpath before launching the actual
 * application. The big trick is assembling the classpath before the launch.
 * Yep, this is functionality that should be built right into Java.
 * 
 * <p>
 * This is packaged into a JAR with no dependencies. This Launcher is based on
 * Howard Lewis Ship's blog posting:
 * http://tapestryjava.blogspot.com/2007/08/quick-and-dirty-java-application.html
 * Main-Class: Launcher
 */
public final class Launcher {

	private static final List<URL> _classpath = new ArrayList<URL>();

	/**
	 * Usage:
	 * 
	 * <pre>
	 * java -jar mblauncher.jar some.class.to.launch [--addclasspath dir] [--addjardir dir] [options]
	 * </pre>
	 * 
	 * <p>
	 * The --addclasspath parameter is used to add a directory to add to the
	 * classpath. This is most commonly used with a directory containing
	 * configuration files that override corresponding files stored inside other
	 * JARs on the classpath.
	 * 
	 * <p>
	 * The --addjardir parameter is used to define directories. JAR files
	 * directly within such directories will be added to the search path.
	 * 
	 * <p>
	 * Any remaining options will be collected and passed to the main() method
	 * of the launch class.
	 */
	public static void main(String[] args) {

		List<String> launchOptions = new ArrayList<String>();

		if (args.length == 0)
			fail("No class to launch was specified.  This should be the first parameter.");

		String launchClass = args[0];

		int cursor = 1;

		while (cursor < args.length) {

			String arg = args[cursor];

			if (arg.equals("--addclasspath")) {
				if (cursor + 1 == args.length)
					fail("--addclasspath argument was not followed by the name of the directory to add to the classpath.");

				String dir = args[cursor + 1];

				add(dir);

				cursor += 2;
				continue;
			}

			if (arg.equals("--addjardir")) {

				if (cursor + 1 == args.length) {
					fail("--addjardir argument was not followed by the name of a directory to search for JARs.");
				}

				String dir = args[cursor + 1];

				search(dir);

				cursor += 2;
				continue;
			}

			if (arg.equals("--findjardirs")) {

				if (cursor + 1 == args.length) {
					fail("--findjardirs argument was not followed by the name of a directory to search for JARs.");
				}

				String dir = args[cursor + 1];

				deepSearch(dir);

				cursor += 2;
				continue;
			}

			launchOptions.add(arg);

			cursor++;
		}

		String[] newArgs = launchOptions.toArray(new String[launchOptions.size()]);
		System.out.println("Main class: " + launchClass);
		System.out.println("Args: " + launchOptions);
		System.out.println("Class path: " + _classpath);
		launch(launchClass, newArgs);
	}

	static void launch(String launchClassName, String[] args) {

		URL[] classpathURLs = _classpath.toArray(new URL[_classpath.size()]);

		try {
			URLClassLoader newLoader = new URLClassLoader(classpathURLs, Thread.currentThread().getContextClassLoader());

			Thread.currentThread().setContextClassLoader(newLoader);

			Class<?> launchClass = newLoader.loadClass(launchClassName);

			SystemPropertiesReader sysPropReader = new SystemPropertiesReader(newLoader);
			sysPropReader.readSystemProperties();
			
			Method main = launchClass.getMethod("main", new Class[] { String[].class });

			main.invoke(null, new Object[] { args });
		}
		catch (ClassNotFoundException ex) {
			fail(String.format("Class '%s' not found.", launchClassName));
		}
		catch (NoSuchMethodException ex) {
			fail(String.format("Class '%s' does not contain a main() method.", launchClassName));
		}
		catch (Exception ex) {
			Throwable root = ex;
			Throwable cause = null;
			
			//navigate to orot cause
			while ((cause = root.getCause()) != null) {
				root = cause;
			}
			root.printStackTrace();			
			fail(String.format("Error invoking method main() of %s: %s", launchClassName, ex.toString()));
		}
	}

	private static void add(String directoryName) {
		File dir = toDir(directoryName);

		if (dir == null)
			return;

		addToClasspath(dir);
	}

	private static File toDir(String directoryName) {
		File dir = new File(directoryName);

		if (!dir.exists()) {
			System.err.printf("Warning: directory '%s' does not exist.\n", directoryName);
			return null;
		}

		if (!dir.isDirectory()) {
			System.err.printf("Warning: '%s' is a file, not a directory.\n", directoryName);
			return null;
		}

		return dir;
	}

	private static void search(String directoryName) {

		File dir = toDir(directoryName);

		if (dir == null)
			return;

		File[] jars = dir.listFiles(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				return name.endsWith(".jar");
			}

		});

		for (File jar : jars) {
			addToClasspath(jar);
		}

	}

	private static void deepSearch(String directoryName) {

		File dir = toDir(directoryName);

		if (dir == null)
			return;

		List<File> jars = new ArrayList<File>();
		addFiles(jars, dir);

		for (File jar : jars) {
			addToClasspath(jar);
		}

	}

	static void addFiles(List<File> jarFiles, File dir) {
		File[] files = dir.listFiles(new FilenameFilter() {
			public boolean accept(File file, String name) {
				if (file.isDirectory() && !file.getName().startsWith("."))
					return true;
				if (file.isFile())
					return name.endsWith(".jar");
				return false;
			}
		});

		for (File file : files) {
			if (file.isDirectory() && !file.getName().startsWith(".")) {
				addFiles(jarFiles, file);
			}
			else {
				jarFiles.add(file);
			}
		}
	}

	private static void addToClasspath(File jar) {
		URL url = toURL(jar);

		if (url != null)
			_classpath.add(url);
	}

	private static URL toURL(File file) {
		try {
			return file.toURI().toURL();
		}
		catch (MalformedURLException ex) {
			System.err.printf("Error converting %s to a URL: %s\n", file, ex.getMessage());

			return null;
		}
	}

	private static void fail(String message) {
		System.err.println("Launcher failure: " + message);

		System.exit(-1);
	}

}