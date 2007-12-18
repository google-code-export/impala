package org.impalaframework.module.monitor;

import java.util.ArrayList;
import java.util.Set;

import org.impalaframework.module.monitor.BaseModuleChangeListener;
import org.impalaframework.module.monitor.ModuleChangeEvent;
import org.impalaframework.module.monitor.ModuleChangeInfo;

import junit.framework.TestCase;

/**
 * @author Phil Zoio
 */
public class BaseModuleChangeListenerTest extends TestCase {

	public final void testGetModifiedPlugins() {
		BaseModuleChangeListener listener = new BaseModuleChangeListener();
		ArrayList<ModuleChangeInfo> info = new ArrayList<ModuleChangeInfo>();
		ModuleChangeEvent event = new ModuleChangeEvent(info);
		assertTrue(listener.getModifiedPlugins(event).isEmpty());
		
		info.add(new ModuleChangeInfo("p1"));
		info.add(new ModuleChangeInfo("p2"));
		info.add(new ModuleChangeInfo("p2"));
		
		event = new ModuleChangeEvent(info);
		Set<String> modifiedPlugins = listener.getModifiedPlugins(event);
		assertEquals(2, modifiedPlugins.size());
		assertTrue(modifiedPlugins.contains("p1"));
	}

}
