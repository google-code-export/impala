package org.impalaframework.web.module;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.modification.ModificationExtractorType;
import org.impalaframework.module.modification.ModificationExtractor;
import org.impalaframework.module.modification.ModificationExtractorRegistry;
import org.impalaframework.module.modification.ModuleStateChange;
import org.impalaframework.module.modification.TransitionSet;
import org.impalaframework.module.monitor.ModuleChangeEvent;
import org.impalaframework.module.monitor.ModuleChangeInfo;
import org.impalaframework.web.WebConstants;
import org.impalaframework.web.module.WebModuleChangeListener;

public class WebModuleChangeListenerTest extends TestCase {

	public final void testEmpty() throws Exception {

		ServletContext servletContext = createMock(ServletContext.class);

		final WebModuleChangeListener listener = new WebModuleChangeListener(servletContext);

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

	public final void testNonEmpty() throws Exception {


		ArrayList<ModuleChangeInfo> info = new ArrayList<ModuleChangeInfo>();

		info.add(new ModuleChangeInfo("p1"));

		ModuleChangeEvent event = new ModuleChangeEvent(info);

		ServletContext servletContext = createMock(ServletContext.class);
		ModuleManagementSource bootstrapFactory = createMock(ModuleManagementSource.class);
		ModuleStateHolder moduleStateHolder = createMock(ModuleStateHolder.class);

		final WebModuleChangeListener listener = new WebModuleChangeListener(servletContext);
		SimpleRootModuleDefinition simpleRootModuleDefinition = new SimpleRootModuleDefinition("p1");
		
		ModificationExtractor modificationCalculator = createMock(ModificationExtractor.class);
		ModificationExtractorRegistry registry = new ModificationExtractorRegistry();
		registry.addModificationCalculationType(ModificationExtractorType.STRICT, modificationCalculator);

		List<ModuleStateChange> transitions = new ArrayList<ModuleStateChange>();
		TransitionSet transitionSet = new TransitionSet(transitions, simpleRootModuleDefinition);
		
		// set expectations
		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(bootstrapFactory);
		expect(bootstrapFactory.getModuleStateHolder()).andReturn(moduleStateHolder);
		expect(moduleStateHolder.getRootModuleDefinition()).andReturn(simpleRootModuleDefinition);
		expect(bootstrapFactory.getModuleStateHolder()).andReturn(moduleStateHolder);
		expect(moduleStateHolder.cloneRootModuleDefinition()).andReturn(simpleRootModuleDefinition);
		expect(bootstrapFactory.getModificationExtractorRegistry()).andReturn(registry);
		expect(modificationCalculator.reload(simpleRootModuleDefinition, simpleRootModuleDefinition, "p1")).andReturn(transitionSet);
		moduleStateHolder.processTransitions(transitionSet);

		replay(servletContext);
		replay(bootstrapFactory);
		replay(moduleStateHolder);
		replay(modificationCalculator);

		listener.moduleContentsModified(event);

		verify(servletContext);
		verify(bootstrapFactory);
		verify(moduleStateHolder);
		verify(modificationCalculator);

	}
}
