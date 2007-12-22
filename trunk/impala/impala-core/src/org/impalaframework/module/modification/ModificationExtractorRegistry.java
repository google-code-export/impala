package org.impalaframework.module.modification;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.impalaframework.exception.NoServiceException;
import org.springframework.util.Assert;

public class ModificationExtractorRegistry {

	private Map<ModificationExtractorType, ModificationExtractor> modificationExtractors = new HashMap<ModificationExtractorType, ModificationExtractor>();

	public ModificationExtractor getModificationExtractor(ModificationExtractorType type) {
		ModificationExtractor calculator = modificationExtractors.get(type);

		if (calculator == null) {
			throw new NoServiceException("No " + ModificationExtractor.class.getSimpleName()
					+ " available for modification type " + type);
		}

		return calculator;
	}

	public ModificationExtractor getPluginModificationCalculator(String type) {
		Assert.notNull(type);
		ModificationExtractorType enumType = ModificationExtractorType.valueOf(type.toUpperCase());
		return getModificationExtractor(enumType);
	}

	public void setPluginModificationCalculators(
			Map<ModificationExtractorType, ModificationExtractor> calculators) {
		this.modificationExtractors.clear();
		this.modificationExtractors.putAll(calculators);
	}

	public void setPluginModificationCalculatorMap(Map<String, ModificationExtractor> calculators) {
		this.modificationExtractors.clear();
		Set<String> keySet = calculators.keySet();
		for (String typeName : keySet) {
			ModificationExtractorType type = ModificationExtractorType.valueOf(typeName.toUpperCase());
			addModificationCalculationType(type, calculators.get(typeName));
		}
	}

	public void addModificationCalculationType(ModificationExtractorType type, ModificationExtractor calculator) {
		this.modificationExtractors.put(type, calculator);
	}

}
