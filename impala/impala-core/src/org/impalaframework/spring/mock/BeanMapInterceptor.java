package org.impalaframework.spring.mock;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.impalaframework.spring.DebuggingInterceptor;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

public class BeanMapInterceptor extends DebuggingInterceptor {

	private BeanMap beanMap;

	public BeanMapInterceptor(BeanMap beanMap) {
		super();
		Assert.notNull(beanMap);
		this.beanMap = beanMap;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();
		
		Class<?>[] parameterTypes = method.getParameterTypes();
		Method beanMapMethod = ReflectionUtils.findMethod(beanMap.getClass(), method.getName(), parameterTypes);
		
		if (beanMapMethod != null) {
			return ReflectionUtils.invokeMethod(beanMapMethod, beanMap, invocation.getArguments());
		}
		
		return super.invoke(invocation);
	}

}
