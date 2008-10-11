package org.impalaframework.web.bootstrap;

import javax.servlet.ServletContext;

/**
 * Subclass ExternalBootstrapLocationResolutionStrategy which relies on Impala itself being configured using
 * names with the convention of META-INF/impala-xxx.xml.
 * @author Phil Zoio
 */
public class AbridgedExternalBootstrapLocationResolutionStrategy extends ExternalBootstrapLocationResolutionStrategy {

	@Override
	public String[] getBootstrapContextLocations(ServletContext servletContext) {
		String[] abridgedNames = super.getBootstrapContextLocations(servletContext);
		return getFullNames(abridgedNames);
	}

	String[] getFullNames(String[] abridgedNames) {
		String[] fullNames = new String[abridgedNames.length];
		
		for (int i = 0; i < abridgedNames.length; i++) {
			if (!abridgedNames[i].endsWith(".xml"))
				fullNames[i] = "META-INF/impala-" + abridgedNames[i] + ".xml";
			else
				fullNames[i] = abridgedNames[i];
		}
		
		return fullNames;
	}

}
