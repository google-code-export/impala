package org.impalaframework.plugin.modification;

import java.util.HashMap;
import java.util.Map;

import org.impalaframework.exception.NoServiceException;

import junit.framework.TestCase;

public class PluginModificationCalculatorRegistryTest extends TestCase {

	private PluginModificationCalculatorRegistry registry;

	@Override
	protected void setUp() throws Exception {
		registry = new PluginModificationCalculatorRegistry();
	}

	public void testRegistry() throws Exception {
		try {
			assertNull(registry.getPluginModificationCalculator(ModificationCalculationType.STICKY));
			fail();
		}
		catch (NoServiceException e) {
			assertEquals("No PluginModificationCalculator available for modification type STICKY", e.getMessage());
		}
		try {
			assertNull(registry.getPluginModificationCalculator("sticky"));
			fail();
		}
		catch (NoServiceException e) {
			assertEquals("No PluginModificationCalculator available for modification type STICKY", e.getMessage());
		}

		StickyPluginModificationCalculator stickyPluginModificationCalculator = new StickyPluginModificationCalculator();

		Map<ModificationCalculationType, PluginModificationCalculator> map = new HashMap<ModificationCalculationType, PluginModificationCalculator>();
		map.put(ModificationCalculationType.STICKY, stickyPluginModificationCalculator);
		registry.setPluginModificationCalculators(map);

		assertNotNull(registry.getPluginModificationCalculator(ModificationCalculationType.STICKY));
		assertNotNull(registry.getPluginModificationCalculator("sticky"));
	}

	public void testRegistryWithStringMap() throws Exception {
		StickyPluginModificationCalculator stickyPluginModificationCalculator = new StickyPluginModificationCalculator();

		Map<String, PluginModificationCalculator> map = new HashMap<String, PluginModificationCalculator>();
		map.put("sticky", stickyPluginModificationCalculator);
		registry.setPluginModificationCalculatorMap(map);

		assertNotNull(registry.getPluginModificationCalculator(ModificationCalculationType.STICKY));
		assertNotNull(registry.getPluginModificationCalculator("sticky"));
	}
}
