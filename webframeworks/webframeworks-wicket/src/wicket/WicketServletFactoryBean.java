package wicket;

import javax.servlet.http.HttpServlet;

import org.impalaframework.web.helper.ImpalaServletUtils;
import org.impalaframework.web.integration.ServletFactoryBean;

public class WicketServletFactoryBean extends ServletFactoryBean {

	@Override
	protected void initServletProperties(HttpServlet servlet) {
		ImpalaServletUtils.publishRootModuleContext(getServletContext(), getModuleDefintion().getName(), getApplicationContext());
		super.initServletProperties(servlet);
	}

}
