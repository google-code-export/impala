package org.impalaframework.module.modification;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.impalaframework.exception.NoServiceException;
import org.springframework.util.Assert;

public class ModuleModificationCalculatorRegistry {

	private Map<ModificationCalculationType, ModuleModificationCalculator> moduleModificationCalculators = new HashMap<ModificationCalculationType, ModuleModificationCalculator>();

	public ModuleModificationCalculator getPluginModificationCalculator(ModificationCalculationType type) {
		ModuleModificationCalculator calculator = moduleModificationCalculators.get(type);

		if (calculator == null) {
			throw new NoServiceException("No " + ModuleModificationCalculator.class.getSimpleName()
					+ " available for modification type " + type);
		}

		return calculator;
	}

	public ModuleModificationCalculator getPluginModificationCalculator(String type) {
		Assert.notNull(type);
		ModificationCalculationType enumType = ModificationCalculationType.valueOf(type.toUpperCase());
		return getPluginModificationCalculator(enumType);
	}

	public void setPluginModificationCalculators(
			Map<ModificationCalculationType, ModuleModificationCalculator> calculators) {
		this.moduleModificationCalculators.clear();
		this.moduleModificationCalculators.putAll(calculators);
	}

	public void setPluginModificationCalculatorMap(Map<String, ModuleModificationCalculator> calculators) {
		this.moduleModificationCalculators.clear();
		Set<String> keySet = calculators.keySet();
		for (String typeName : keySet) {
			ModificationCalculationType type = ModificationCalculationType.valueOf(typeName.toUpperCase());
			addModificationCalculationType(type, calculators.get(typeName));
		}
	}

	public void addModificationCalculationType(ModificationCalculationType type, ModuleModificationCalculator calculator) {
		this.moduleModificationCalculators.put(type, calculator);
	}

}
