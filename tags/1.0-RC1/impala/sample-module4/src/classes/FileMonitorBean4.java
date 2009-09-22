package classes;

import java.io.File;
import java.util.Properties;


import org.impalaframework.file.FileMonitor;
import org.impalaframework.util.PropertyUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ClassUtils;

public class FileMonitorBean4 implements FileMonitor {

    public long lastModified(File file) {
        ClassPathResource resource = null;

        if (file == null)
            resource = new ClassPathResource("monitor.properties", this.getClass().getClassLoader());

        else
            resource = new ClassPathResource("monitor.properties", ClassUtils.getDefaultClassLoader());

        if (resource.exists()) {
            final Properties loadProperties = PropertyUtils.loadProperties(resource);

            String value = loadProperties.getProperty("value");
            if (value != null)
                return Long.parseLong(value);
        }
        return 0;
    }

    public long lastModified(File[] file) {
        return 0;
    }

}
