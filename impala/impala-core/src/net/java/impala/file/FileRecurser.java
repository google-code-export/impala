package net.java.impala.file;

import java.io.File;

import org.springframework.util.Assert;

public class FileRecurser {

	public void recurse(FileRecurseHandler handler, File directory) {
		Assert.isTrue(directory.isDirectory(), directory + " is not a directory");
		handler.handleDirectory(directory);
		
		File[] files = directory.listFiles(handler.getDirectoryFilter());
		for (File subfile : files) {
			if (subfile.isFile()) {
				handler.handleFile(subfile);
			}
			else if (subfile.isDirectory()) {
				recurse(handler, subfile);
			}
		}
	}

}
