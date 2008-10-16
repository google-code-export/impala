package tapestry5;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.ServletContext;

import org.impalaframework.module.definition.ModuleDefinitionAware;
import org.impalaframework.web.helper.ImpalaServletUtils;
import org.impalaframework.web.integration.FilterFactoryBean;
import org.impalaframework.web.integration.IntegrationFilterConfig;
import org.impalaframework.web.servlet.wrapper.DelegatingWrapperServletContext;
import org.springframework.context.ApplicationContextAware;

public class Tapestry5FilterFactoryBean extends FilterFactoryBean implements ApplicationContextAware, ModuleDefinitionAware {

	private String applicationPackage;

	@Override
	protected void initFilterProperties(Filter servlet) {
		//FIXME would be good to avoid having to do this here
		ImpalaServletUtils.publishRootModuleContext(getServletContext(), getModuleDefintion().getName(), getApplicationContext());
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



