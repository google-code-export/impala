package org.impalaframework.resolver;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class SimpleModuleLocationResolver extends SimpleBaseModuleLocationResolver {

	private String moduleClassDirectory;
	private String moduleTestDirectory;
	
	public void init() {
		super.init();
		Assert.notNull(moduleClassDirectory, "moduleClassDirectory cannot be null");
		Assert.notNull(moduleTestDirectory, "moduleTestDirectory cannot be null");
	}

	public List<Resource> getApplicationModuleClassLocations(String moduleName) {
		return getResources(moduleName, moduleClassDirectory);
	}
	
	public List<Resource> getModuleTestClassLocations(String moduleName) {
		return getResources(moduleName, moduleTestDirectory);
	}

	public void setModuleTestDirectory(String moduleTestDirectory) {
		this.moduleTestDirectory = moduleTestDirectory;
	}

	public void setModuleClassDirectory(String moduleClassDirectory) {
		this.moduleClassDirectory = moduleClassDirectory;
	}

}
