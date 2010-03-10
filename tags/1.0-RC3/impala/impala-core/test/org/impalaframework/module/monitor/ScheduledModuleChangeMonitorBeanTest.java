/*
 * Copyright 2007-2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.module.monitor;

import org.impalaframework.module.monitor.ScheduledModuleChangeMonitorBean;

import junit.framework.TestCase;

public class ScheduledModuleChangeMonitorBeanTest extends TestCase {
    
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

class TestBean extends ScheduledModuleChangeMonitorBean {

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
