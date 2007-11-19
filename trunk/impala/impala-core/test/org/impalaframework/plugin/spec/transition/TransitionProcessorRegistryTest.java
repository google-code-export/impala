package org.impalaframework.plugin.spec.transition;

import java.util.HashMap;
import java.util.Map;

import static org.easymock.EasyMock.*;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.plugin.spec.modification.PluginTransition;

import junit.framework.TestCase;

public class TransitionProcessorRegistryTest extends TestCase {

	public final void testGetTransitionProcessor() {
		TransitionProcessorRegistry registry = new TransitionProcessorRegistry();
		Map<PluginTransition, TransitionProcessor> transitionProcessors = new HashMap<PluginTransition, TransitionProcessor>();

		TransitionProcessor transitionProcessor1 = createMock(TransitionProcessor.class);
		transitionProcessors.put(PluginTransition.LOADED_TO_UNLOADED, transitionProcessor1);

		TransitionProcessor transitionProcessor2 = createMock(TransitionProcessor.class);
		transitionProcessors.put(PluginTransition.UNLOADED_TO_LOADED, transitionProcessor2);
		registry.setTransitionProcessors(transitionProcessors);

		assertSame(transitionProcessor1, registry.getTransitionProcessor(PluginTransition.LOADED_TO_UNLOADED));
		assertSame(transitionProcessor2, registry.getTransitionProcessor(PluginTransition.UNLOADED_TO_LOADED));

		try {
			registry.getTransitionProcessor(PluginTransition.CONTEXT_LOCATIONS_ADDED);
			fail();
		}
		catch (NoServiceException e) {
			assertEquals("No TransitionProcessor set up for transition CONTEXT_LOCATIONS_ADDED", e.getMessage());
		}
	}

	public void testSetMap() {
		TransitionProcessorRegistry registry = new TransitionProcessorRegistry();
		Map<String, TransitionProcessor> transitionProcessors = new HashMap<String, TransitionProcessor>();

		TransitionProcessor transitionProcessor1 = createMock(TransitionProcessor.class);
		transitionProcessors.put("LOADED_TO_UNLOADED", transitionProcessor1);

		TransitionProcessor transitionProcessor2 = createMock(TransitionProcessor.class);
		transitionProcessors.put("UNLOADED_TO_LOADED", transitionProcessor2);
		registry.setTransitionProcessorMap(transitionProcessors);

		assertSame(transitionProcessor1, registry.getTransitionProcessor(PluginTransition.LOADED_TO_UNLOADED));
		assertSame(transitionProcessor2, registry.getTransitionProcessor(PluginTransition.UNLOADED_TO_LOADED));
	}

}
