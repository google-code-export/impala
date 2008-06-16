package org.impalaframework.module.type;

import java.util.HashMap;
import java.util.Map;

import org.impalaframework.module.definition.ModuleTypes;

public class TypeReaderRegistryFactory {
	public static Map<String, TypeReader> readTypeReaders() {
		Map<String, TypeReader> typeReaders = new HashMap<String, TypeReader>();
		typeReaders.put(ModuleTypes.ROOT.toLowerCase(), new RootModuleTypeReader());
		typeReaders.put(ModuleTypes.APPLICATION.toLowerCase(), new ApplicationModuleTypeReader());
		typeReaders.put(ModuleTypes.APPLICATION_WITH_BEANSETS.toLowerCase(), new ApplicationWithBeansetsModuleTypeReader());
		return typeReaders;
	}
}
