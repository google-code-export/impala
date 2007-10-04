package net.java.impala.file;

import java.io.File;
import java.io.FileFilter;

public interface FileRecurseHandler {

	FileFilter getDirectoryFilter();

	void handleFile(File subfile);

	void handleDirectory(File directory);
	
	boolean shouldStop();

}
