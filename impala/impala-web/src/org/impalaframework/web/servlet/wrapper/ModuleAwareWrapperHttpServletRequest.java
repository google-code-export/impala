package org.impalaframework.web.servlet.wrapper;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.module.bootstrap.ModuleManagementFactory;
import org.impalaframework.web.helper.ImpalaServletUtils;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

public class ModuleAwareWrapperHttpServletRequest extends
		HttpServletRequestWrapper {

	private static final Log logger = LogFactory.getLog(ModuleAwareWrapperHttpServletRequest.class);

	private final String moduleName;
	private final ServletContext servletContext;

	public ModuleAwareWrapperHttpServletRequest(HttpServletRequest request, String moduleName,
			ServletContext servletContext) {
		super(request);
		Assert.notNull(request);
		Assert.notNull(moduleName);
		Assert.notNull(servletContext);
		this.moduleName = moduleName;
		this.servletContext = servletContext;
	}

	@Override
	public HttpSession getSession() {
		//FIXME test
		
		HttpSession session = super.getSession();
		return wrapSession(session);
	}

	@Override
	public HttpSession getSession(boolean create) {
		//FIXME test
		
		HttpSession session = super.getSession(create);
		return wrapSession(session);
	}

	private HttpSession wrapSession(HttpSession session) {
		if (session == null) {
			return null;
		}
		ModuleManagementFactory moduleManagementFactory = ImpalaServletUtils.getModuleManagementFactory(servletContext);
		if (moduleManagementFactory != null) {
			ConfigurableApplicationContext currentModuleContext = moduleManagementFactory.getModuleStateHolder().getModule(moduleName);
			
			if (currentModuleContext != null) {
				return new ModuleAwareWrapperHttpSession(session, currentModuleContext.getClassLoader());
			} else {
				logger.warn("No module application context associated with module: " + moduleName + ". Using unwrapped session");
				return session;
			}
		}
		return session;
	}	

}
