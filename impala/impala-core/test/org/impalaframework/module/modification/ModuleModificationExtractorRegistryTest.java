package org.impalaframework.module.modification;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.module.modification.ModificationExtractorType;
import org.impalaframework.module.modification.ModuleModificationExtractor;
import org.impalaframework.module.modification.ModuleModificationExtractorRegistry;
import org.impalaframework.module.modification.StickyModuleModificationExtractor;

public class ModuleModificationExtractorRegistryTest extends TestCase {

	private ModuleModificationExtractorRegistry registry;

	@Override
	protected void setUp() throws Exception {
		registry = new ModuleModificationExtractorRegistry();
	}

	public void testRegistry() throws Exception {
		try {
			assertNull(registry.getPluginModificationCalculator(ModificationExtractorType.STICKY));
			fail();
		}
		catch (NoServiceException e) {
			assertEquals("No ModuleModificationExtractor available for modification type STICKY", e.getMessage());
		}
		try {
			assertNull(registry.getPluginModificationCalculator("sticky"));
			fail();
		}
		catch (NoServiceException e) {
			assertEquals("No ModuleModificationExtractor available for modification type STICKY", e.getMessage());
		}

		StickyModuleModificationExtractor stickyModuleModificationExtractor = new StickyModuleModificationExtractor();

		Map<ModificationExtractorType, ModuleModificationExtractor> map = new HashMap<ModificationExtractorType, ModuleModificationExtractor>();
		map.put(ModificationExtractorType.STICKY, stickyModuleModificationExtractor);
		registry.setPluginModificationCalculators(map);

		assertNotNull(registry.getPluginModificationCalculator(ModificationExtractorType.STICKY));
		assertNotNull(registry.getPluginModificationCalculator("sticky"));
	}

	public void testRegistryWithStringMap() throws Exception {
		StickyModuleModificationExtractor stickyModuleModificationExtractor = new StickyModuleModificationExtractor();

		Map<String, ModuleModificationExtractor> map = new HashMap<String, ModuleModificationExtractor>();
		map.put("sticky", stickyModuleModificationExtractor);
		registry.setPluginModificationCalculatorMap(map);

		assertNotNull(registry.getPluginModificationCalculator(ModificationExtractorType.STICKY));
		assertNotNull(registry.getPluginModificationCalculator("sticky"));
	}
}
