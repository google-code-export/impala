package org.impalaframework.module.transition;

import static org.easymock.EasyMock.createMock;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.module.modification.Transition;
import org.impalaframework.module.transition.TransitionProcessor;
import org.impalaframework.module.transition.TransitionProcessorRegistry;

public class TransitionProcessorRegistryTest extends TestCase {

	private TransitionProcessorRegistry registry;

	public void setUp() {
		registry = new TransitionProcessorRegistry();
	}
	
	public final void testGetTransitionProcessor() {
		Map<Transition, TransitionProcessor> transitionProcessors = new HashMap<Transition, TransitionProcessor>();

		TransitionProcessor transitionProcessor1 = createMock(TransitionProcessor.class);
		transitionProcessors.put(Transition.LOADED_TO_UNLOADED, transitionProcessor1);

		TransitionProcessor transitionProcessor2 = createMock(TransitionProcessor.class);
		transitionProcessors.put(Transition.UNLOADED_TO_LOADED, transitionProcessor2);
		registry.setTransitionProcessors(transitionProcessors);

		assertSame(transitionProcessor1, registry.getTransitionProcessor(Transition.LOADED_TO_UNLOADED));
		assertSame(transitionProcessor2, registry.getTransitionProcessor(Transition.UNLOADED_TO_LOADED));

		try {
			registry.getTransitionProcessor(Transition.CONTEXT_LOCATIONS_ADDED);
			fail();
		}
		catch (NoServiceException e) {
			assertEquals("No TransitionProcessor set up for transition CONTEXT_LOCATIONS_ADDED", e.getMessage());
		}
	}

	public void testSetMap() {
		Map<String, TransitionProcessor> transitionProcessors = new HashMap<String, TransitionProcessor>();

		TransitionProcessor transitionProcessor1 = createMock(TransitionProcessor.class);
		transitionProcessors.put("LOADED_TO_UNLOADED", transitionProcessor1);

		TransitionProcessor transitionProcessor2 = createMock(TransitionProcessor.class);
		transitionProcessors.put("UNLOADED_TO_LOADED", transitionProcessor2);
		registry.setTransitionProcessorMap(transitionProcessors);

		assertSame(transitionProcessor1, registry.getTransitionProcessor(Transition.LOADED_TO_UNLOADED));
		assertSame(transitionProcessor2, registry.getTransitionProcessor(Transition.UNLOADED_TO_LOADED));
	}
	
	public void testNoTransition() {
		Map<String, TransitionProcessor> transitionProcessors = new HashMap<String, TransitionProcessor>();
		TransitionProcessor transitionProcessor = createMock(TransitionProcessor.class);
		transitionProcessors.put("unknown", transitionProcessor);
		try {
			registry.setTransitionProcessorMap(transitionProcessors);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("No enum const class org.impalaframework.module.modification.Transition.unknown", e.getMessage());
		}
		
	}
	

}
