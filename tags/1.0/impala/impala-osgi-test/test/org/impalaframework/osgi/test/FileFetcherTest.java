package org.impalaframework.osgi.test;

import java.util.List;

import junit.framework.TestCase;

import org.springframework.core.io.Resource;

public class FileFetcherTest extends TestCase {

    public void testMain() {
        
        //impala is not in main
        checkSize("jetty:*;main:impala", "main:jmx", 0);
        
        //cglib is included in main
        checkSize("jetty:*;main:impala,cglib", "main:jmx", 1);
        
        //as above with spaces
        checkSize("jetty: *; main: impala, cglib", " main: jmx", 1);
        
        //now we also exclude cglib
        checkSize("jetty:*;main:impala,cglib", "main:jmx,cglib", 0);
        
        //impala is not in main. All files should appear
        checkSize("jetty:jetty;main:*", "main:jmx", 8);
        
        //now we also exclude cglib
        checkSize("jetty:*", null, 0);
        
    }
    
    public void testMultipleFolders() {
        checkSize("build:junit;main:cglib", null, new String[]{"main", "build"}, 1);
    }

    private void checkSize(String includes, String excludes, int size) {
        String[] folders = new String[] {"main"};
        checkSize(includes, excludes, folders, size);
    }

    private void checkSize(String includes, String excludes, String[] folders,
            int size) {
        FileFetcher fileFetcher = new FileFetcher("../impala-repository", folders);
        
        ConfigurableFileFilter filter = new ConfigurableFileFilter(includes, excludes);
        List<Resource> fileList = fileFetcher.getResources(filter);
        
        assertEquals(size, fileList.size());
    }
}
