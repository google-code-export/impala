package org.impalaframework.module.application;

import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.module.operation.ModuleOperation;
import org.impalaframework.module.operation.ModuleOperationConstants;
import org.impalaframework.module.operation.ModuleOperationResult;
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
        this.application = applicationFactory.newApplication(null);
    }
    
    public Application getCurrentApplication() {
        return this.application;
    }
    
    public Application getApplication(String id) {
        return application;
    }
    
    public boolean close() {
        ModuleOperation operation = moduleManagementFacade.getModuleOperationRegistry().getOperation(ModuleOperationConstants.CloseRootModuleOperation);
        ModuleOperationResult execute = operation.execute(application, null);
        return execute.isErrorFree();
    }
    
    public void setApplicationFactory(ApplicationFactory applicationFactory) {
        this.applicationFactory = applicationFactory;
    }
    
    public void setModuleManagementFacade(ModuleManagementFacade moduleManagementFacade) {
        this.moduleManagementFacade = moduleManagementFacade;
    }
    
}
