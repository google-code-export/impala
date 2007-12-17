package org.impalaframework.module.modification;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.module.modification.ModificationCalculationType;
import org.impalaframework.module.modification.ModuleModificationCalculator;
import org.impalaframework.module.modification.ModuleModificationCalculatorRegistry;
import org.impalaframework.module.modification.StickyModuleModificationCalculator;

public class ModuleModificationCalculatorRegistryTest extends TestCase {

	private ModuleModificationCalculatorRegistry registry;

	@Override
	protected void setUp() throws Exception {
		registry = new ModuleModificationCalculatorRegistry();
	}

	public void testRegistry() throws Exception {
		try {
			assertNull(registry.getPluginModificationCalculator(ModificationCalculationType.STICKY));
			fail();
		}
		catch (NoServiceException e) {
			assertEquals("No ModuleModificationCalculator available for modification type STICKY", e.getMessage());
		}
		try {
			assertNull(registry.getPluginModificationCalculator("sticky"));
			fail();
		}
		catch (NoServiceException e) {
			assertEquals("No ModuleModificationCalculator available for modification type STICKY", e.getMessage());
		}

		StickyModuleModificationCalculator stickyModuleModificationCalculator = new StickyModuleModificationCalculator();

		Map<ModificationCalculationType, ModuleModificationCalculator> map = new HashMap<ModificationCalculationType, ModuleModificationCalculator>();
		map.put(ModificationCalculationType.STICKY, stickyModuleModificationCalculator);
		registry.setPluginModificationCalculators(map);

		assertNotNull(registry.getPluginModificationCalculator(ModificationCalculationType.STICKY));
		assertNotNull(registry.getPluginModificationCalculator("sticky"));
	}

	public void testRegistryWithStringMap() throws Exception {
		StickyModuleModificationCalculator stickyModuleModificationCalculator = new StickyModuleModificationCalculator();

		Map<String, ModuleModificationCalculator> map = new HashMap<String, ModuleModificationCalculator>();
		map.put("sticky", stickyModuleModificationCalculator);
		registry.setPluginModificationCalculatorMap(map);

		assertNotNull(registry.getPluginModificationCalculator(ModificationCalculationType.STICKY));
		assertNotNull(registry.getPluginModificationCalculator("sticky"));
	}
}
