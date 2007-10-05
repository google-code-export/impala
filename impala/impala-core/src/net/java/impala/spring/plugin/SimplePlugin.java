package net.java.impala.spring.plugin;

import org.springframework.util.Assert;

public class SimplePlugin implements Plugin {

	private String name;

	public SimplePlugin(String name) {
		super();
		Assert.notNull(name);
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
