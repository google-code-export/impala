package net.java.impala.file;

import java.io.File;
import java.io.FileFilter;

public interface RootPathAwareFileFilter extends FileFilter {
	public void setRootPath(File file);
}
