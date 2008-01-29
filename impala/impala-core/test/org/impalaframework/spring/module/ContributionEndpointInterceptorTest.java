package org.impalaframework.spring.module;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.aopalliance.intercept.MethodInvocation;
import org.impalaframework.exception.NoServiceException;
import org.springframework.util.ReflectionUtils;

public class ContributionEndpointInterceptorTest extends TestCase {

	private ContributionEndpointTargetSource targetSource;

	private ContributionEndpointInterceptor interceptor;

	private MethodInvocation invocation;

	private Method method;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		targetSource = createMock(ContributionEndpointTargetSource.class);
		interceptor = new ContributionEndpointInterceptor(targetSource, "myBean");
		method = ReflectionUtils.findMethod(String.class, "toString", new Class[] {});
		assertNotNull(method);
		invocation = createMock(MethodInvocation.class);

	}

	public final void testInvoke() throws Throwable {
		expect(targetSource.hasTarget()).andReturn(true);
		Object result = new Object();
		expect(invocation.proceed()).andReturn(result);

		replayMocks();
		assertSame(result, interceptor.invoke(invocation));
		verifyMocks();
	}

	public final void testInvokeNoService() throws Throwable {
		expect(targetSource.hasTarget()).andReturn(false);

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

		expect(targetSource.hasTarget()).andReturn(false);
		expect(invocation.getMethod()).andReturn(method);

		replayMocks();
		assertNull(interceptor.invoke(invocation));
		verifyMocks();
	}

	private void verifyMocks() {
		verify(targetSource);
		verify(invocation);
	}

	private void replayMocks() {
		replay(targetSource);
		replay(invocation);
	}

}
