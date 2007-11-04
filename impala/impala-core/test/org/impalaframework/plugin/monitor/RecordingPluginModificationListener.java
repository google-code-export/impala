package org.impalaframework.plugin.monitor;

import java.util.ArrayList;
import java.util.List;

import org.impalaframework.plugin.monitor.PluginModificationEvent;
import org.impalaframework.plugin.monitor.PluginModificationListener;

/**
 * @author Phil Zoio
 */
public class RecordingPluginModificationListener implements PluginModificationListener {

	private List<PluginModificationEvent> events = new ArrayList<PluginModificationEvent>();

	public void pluginModified(PluginModificationEvent event) {
		events.add(event);
	}

	protected List<PluginModificationEvent> getEvents() {
		return events;
	}
	
	protected void clear() {
		events.clear();
	}

}
