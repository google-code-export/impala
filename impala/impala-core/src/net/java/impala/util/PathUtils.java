package net.java.impala.util;

import java.io.File;

public class PathUtils {

	public static String getCurrentDirectoryName() {
		File file = new File(new File("").getAbsolutePath());
		return file.getName();
	}

}
