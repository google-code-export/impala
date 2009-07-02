package wicket;

import javax.servlet.Servlet;

import org.impalaframework.web.spring.helper.ImpalaServletUtils;
import org.impalaframework.web.spring.integration.ServletFactoryBean;

public class WicketServletFactoryBean extends ServletFactoryBean {

    @Override
    protected void initServletProperties(Servlet servlet) {
        //FIXME would be good to avoid having to do this here
        ImpalaServletUtils.publishRootModuleContext(getServletContext(), getModuleDefintion().getName(), getApplicationContext());
        super.initServletProperties(servlet);
    }

}
