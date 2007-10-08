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

	public boolean containsAll(ParentSpec alternative) {
		if (alternative == null)
			return false;

		final String[] alternativeLocations = alternative.getParentContextLocations();

		// check that each of the alternatives are contained in
		// parentContextLocations
		for (String alt : alternativeLocations) {
			boolean found = false;
			for (String thisOne : parentContextLocations) {
				if (thisOne.equals(alt)) {
					found = true;
					break;
				}
			}
			if (!found) {
				System.out.println("Unable to find " + alt);
				return false;
			}
		}

		return true;
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
