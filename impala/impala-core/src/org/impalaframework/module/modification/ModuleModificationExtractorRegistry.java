package org.impalaframework.module.modification;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.impalaframework.exception.NoServiceException;
import org.springframework.util.Assert;

public class ModuleModificationExtractorRegistry {

	private Map<ModificationExtractorType, ModuleModificationExtractor> moduleModificationExtractors = new HashMap<ModificationExtractorType, ModuleModificationExtractor>();

	public ModuleModificationExtractor getPluginModificationCalculator(ModificationExtractorType type) {
		ModuleModificationExtractor calculator = moduleModificationExtractors.get(type);

		if (calculator == null) {
			throw new NoServiceException("No " + ModuleModificationExtractor.class.getSimpleName()
					+ " available for modification type " + type);
		}

		return calculator;
	}

	public ModuleModificationExtractor getPluginModificationCalculator(String type) {
		Assert.notNull(type);
		ModificationExtractorType enumType = ModificationExtractorType.valueOf(type.toUpperCase());
		return getPluginModificationCalculator(enumType);
	}

	public void setPluginModificationCalculators(
			Map<ModificationExtractorType, ModuleModificationExtractor> calculators) {
		this.moduleModificationExtractors.clear();
		this.moduleModificationExtractors.putAll(calculators);
	}

	public void setPluginModificationCalculatorMap(Map<String, ModuleModificationExtractor> calculators) {
		this.moduleModificationExtractors.clear();
		Set<String> keySet = calculators.keySet();
		for (String typeName : keySet) {
			ModificationExtractorType type = ModificationExtractorType.valueOf(typeName.toUpperCase());
			addModificationCalculationType(type, calculators.get(typeName));
		}
	}

	public void addModificationCalculationType(ModificationExtractorType type, ModuleModificationExtractor calculator) {
		this.moduleModificationExtractors.put(type, calculator);
	}

}
