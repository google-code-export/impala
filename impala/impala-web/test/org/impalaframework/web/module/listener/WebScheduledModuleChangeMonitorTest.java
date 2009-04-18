package org.impalaframework.web.module.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import junit.framework.TestCase;

import org.springframework.core.io.FileSystemResource;
import org.springframework.util.FileCopyUtils;

public class WebScheduledModuleChangeMonitorTest extends TestCase {
    
    private WebScheduledModuleChangeMonitor monitor;
    private File tempFile;
    
    //uncomment to run tests, which are subject to race conditions so take a bit long to run
    private boolean manual = false;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        monitor = new WebScheduledModuleChangeMonitor();
        monitor.setUseTouchFile(true);
        tempFile = new File(new File(System.getProperty("java.io.tmpdir")), "touch.txt");
        tempFile.createNewFile();
    }

    public void testNoUseTouchFile() throws Exception {
        deleteFile();
        monitor.setTouchFile(new FileSystemResource(tempFile));
        monitor.setUseTouchFile(false);
        
        assertTrue(monitor.checkForChanges());
    }
    
    public void testCheckForChangesNull() throws Exception {
        assertTrue(monitor.checkForChanges());
        
        monitor.setTouchFile(new FileSystemResource(tempFile));
        deleteFile();
        assertFalse(monitor.checkForChanges());
        
        writeFile();
        assertTrue(monitor.checkForChanges());
        assertFalse(monitor.checkForChanges());
        assertFalse(monitor.checkForChanges());

        doSleepAndCheckForChange();
    }
    
    public void testGetLastModified() throws Exception {
        writeFile();
        monitor.setTouchFile(new FileSystemResource(tempFile));
        long lastModified = monitor.getLastModified();
        assertTrue(lastModified > 0);
        deleteFile();
        
        doSleepLastModifiedCheckForChange(lastModified);
    }

    private void doSleepAndCheckForChange() throws InterruptedException, IOException {
        if (manual) {
            Thread.sleep(1000);
            writeFile();
            assertTrue(monitor.checkForChanges());
        }
    }

    private void doSleepLastModifiedCheckForChange(long lastModified)
            throws InterruptedException, IOException {
        if (manual) {
            Thread.sleep(1000);
            writeFile();
            long newModified = monitor.getLastModified();
            assertTrue(newModified > lastModified);
        }
    }

    private void deleteFile() {
        tempFile.delete();
    }

    private void writeFile() throws IOException {
        tempFile.createNewFile();
        FileCopyUtils.copy("", new FileWriter(tempFile));
    }

}
