package org.impalaframework.web.spring.loader;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;

/**
 * Overrides <code>ContextLoaderListener</code> simply by returning <code>ExternalContextLoader</code> as the <code>ContextLoader</code> instance.
 * 
 * @see ExternalContextLoader
 * @author Phil Zoio
 */
public class ExternalContextLoaderListener extends ContextLoaderListener {

    @Override
    protected ContextLoader createContextLoader() {
        return new ExternalContextLoader();
    }

}
