package org.impalaframework.facade;

import java.util.ArrayList;
import java.util.List;

public class JarOperationsFacade extends BaseOperationsFacade {

	public JarOperationsFacade() {
		super();
	}

	@Override
	protected List<String> getBootstrapContextLocations() {
		List<String> locations = new ArrayList<String>();
		locations.add("META-INF/impala-bootstrap.xml");
		locations.add("META-INF/impala-jar-bootstrap.xml");
		return locations;
	}

}
