package org.impalaframework.osgi.util;

import org.impalaframework.facade.InternalOperationsFacade;
import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.facade.OperationsFacade;
import org.impalaframework.util.ObjectUtils;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public abstract class ImpalaOsgiUtils {

	/**
	 * Returns the Impala {@link ModuleManagementFacade} from the OSGi service registry.
	 * Will be null if Impala has not been initialised, typically via an Impala {@link BundleActivator} instance.
	 */
	public static ModuleManagementFacade getManagementFacade(BundleContext context) {
		//FIXME add test
		InternalOperationsFacade facade = ImpalaOsgiUtils.getOperationsFacade(context);
		return facade.getModuleManagementFacade();
	}

	public static InternalOperationsFacade getOperationsFacade(BundleContext context) {
		//FIXME add test
		ServiceReference serviceReference = context.getServiceReference(OperationsFacade.class.getName());
		InternalOperationsFacade facade = ObjectUtils.cast(context.getService(serviceReference), InternalOperationsFacade.class);
		return facade;
	}

}
