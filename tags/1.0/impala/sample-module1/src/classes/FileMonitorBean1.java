package classes;

import java.io.File;

import org.impalaframework.file.FileMonitor;


public class FileMonitorBean1 implements FileMonitor {

    public long lastModified(File file) {
        return 999;
    }

    public long lastModified(File[] file) {
        return 0;
    }
}
