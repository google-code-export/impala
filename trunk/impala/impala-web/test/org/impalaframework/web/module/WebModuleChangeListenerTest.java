package org.impalaframework.web.module;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.module.bootstrap.ModuleManagementFactory;
import org.impalaframework.module.monitor.ModuleChangeEvent;
import org.impalaframework.module.monitor.ModuleChangeInfo;
import org.impalaframework.module.operation.ModuleOperation;
import org.impalaframework.module.operation.ModuleOperationConstants;
import org.impalaframework.module.operation.ModuleOperationInput;
import org.impalaframework.module.operation.ModuleOperationRegistry;
import org.impalaframework.module.operation.ModuleOperationResult;
import org.impalaframework.web.WebConstants;

public class WebModuleChangeListenerTest extends TestCase {

	public final void testEmpty() throws Exception {

		ServletContext servletContext = createMock(ServletContext.class);

		final WebModuleChangeListener listener = new WebModuleChangeListener();
		listener.setServletContext(servletContext);

		ArrayList<ModuleChangeInfo> info = new ArrayList<ModuleChangeInfo>();
		ModuleChangeEvent event = new ModuleChangeEvent(info);

		replay(servletContext);

		listener.moduleContentsModified(event);

		verify(servletContext);

		info.add(new ModuleChangeInfo("p1"));
		info.add(new ModuleChangeInfo("p2"));
		info.add(new ModuleChangeInfo("p2"));

		event = new ModuleChangeEvent(info);

	}	
	
	public final void testEventsNonEmpty() throws Exception {

		ArrayList<ModuleChangeInfo> info = new ArrayList<ModuleChangeInfo>();

		info.add(new ModuleChangeInfo("p1"));

		ModuleChangeEvent event = new ModuleChangeEvent(info);
		ServletContext servletContext = createMock(ServletContext.class);
		final WebModuleChangeListener listener = new WebModuleChangeListener();
		listener.setServletContext(servletContext);
		ModuleManagementFactory bootstrapFactory = createMock(ModuleManagementFactory.class);
		ModuleOperationRegistry moduleOperationRegistry = createMock(ModuleOperationRegistry.class);
		ModuleOperation moduleOperation = createMock(ModuleOperation.class);

		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(bootstrapFactory);
		expect(bootstrapFactory.getModuleOperationRegistry()).andReturn(moduleOperationRegistry);
		expect(moduleOperationRegistry.getOperation(ModuleOperationConstants.ReloadNamedModuleOperation)).andReturn(moduleOperation);
		expect(moduleOperation.execute(isA(ModuleOperationInput.class))).andReturn(ModuleOperationResult.TRUE);
		
		replay(servletContext);
		replay(bootstrapFactory);
		replay(moduleOperationRegistry);
		replay(moduleOperation);
		
		listener.moduleContentsModified(event);

		verify(servletContext);
		verify(bootstrapFactory);
		verify(moduleOperationRegistry);
		verify(moduleOperation);

	}
}
