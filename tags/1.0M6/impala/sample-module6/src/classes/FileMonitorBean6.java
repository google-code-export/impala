package classes;

import java.io.File;

import org.impalaframework.file.FileMonitor;

public class FileMonitorBean6 implements FileMonitor {

	public long lastModified(File file) {
		return -100;
	}

	public long lastModified(File[] file) {
		return 0;
	}

}
