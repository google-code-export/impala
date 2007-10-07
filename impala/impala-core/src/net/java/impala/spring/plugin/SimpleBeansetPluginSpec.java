package net.java.impala.spring.plugin;

import java.util.Collections;
import java.util.Map;

import org.springframework.util.Assert;

public class SimpleBeansetPluginSpec extends SimplePluginSpec implements BeansetPluginSpec {

	private Map<String, String> properties;

	public SimpleBeansetPluginSpec(String name, Map<String, String> properties) {
		super(name);
		Assert.notNull(properties);
		this.properties = Collections.unmodifiableMap(properties);
	}

	public Map<String, String> getOverrides() {
		return properties;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + ((properties == null) ? 0 : properties.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SimpleBeansetPluginSpec other = (SimpleBeansetPluginSpec) obj;
		if (properties == null) {
			if (other.properties != null)
				return false;
		}
		else if (!properties.equals(other.properties))
			return false;
		return true;
	}

}
