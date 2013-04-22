package org.impalaframework.classloader.graph;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.impalaframework.resolver.ModuleLocationResolver;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

class TestClassResolver implements ModuleLocationResolver {

    private String rootLocation = "../impala-core/files/impala-classloader";
    
    public List<Resource> getApplicationModuleClassLocations(String moduleName) {
        File root = rootFileLocation();
        File moduleDirectory = new File(root, moduleName);
        File classDirectory = new File(moduleDirectory, "bin");
        final Resource resource = new FileSystemResource(classDirectory);
        
        Assert.isTrue(resource.exists());
        
        return Collections.singletonList(resource);
    }
    
    public List<Resource> getApplicationModuleLibraryLocations(String moduleName) {
        //FIXME implement if necessary
        return null;
    }

    public List<Resource> getModuleTestClassLocations(String moduleName) {
        throw new UnsupportedOperationException();
    }

    public Resource getRootDirectory() {
        File file = rootFileLocation();
        return new FileSystemResource(file);
    }

    private File rootFileLocation() {
        File file = new File(rootLocation);
        return file;
    }
    
}
