package net.java.impala.spring.monitor;

import java.util.ArrayList;
import java.util.Set;

import net.java.impala.spring.monitor.BasePluginModificationListener;
import net.java.impala.spring.monitor.PluginModificationEvent;
import net.java.impala.spring.monitor.PluginModificationInfo;
import junit.framework.TestCase;

/**
 * @author Phil Zoio
 */
public class BasePluginModificationListenerTest extends TestCase {

	public final void testGetModifiedPlugins() {
		BasePluginModificationListener listener = new BasePluginModificationListener();
		ArrayList<PluginModificationInfo> info = new ArrayList<PluginModificationInfo>();
		PluginModificationEvent event = new PluginModificationEvent(info);
		assertTrue(listener.getModifiedPlugins(event).isEmpty());
		
		info.add(new PluginModificationInfo("p1"));
		info.add(new PluginModificationInfo("p2"));
		info.add(new PluginModificationInfo("p2"));
		
		event = new PluginModificationEvent(info);
		Set<String> modifiedPlugins = listener.getModifiedPlugins(event);
		assertEquals(2, modifiedPlugins.size());
		assertTrue(modifiedPlugins.contains("p1"));
	}

}
