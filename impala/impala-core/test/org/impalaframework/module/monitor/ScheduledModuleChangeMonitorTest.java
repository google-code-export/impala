package org.impalaframework.module.monitor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.module.monitor.ModuleChangeEvent;
import org.impalaframework.module.monitor.ScheduledModuleChangeMonitor;
import org.impalaframework.util.ResourceUtils;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

/**
 * @author Phil Zoio
 */
public class ScheduledModuleChangeMonitorTest extends TestCase {

	private File file1;

	private File file2;

	private File file3;

	private File file4;


	public void setUp() throws IOException {
		String tmpDir = System.getProperty("java.io.tmpdir");
		file1 = new File(tmpDir, "file1.txt");
		file2 = new File(tmpDir, "file2.txt");
		file3 = new File(tmpDir, "file3.txt");
		file4 = new File(tmpDir, "file4.txt");
		FileCopyUtils.copy("file1 text", new FileWriter(file1));
		FileCopyUtils.copy("file2 text", new FileWriter(file2));
		FileCopyUtils.copy("file3 text", new FileWriter(file1));
		FileCopyUtils.copy("file4 text", new FileWriter(file2));
	}

	public final void testMonitor() throws Exception {
		File[] files1 = new File[] { file1, file2, };
		Resource[] resources1 = ResourceUtils.getResources(files1);
		File[] files2 = new File[] { file3, file4, };
		Resource[] resources2 = ResourceUtils.getResources(files2);
		
		ScheduledModuleChangeMonitor monitor = new ScheduledModuleChangeMonitor();
		monitor.setResourcesToMonitor("myplugin1", resources1);
		monitor.setResourcesToMonitor("myplugin2", resources2);
		
		final RecordingModuleChangeListener listener = new RecordingModuleChangeListener();
		monitor.addModificationListener(listener);
		monitor.setInitialDelay(0);
		monitor.setInterval(1);
		monitor.start();

		Thread.sleep(500);
		
		FileCopyUtils.copy("file1 text modified", new FileWriter(file1));
		FileCopyUtils.copy("file2 text modified", new FileWriter(file2));
		FileCopyUtils.copy("file3 text modified", new FileWriter(file3));
		FileCopyUtils.copy("file4 text modified", new FileWriter(file4));
		
		Thread.sleep(1500);
		
		List<ModuleChangeEvent> events = listener.getEvents();
		System.out.println(events);
		
		assertEquals(1, events.size());
		assertEquals(2, events.get(0).getModifiedModules().size());
		
		//now clear the stored events
		listener.clear();
		Thread.sleep(1500);
		
		events = listener.getEvents();
		//no further changes are made, so no further events are expected
		assertEquals(0, events.size());
		monitor.stop();
		
	}

	public void tearDown() {
		if (file1.exists())
			file1.delete();
		if (file2.exists())
			file2.delete();
		if (file3.exists())
			file3.delete();
		if (file4.exists())
			file4.delete();
	}

}
