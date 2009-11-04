package org.impalaframework.classloader;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.spi.Application;
import org.springframework.util.ClassUtils;

public class TestClassLoaderFactory implements ClassLoaderFactory {

    public ClassLoader newClassLoader(Application application, ClassLoader parent, ModuleDefinition moduleDefinition) {        
        return ClassUtils.getDefaultClassLoader();
    }

}
