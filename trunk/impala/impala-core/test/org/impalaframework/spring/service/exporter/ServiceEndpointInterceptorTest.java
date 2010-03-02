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
import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.service.StaticServiceBeanReference;
import org.impalaframework.spring.service.ServiceEndpointTargetSource;
import org.impalaframework.spring.service.proxy.ServiceEndpointInterceptor;
import org.impalaframework.spring.service.proxy.ServiceEndpointOptionsHelper;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

public class ServiceEndpointInterceptorTest extends TestCase {

    private ServiceEndpointTargetSource targetSource;

    private ServiceEndpointInterceptor interceptor;

    private MethodInvocation invocation;

    private Method method;

    private ServiceRegistryEntry serviceRegistryReference;

    private Object result;

    private Object[] arguments;

    private ServiceEndpointOptionsHelper optionsHelper;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        targetSource = createMock(ServiceEndpointTargetSource.class);
        serviceRegistryReference = createMock(ServiceRegistryEntry.class);
        optionsHelper = new ServiceEndpointOptionsHelper(null);
        interceptor = new ServiceEndpointInterceptor(targetSource, "myBean", optionsHelper);
        result = "somestring";
        arguments = new Object[0];
        
        method = ReflectionUtils.findMethod(String.class, "toString", new Class[] {});
        assertNotNull(method);
        invocation = createMock(MethodInvocation.class);

    }

    public final void testInvoke() throws Throwable {
        optionsHelper.setSetContextClassLoader(true);
        
        invocationExpectations();        
        expect(targetSource.getServiceRegistryReference()).andReturn(serviceRegistryReference);
        serviceRegistryReferenceExpectations(true);

        replayMocks();
        assertSame(result, interceptor.invoke(invocation));
        verifyMocks();
    }
    
    public final void testInvokeNoSetContextClassLoader() throws Throwable {
        
        optionsHelper.setSetContextClassLoader(false);
        
        invocationExpectations();
        expect(targetSource.getServiceRegistryReference()).andReturn(serviceRegistryReference);
        serviceRegistryReferenceExpectations(false);

        replayMocks();
        assertSame(result, interceptor.invoke(invocation));
        verifyMocks();
    }

    public final void testInvokeNoService() throws Throwable {
        
        invocationExpectations();
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
    
    public void testInvokeWithRetries() throws Throwable {

        invocationExpectations();
        
        optionsHelper.setRetryInterval(50);
        optionsHelper.setRetryCount(2);
        expect(targetSource.getServiceRegistryReference()).andReturn(null);
        expect(targetSource.getServiceRegistryReference()).andReturn(null);
        expect(targetSource.getServiceRegistryReference()).andReturn(serviceRegistryReference);
        serviceRegistryReferenceExpectations(false);

        replayMocks();
        assertSame(result, interceptor.invoke(invocation));
        verifyMocks();
    }

    public final void testInvokeDummy() throws Throwable {
        
        invocationExpectations();
        
        optionsHelper.setProceedWithNoService(true);
        optionsHelper.setLogWarningNoService(true);

        expect(targetSource.getServiceRegistryReference()).andReturn(null);
        expect(invocation.getMethod()).andReturn(method);

        replayMocks();
        assertNull(interceptor.invoke(invocation));
        verifyMocks();
    }

    private void invocationExpectations() {
        expect(invocation.getMethod()).andReturn(method);
        expect(invocation.getArguments()).andReturn(arguments);
    }

    private void serviceRegistryReferenceExpectations(boolean getClassLoader) {
        if (getClassLoader) {
            expect(serviceRegistryReference.getBeanClassLoader()).andReturn(ClassUtils.getDefaultClassLoader());
        }
        expect(serviceRegistryReference.getServiceBeanReference()).andReturn(new StaticServiceBeanReference(result));
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
