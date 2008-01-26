package org.impalaframework.module.monitor;

import java.util.ArrayList;
import java.util.List;

import org.impalaframework.module.monitor.ModuleChangeEvent;
import org.impalaframework.module.monitor.ModuleContentChangeListener;

/**
 * @author Phil Zoio
 */
public class RecordingModuleChangeListener implements ModuleContentChangeListener {

	private List<ModuleChangeEvent> events = new ArrayList<ModuleChangeEvent>();

	public void moduleContentsModified(ModuleChangeEvent event) {
		events.add(event);
	}

	protected List<ModuleChangeEvent> getEvents() {
		return events;
	}
	
	protected void clear() {
		events.clear();
	}

}
