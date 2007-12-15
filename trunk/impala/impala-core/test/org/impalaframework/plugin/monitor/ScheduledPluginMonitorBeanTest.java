package org.impalaframework.plugin.monitor;

import junit.framework.TestCase;

public class ScheduledPluginMonitorBeanTest extends TestCase {

	public final void testDestroy() throws Exception {
		TestBean bean = new TestBean();
		assertEquals(0, bean.getCount());
		bean.destroy();
		assertEquals(1, bean.getCount());
	}
}

class TestBean extends ScheduledPluginMonitorBean {

	private int count;

	@Override
	public void stop() {
		count++;
	}

	public int getCount() {
		return count;
	}

}
