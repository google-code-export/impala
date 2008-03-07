package org.impalaframework.command.basic;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Modifier;

import org.impalaframework.classloader.ModuleClassLoader;
import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.exception.ExecutionException;
import org.impalaframework.file.handler.DefaultClassFilter;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

public class ModuleDefinitionAwareClassFilter extends DefaultClassFilter implements FileFilter {

	private static final Log logger = LogFactory.getLog(ModuleDefinitionAwareClassFilter.class);

	@Override
	public void setRootPath(File file) {
		Assert.notNull(file);
		try {
			this.rootFile = file;
			this.rootCanonicalPath = file.getCanonicalPath();
		}
		catch (IOException e) {
			throw new ExecutionException("Unable to obtain canonical path for file " + file);
		}
	}

	private File rootFile;
	private String rootCanonicalPath;

	public ModuleDefinitionAwareClassFilter() {
	}

	public boolean accept(File pathname) {
		if (!super.accept(pathname)) {
			return false;
		}

		if (pathname.isDirectory()) {
			return true;
		}

		if (rootCanonicalPath == null) {
			throw new ConfigurationException("root canonical path not set");
		}

		String canonicalPath = null;
		try {
			canonicalPath = pathname.getCanonicalPath();
		}
		catch (IOException e) {
			logger.error("Could not read canonical path for " + pathname, e);
			return false;
		}
		String relativePath = canonicalPath.substring(rootCanonicalPath.length());

		relativePath = relativePath.replace(File.separator, ".");
		relativePath = relativePath.replace("/", ".");
		relativePath = relativePath.substring(0, relativePath.length() - ".class".length());
		if (relativePath.startsWith(".")) {
			relativePath = relativePath.substring(1);
		}
		
		//create a classloader pointing to the supplied root file location
		ModuleClassLoader classLoader = new ModuleClassLoader(new File[]{ this.rootFile });
		
		Class<?> forName = null;
		try {
			forName = Class.forName(relativePath, false, classLoader);
			if (forName.isInterface()) {
				return false;
			}

			int mods = forName.getModifiers();
			if (Modifier.isAbstract(mods)) {
				return false;
			}

			if (ModuleDefinitionSource.class.isAssignableFrom(forName)) {
				return true;
			}
		}
		catch (ClassNotFoundException e) {
			logger.error("Unable to resolve class associated with path " + pathname, e);
		}

		return false;

	}

}
