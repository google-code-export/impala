package org.impalaframework.spring.jmx.mx4j;

import mx4j.tools.adaptor.http.HttpAdaptor;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class MX4JHttpAdaptorListener implements InitializingBean, DisposableBean {

	private HttpAdaptor httpAdaptor;
	
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(httpAdaptor, "httpAdaptor cannot be null");
		httpAdaptor.start();
	}

	public void destroy() throws Exception {
		httpAdaptor.stop();
	}

	public void setHttpAdaptor(HttpAdaptor httpAdaptor) {
		this.httpAdaptor = httpAdaptor;
	}

}
