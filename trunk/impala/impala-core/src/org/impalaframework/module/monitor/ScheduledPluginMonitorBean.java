package org.impalaframework.module.monitor;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class ScheduledPluginMonitorBean extends ScheduledPluginMonitor implements InitializingBean, DisposableBean {

	public void afterPropertiesSet() throws Exception {
		this.start();
	}

	public void destroy() throws Exception {
		this.stop();
	}
}
