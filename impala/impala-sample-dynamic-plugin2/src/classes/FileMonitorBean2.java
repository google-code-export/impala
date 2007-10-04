package classes;

import java.io.File;

import net.java.impala.monitor.FileMonitor;

public class FileMonitorBean2 implements FileMonitor {

	public long lastModified(File file) {
		return 100;
	}

}
