/*
 * Copyright 2007-2008 the original author or authors.
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

package org.impalaframework.spring.service.exporter;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.aopalliance.intercept.MethodInvocation;
import org.impalaframework.exception.NoServiceException;
import org.impalaframework.service.ServiceRegistryReference;
import org.impalaframework.spring.service.ContributionEndpointTargetSource;
import org.impalaframework.spring.service.proxy.ContributionEndpointInterceptor;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

public class ContributionEndpointInterceptorTest extends TestCase {

    private ContributionEndpointTargetSource targetSource;

    private ContributionEndpointInterceptor interceptor;

    private MethodInvocation invocation;

    private Method method;

    private ServiceRegistryReference serviceRegistryReference;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        targetSource = createMock(ContributionEndpointTargetSource.class);
        serviceRegistryReference = createMock(ServiceRegistryReference.class);
        interceptor = new ContributionEndpointInterceptor(targetSource, "myBean");
        method = ReflectionUtils.findMethod(String.class, "toString", new Class[] {});
        assertNotNull(method);
        invocation = createMock(MethodInvocation.class);

    }

    public final void testInvoke() throws Throwable {
        interceptor.setSetContextClassLoader(true);
        Object result = new Object();
        expect(targetSource.getServiceRegistryReference()).andReturn(serviceRegistryReference);
        expect(serviceRegistryReference.getBeanClassLoader()).andReturn(ClassUtils.getDefaultClassLoader());
        expect(invocation.proceed()).andReturn(result);

        replayMocks();
        assertSame(result, interceptor.invoke(invocation));
        verifyMocks();
    }
    
    public final void testInvokeNoSetContextClassLoader() throws Throwable {
        interceptor.setSetContextClassLoader(false);
        expect(targetSource.getServiceRegistryReference()).andReturn(serviceRegistryReference);
        Object result = new Object();
        expect(invocation.proceed()).andReturn(result);

        replayMocks();
        assertSame(result, interceptor.invoke(invocation));
        verifyMocks();
    }

    public final void testInvokeNoService() throws Throwable {
        expect(targetSource.getServiceRegistryReference()).andReturn(null);

        replayMocks();
        try {
            interceptor.invoke(invocation);
            fail();
        }
        catch (NoServiceException e) {
        }
        verifyMocks();
    }

    public final void testInvokeDummy() throws Throwable {
        interceptor.setProceedWithNoService(true);
        interceptor.setLogWarningNoService(true);

        expect(targetSource.getServiceRegistryReference()).andReturn(null);
        expect(invocation.getMethod()).andReturn(method);

        replayMocks();
        assertNull(interceptor.invoke(invocation));
        verifyMocks();
    }

    private void verifyMocks() {
        verify(targetSource);
        verify(invocation);
        verify(serviceRegistryReference);
    }

    private void replayMocks() {
        replay(targetSource);
        replay(invocation);
        replay(serviceRegistryReference);
    }

}
