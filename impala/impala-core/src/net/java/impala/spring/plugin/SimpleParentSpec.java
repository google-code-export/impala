package net.java.impala.spring.plugin;

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

}
