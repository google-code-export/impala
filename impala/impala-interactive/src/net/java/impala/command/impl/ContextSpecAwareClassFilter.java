package net.java.impala.command.impl;

import java.io.File;
import java.io.FileFilter;

import net.java.impala.file.DefaultClassFilter;
import net.java.impala.testrun.SpringContextSpecAware;

import org.springframework.util.Assert;

public class ContextSpecAwareClassFilter extends DefaultClassFilter implements FileFilter {

	private String rootCanonicalPath;
	
	public ContextSpecAwareClassFilter(String canonicalRootPath) {
		Assert.notNull(canonicalRootPath);
		this.rootCanonicalPath = canonicalRootPath;
	}

	public boolean accept(File pathname) {
		if (!super.accept(pathname)) {
			return false;
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
