package org.impalaframework.command.basic;

import java.io.File;
import java.io.FileFilter;

public class ClassFindCommandFilter implements FileFilter {

	private String rootPath;
	private String classSegment;
	private String packageSegment;
	
	private String className;
	
	public ClassFindCommandFilter(String rootPath, String classSegment, String packageSegment) {
		super();
		this.rootPath = rootPath;
		this.classSegment = classSegment;
		this.packageSegment = packageSegment;
	}

	public boolean accept(File file) {
		
		if (file.getName().toLowerCase().contains(classSegment.toLowerCase())) {
			
			// calculate relative path
			String absolute = file.getAbsolutePath();
			String relative = absolute.substring(rootPath.length() + 1, absolute.length() - 6);

			boolean add = true;

			className = relative.replace(File.separatorChar, '.');

			if (packageSegment != null) {
				// add if packageSegment matches
				int packagePartDot = className.lastIndexOf('.');
				if (packagePartDot >= 0) {
					String packagePart = className.substring(0, packagePartDot);
					add = (packagePart.endsWith(packageSegment));
				}

			}

			return add;
		}
		return false;
	}

	public String getClassName() {
		return className;
	}

}
