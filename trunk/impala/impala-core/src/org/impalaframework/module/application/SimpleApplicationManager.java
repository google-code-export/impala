package org.impalaframework.module.application;

import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ApplicationFactory;
import org.impalaframework.module.spi.ApplicationManager;
import org.springframework.beans.factory.InitializingBean;

/**
 * Implementation of {@link ApplicationManager}, which can be used to 
 * access current application instance.
 * @author Phil Zoio
 */
public class SimpleApplicationManager implements InitializingBean, ApplicationManager {

    private ApplicationFactory applicationFactory;
    
    private ModuleManagementFacade moduleManagementFacade;
    
    private Application application;
    
    public void afterPropertiesSet() throws Exception {
        this.application = applicationFactory.newApplication();
    }
    
    public Application getCurrentApplication() {
        return this.application;
    }
    
    public void close() {
    }
    
    public void setApplicationFactory(ApplicationFactory applicationFactory) {
        this.applicationFactory = applicationFactory;
    }
    
    public void setModuleManagementFacade(ModuleManagementFacade moduleManagementFacade) {
        this.moduleManagementFacade = moduleManagementFacade;
    }
    
}
