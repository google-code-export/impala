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

package org.impalaframework.spring.module.application;

import static org.easymock.EasyMock.createMock;

import org.impalaframework.module.spi.Application;

import junit.framework.TestCase;

public class ApplicationAwarePostProcessorTest extends TestCase {
    
    private Application application;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        application = createMock(Application.class);
    }

    public void testPostProcessAfterInitialization() {
        ApplicationAwarePostProcessor postProcessor = new ApplicationAwarePostProcessor(application);
        TestApplicationAware bean = new TestApplicationAware();
        assertSame(bean, postProcessor.postProcessAfterInitialization(bean, "mybean"));
        assertNull(bean.getApplication());
    }

    public void testPostProcessBeforeInitialization() {
        ApplicationAwarePostProcessor postProcessor = new ApplicationAwarePostProcessor(application);
        TestApplicationAware bean = new TestApplicationAware();
        assertSame(bean, postProcessor.postProcessBeforeInitialization(bean, "mybean"));
        assertSame(application, bean.getApplication());
    }
}


class TestApplicationAware implements ApplicationAware {
    
    private Application application;

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }
}
