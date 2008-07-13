package org.impalaframework.web.loader;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;

public class ExternalContextLoaderListener extends ContextLoaderListener {

	@Override
	protected ContextLoader createContextLoader() {
		return new ExternalContextLoader();
	}

}
