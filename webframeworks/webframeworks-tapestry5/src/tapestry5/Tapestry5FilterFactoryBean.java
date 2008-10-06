package tapestry5;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.ServletContext;

import org.impalaframework.web.integration.FilterFactoryBean;
import org.impalaframework.web.integration.IntegrationFilterConfig;
import org.impalaframework.web.servlet.wrapper.DelegatingWrapperServletContext;

public class Tapestry5FilterFactoryBean extends FilterFactoryBean {

	private String applicationPackage;

	@Override
	protected void initFilterProperties(Filter servlet) {
		super.initFilterProperties(servlet);
	}

	@Override
	protected IntegrationFilterConfig newFilterConfig(Map<String, String> parameterMap) {
		
		ServletContext servletContext = new DelegatingWrapperServletContext(getServletContext()) {

			@Override
			public String getInitParameter(String name) {
				return applicationPackage;
			}

			@Override
			public Enumeration<?> getInitParameterNames() {
				//a bit of a hack because it restricts init parameters to just a single value
				return Collections.enumeration(Collections.singleton("tapestry.app-package"));
			}
			
		};
		IntegrationFilterConfig config = new IntegrationFilterConfig(parameterMap, servletContext, getFilterName());
		return config;
	}

	public void setApplicationPackage(String applicationPackage) {
		this.applicationPackage = applicationPackage;
	}
	
}



