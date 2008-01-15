package org.impalaframework.command.basic;

import java.io.File;
import java.io.FileFilter;
import java.util.LinkedList;
import java.util.List;

import org.springframework.util.Assert;

public class FileFilterHandler {

	public List<File> filter(List<File> files, FileFilter filter) {
		Assert.notNull(files, "files cannot be null");
		Assert.notNull(filter, "filter cannot be null");
		
		List<File> filtered = new LinkedList<File>();
		for (File file : files) {
			if (filter.accept(file)) {
				filtered.add(file);
			}
		}
		return filtered;
	}

}
