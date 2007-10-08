package net.java.impala.spring.plugin;

import java.util.Arrays;

import org.springframework.util.Assert;

public class SimpleParentSpec implements ParentSpec {

	private String[] parentContextLocations;

	public SimpleParentSpec(String[] parentContextLocations) {
		super();
		Assert.notNull(parentContextLocations);
		for (int i = 0; i < parentContextLocations.length; i++) {
			Assert.notNull(parentContextLocations[i]);
		}
		this.parentContextLocations = parentContextLocations;
	}

	public String[] getParentContextLocations() {
		return parentContextLocations;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + Arrays.hashCode(parentContextLocations);
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
		final SimpleParentSpec other = (SimpleParentSpec) obj;
		if (!Arrays.equals(parentContextLocations, other.parentContextLocations))
			return false;
		return true;
	}

}
