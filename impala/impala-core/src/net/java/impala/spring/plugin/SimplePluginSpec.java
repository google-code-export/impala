package net.java.impala.spring.plugin;

import org.springframework.util.Assert;

public class SimplePluginSpec implements PluginSpec {

	private String name;

	private ChildSpecContainer childContainer;
	
	public SimplePluginSpec(String name) {
		super();
		Assert.notNull(name);
		this.name = name;
		this.childContainer = new ChildSpecContainerImpl(new PluginSpec[]{});
	}

	public String getName() {
		return name;
	}
	
	public String[] getPluginNames() {
		return childContainer.getPluginNames();
	}
	
	public PluginSpec getPlugin(String pluginName) {
		return childContainer.getPlugin(pluginName);
	}
	
	public PluginSpec[] getPlugins() {
		return childContainer.getPlugins();
	}

	public boolean hasPlugin(String pluginName) {
		return getPlugin(pluginName) != null;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((name == null) ? 0 : name.hashCode());
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
		final SimplePluginSpec other = (SimplePluginSpec) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		return true;
	}

}
