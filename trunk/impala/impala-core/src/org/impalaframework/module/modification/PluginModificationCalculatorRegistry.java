package org.impalaframework.module.modification;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.impalaframework.exception.NoServiceException;
import org.springframework.util.Assert;

public class PluginModificationCalculatorRegistry {

	private Map<ModificationCalculationType, PluginModificationCalculator> pluginModificationCalculators = new HashMap<ModificationCalculationType, PluginModificationCalculator>();

	public PluginModificationCalculator getPluginModificationCalculator(ModificationCalculationType type) {
		PluginModificationCalculator calculator = pluginModificationCalculators.get(type);

		if (calculator == null) {
			throw new NoServiceException("No " + PluginModificationCalculator.class.getSimpleName()
					+ " available for modification type " + type);
		}

		return calculator;
	}

	public PluginModificationCalculator getPluginModificationCalculator(String type) {
		Assert.notNull(type);
		ModificationCalculationType enumType = ModificationCalculationType.valueOf(type.toUpperCase());
		return getPluginModificationCalculator(enumType);
	}

	public void setPluginModificationCalculators(
			Map<ModificationCalculationType, PluginModificationCalculator> calculators) {
		this.pluginModificationCalculators.clear();
		this.pluginModificationCalculators.putAll(calculators);
	}

	public void setPluginModificationCalculatorMap(Map<String, PluginModificationCalculator> calculators) {
		this.pluginModificationCalculators.clear();
		Set<String> keySet = calculators.keySet();
		for (String typeName : keySet) {
			ModificationCalculationType type = ModificationCalculationType.valueOf(typeName.toUpperCase());
			addModificationCalculationType(type, calculators.get(typeName));
		}
	}

	public void addModificationCalculationType(ModificationCalculationType type, PluginModificationCalculator calculator) {
		this.pluginModificationCalculators.put(type, calculator);
	}

}
