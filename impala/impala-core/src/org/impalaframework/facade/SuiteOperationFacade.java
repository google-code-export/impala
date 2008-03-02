package org.impalaframework.facade;

import java.util.ArrayList;
import java.util.List;

public class SuiteOperationFacade extends BaseOperationsFacade {

	@Override
	protected List<String> getBootstrapContextLocations() {
		List<String> locations = new ArrayList<String>();
		locations.add("META-INF/impala-bootstrap.xml");
		locations.add("META-INF/impala-parent-loader-bootstrap.xml");
		return locations;
	}

}
