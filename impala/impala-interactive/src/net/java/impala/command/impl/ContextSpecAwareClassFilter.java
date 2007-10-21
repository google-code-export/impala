package net.java.impala.command.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Modifier;

import net.java.impala.exception.ExecutionException;
import net.java.impala.file.DefaultClassFilter;
import net.java.impala.testrun.SpringContextSpecAware;

import org.springframework.util.Assert;

public class ContextSpecAwareClassFilter extends DefaultClassFilter implements FileFilter {

	@Override
	public void setRootPath(File file) {
		Assert.notNull(file);
		try {
			this.rootCanonicalPath = file.getCanonicalPath();
		}
		catch (IOException e) {
			throw new ExecutionException("Unable to obtain canonical path for file " + file);
		}
	}

	private String rootCanonicalPath;
	
	public ContextSpecAwareClassFilter() {
	}

	public boolean accept(File pathname) {
		if (!super.accept(pathname)) {
			return false;
		}

		if (pathname.isDirectory()) {
			return true;
		}
		
		if (rootCanonicalPath == null) {
			throw new IllegalStateException("root canonical path not set");
		}
		
		try {
			
			String canonicalPath = pathname.getCanonicalPath();
			String relativePath = canonicalPath.substring(rootCanonicalPath.length());
			
			relativePath = relativePath.replace(File.separator, ".");
			relativePath = relativePath.replace("/", ".");
			relativePath = relativePath.substring(0, relativePath.length() - ".class".length());
			if (relativePath.startsWith(".")) {
				relativePath = relativePath.substring(1);
			}
			
			final Class<?> forName = Class.forName(relativePath);
			
			if (forName.isInterface()) {
				return false;
			}

			int mods = forName.getModifiers();
		    if (Modifier.isAbstract(mods)) {
		    	return false;
		    }
			
			if (SpringContextSpecAware.class.isAssignableFrom(forName)) {
				return true;
			}
		}
		catch (Exception e) {
			//FIXME debug an error here
		}
		
		
		return false;
		
	}

}
