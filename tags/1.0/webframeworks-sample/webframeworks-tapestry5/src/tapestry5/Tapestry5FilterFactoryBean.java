package tapestry5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.impalaframework.module.definition.ModuleDefinitionAware;
import org.impalaframework.web.integration.IntegrationFilterConfig;
import org.impalaframework.web.servlet.wrapper.context.DelegatingServletContext;
import org.impalaframework.web.spring.integration.FilterFactoryBean;
import org.springframework.context.ApplicationContextAware;

public class Tapestry5FilterFactoryBean extends FilterFactoryBean implements ApplicationContextAware, ModuleDefinitionAware {

    @Override
    protected IntegrationFilterConfig newFilterConfig(Map<String, String> parameterMap) {
        
        ServletContext servletContext = new ExtendedServletContext(getServletContext(), parameterMap);
        IntegrationFilterConfig config = new IntegrationFilterConfig(parameterMap, servletContext, getFilterName());
        return config;
    }
    
}

class ExtendedServletContext extends DelegatingServletContext {
    
    private Map<String, String> localInitParams;
    
    private Enumeration<String> allInitParamNames;

    @SuppressWarnings("unchecked")
    public ExtendedServletContext(ServletContext realContext, Map<String, String> parameterMap) {
        super(realContext);
        this.localInitParams = parameterMap;
        List<String> currentParamNames = new ArrayList<String>(parameterMap.keySet());
        currentParamNames.addAll(Collections.list(realContext.getInitParameterNames()));
        allInitParamNames = Collections.enumeration(currentParamNames);
    }
    
    @Override
    public String getInitParameter(String name) {
        if (localInitParams.containsKey(name)) {
            return localInitParams.get(name);
        }
        return super.getInitParameter(name);
    }

    @Override
    public Enumeration<?> getInitParameterNames() {
        return allInitParamNames;
    }  
    
}



