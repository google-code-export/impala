package org.impalaframework.plugin.monitor;

import org.springframework.beans.factory.DisposableBean;

public class ScheduledPluginMonitorBean extends ScheduledPluginMonitor implements DisposableBean {

	public void destroy() throws Exception {
		//FIXME test
		super.stop();
	}

}
