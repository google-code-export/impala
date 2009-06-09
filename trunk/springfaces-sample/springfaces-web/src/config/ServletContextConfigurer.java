package config;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.impalaframework.util.InstantiationUtils;
import org.impalaframework.util.ObjectUtils;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletContextAware;

public class ServletContextConfigurer implements ServletContextAware, BeanClassLoaderAware, InitializingBean, DisposableBean {

	private ServletContext servletContext;
	
	private String listenerClassName;
	
	private ClassLoader classLoader;

	private ServletContextListener listener;

	public void afterPropertiesSet() throws Exception {
		Object instantiate = InstantiationUtils.instantiate(listenerClassName, classLoader);
		listener = ObjectUtils.cast(instantiate, ServletContextListener.class);
		listener.contextInitialized(new ServletContextEvent(servletContext));
	}

	public void destroy() throws Exception {
		listener.contextDestroyed(new ServletContextEvent(servletContext));
	}

	public void setListenerClassName(String listenerClassName) {
		this.listenerClassName = listenerClassName;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

}
