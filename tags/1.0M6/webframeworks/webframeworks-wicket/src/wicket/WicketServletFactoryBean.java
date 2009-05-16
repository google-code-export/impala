package wicket;

import javax.servlet.http.HttpServlet;

import org.impalaframework.web.spring.helper.ImpalaServletUtils;
import org.impalaframework.web.spring.integration.ServletFactoryBean;

public class WicketServletFactoryBean extends ServletFactoryBean {

    @Override
    protected void initServletProperties(HttpServlet servlet) {
        //FIXME would be good to avoid having to do this here
        ImpalaServletUtils.publishRootModuleContext(getServletContext(), getModuleDefintion().getName(), getApplicationContext());
        super.initServletProperties(servlet);
    }

}
