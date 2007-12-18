package org.impalaframework.module.monitor;

import java.util.ArrayList;
import java.util.List;

import org.impalaframework.module.monitor.ModuleChangeEvent;
import org.impalaframework.module.monitor.ModuleChangeListener;

/**
 * @author Phil Zoio
 */
public class RecordingModuleChangeListener implements ModuleChangeListener {

	private List<ModuleChangeEvent> events = new ArrayList<ModuleChangeEvent>();

	public void pluginModified(ModuleChangeEvent event) {
		events.add(event);
	}

	protected List<ModuleChangeEvent> getEvents() {
		return events;
	}
	
	protected void clear() {
		events.clear();
	}

}
