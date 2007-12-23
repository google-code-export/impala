package org.impalaframework.module.operation;

import java.util.Collections;
import java.util.Map;

import org.springframework.util.Assert;

public class ModuleOperationResult {

	public static final ModuleOperationResult TRUE = new ModuleOperationResult(true);
	public static final ModuleOperationResult FALSE = new ModuleOperationResult(false);
	
	private final boolean success;

	private final Map<String, Object> outputParameters;

	public ModuleOperationResult(final boolean success) {
		super();
		this.success = success;
		this.outputParameters = Collections.emptyMap();
	}	
	
	public ModuleOperationResult(final boolean success, final Map<String, Object> outputValues) {
		super();
		this.success = success;
		Assert.notNull(outputValues);
		this.outputParameters = outputValues;
	}

	public Map<String, Object> getOutputParameters() {
		return outputParameters;
	}

	public boolean isSuccess() {
		return success;
	}

}
