package org.impalaframework.module.monitor;

import org.impalaframework.module.monitor.ScheduledPluginMonitorBean;

import junit.framework.TestCase;

public class ScheduledPluginMonitorBeanTest extends TestCase {
	
	public final void testStart() throws Exception {
		TestBean bean = new TestBean();
		assertEquals(0, bean.getStartCount());
		bean.afterPropertiesSet();
		assertEquals(1, bean.getStartCount());
	}
	
	public final void testDestroy() throws Exception {
		TestBean bean = new TestBean();
		assertEquals(0, bean.getStopCount());
		bean.destroy();
		assertEquals(1, bean.getStopCount());
	}
}

class TestBean extends ScheduledPluginMonitorBean {

	private int stopCount;

	private int startCount;

	@Override
	public void start() {
		startCount++;
	}

	@Override
	public void stop() {
		stopCount++;
	}

	public int getStopCount() {
		return stopCount;
	}

	public int getStartCount() {
		return startCount;
	}

}
