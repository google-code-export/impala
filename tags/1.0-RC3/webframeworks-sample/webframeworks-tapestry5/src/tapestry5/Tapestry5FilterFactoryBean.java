package tapestry5;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletContext;

import org.impalaframework.module.definition.ModuleDefinitionAware;
import org.impalaframework.web.integration.IntegrationFilterConfig;
import org.impalaframework.web.servlet.wrapper.context.DelegatingServletContext;
import org.impalaframework.web.spring.integration.FilterFactoryBean;
import org.springframework.context.ApplicationContextAware;

public class Tapestry5FilterFactoryBean extends FilterFactoryBean implements ApplicationContextAware, ModuleDefinitionAware {

    private String applicationPackage;

    @Override
    protected IntegrationFilterConfig newFilterConfig(Map<String, String> parameterMap) {
        
        ServletContext servletContext = new DelegatingServletContext(getServletContext()) {

            @Override
            public String getInitParameter(String name) {
                if (name.equals("tapestry.use-external-spring-context")) {
                    return "true";
                } if (name.equals("tapestry.app-package"))
                    return applicationPackage;
                return super.getInitParameter(name);
            }

            @Override
            public Enumeration<?> getInitParameterNames() {
                //a bit of a hack because it restricts init parameters to just a single value
                return Collections.enumeration(Arrays.asList("tapestry.app-package", "tapestry.use-external-spring-context"));
            }
            
        };
        IntegrationFilterConfig config = new IntegrationFilterConfig(parameterMap, servletContext, getFilterName());
        return config;
    }

    public void setApplicationPackage(String applicationPackage) {
        this.applicationPackage = applicationPackage;
    }
    
}



