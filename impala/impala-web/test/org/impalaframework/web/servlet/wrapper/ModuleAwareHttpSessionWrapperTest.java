package org.impalaframework.web.servlet.wrapper;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import junit.framework.TestCase;

import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.module.spi.ApplicationManager;
import org.impalaframework.module.spi.ModuleStateHolder;
import org.impalaframework.module.spi.TestApplicationManager;
import org.impalaframework.spring.module.SpringRuntimeModule;
import org.impalaframework.web.WebConstants;
import org.springframework.util.ClassUtils;

public class ModuleAwareHttpSessionWrapperTest extends TestCase {

    private HttpServletRequest request;
    private HttpSession session;
    private ServletContext servletContext;
    private ModuleAwareHttpSessionWrapper wrapper;
    private ModuleManagementFacade moduleManagementFacade;
    private ModuleStateHolder moduleStateHolder;
    private SpringRuntimeModule springRuntimeModule;
    private ApplicationManager applicationManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        request = createMock(HttpServletRequest.class);
        servletContext = createMock(ServletContext.class);
        session = createMock(HttpSession.class);
        moduleManagementFacade = createMock(ModuleManagementFacade.class);
        moduleStateHolder = createMock(ModuleStateHolder.class);
        springRuntimeModule = createMock(SpringRuntimeModule.class);
        wrapper = new ModuleAwareHttpSessionWrapper();
        wrapper.setServletContext(servletContext);
        
        applicationManager = TestApplicationManager.newApplicationManager(null, moduleStateHolder, null);
    }
    
    public void testGetSession() {
    
        expect(springRuntimeModule.getClassLoader()).andReturn(ClassUtils.getDefaultClassLoader());
        expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(moduleManagementFacade);
        expect(moduleManagementFacade.getApplicationManager()).andReturn(applicationManager);
        expect(moduleStateHolder.getModule("mymodule")).andReturn(springRuntimeModule);
        
        replayMocks();

        HttpSession wrappedSession = wrapper.wrapSession(session, "mymodule");
        assertTrue(wrappedSession instanceof SessionProtectingWrapperHttpSession);
        SessionProtectingWrapperHttpSession moduleAwareSession = (SessionProtectingWrapperHttpSession) wrappedSession;
        assertNotNull(moduleAwareSession.getModuleClassLoader());
        assertSame(session, moduleAwareSession.getRealSession());

        verifyMocks();
    }
    
    public void testGetSessionNoFactoryAvailable() {
        
        expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(null);
        
        replayMocks();

        HttpSession wrappedSession = wrapper.wrapSession(session, "mymodule");
        assertSame(session, wrappedSession);

        verifyMocks();
    }

    private void verifyMocks() {
        verify(request);
        verify(servletContext);
        verify(moduleManagementFacade);
        verify(moduleStateHolder);
        verify(springRuntimeModule);
    }

    private void replayMocks() {
        replay(request);
        replay(servletContext);
        replay(moduleManagementFacade);
        replay(moduleStateHolder);
        replay(springRuntimeModule);
    }


}
