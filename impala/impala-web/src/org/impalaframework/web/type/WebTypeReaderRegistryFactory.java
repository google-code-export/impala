package org.impalaframework.web.type;

import java.util.Map;

import org.impalaframework.module.type.TypeReader;
import org.impalaframework.module.type.TypeReaderRegistryFactory;
import org.impalaframework.web.module.WebModuleTypes;

public class WebTypeReaderRegistryFactory {
	public static Map<String, TypeReader> getTypeReaders() {
		Map<String, TypeReader> typeReaders = TypeReaderRegistryFactory.getTypeReaders();
		typeReaders.put(WebModuleTypes.WEB_ROOT.toLowerCase(), new WebRootModuleTypeReader());
		typeReaders.put(WebModuleTypes.SERVLET.toLowerCase(), new ServletModuleTypeReader());
		typeReaders.put(WebModuleTypes.WEB_PLACEHOLDER.toLowerCase(), new WebPlaceholderTypeReader());
		return typeReaders;
	}
}
