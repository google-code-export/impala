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

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((outputParameters == null) ? 0 : outputParameters.hashCode());
		result = PRIME * result + (success ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ModuleOperationResult other = (ModuleOperationResult) obj;
		if (outputParameters == null) {
			if (other.outputParameters != null)
				return false;
		}
		else if (!outputParameters.equals(other.outputParameters))
			return false;
		if (success != other.success)
			return false;
		return true;
	}

}
