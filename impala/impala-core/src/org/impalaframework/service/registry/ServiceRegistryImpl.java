package org.impalaframework.service.registry;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.service.registry.event.ServiceAddedEvent;
import org.impalaframework.service.registry.event.ServiceRegistryEvent;
import org.impalaframework.service.registry.event.ServiceRegistryEventListener;
import org.impalaframework.service.registry.event.ServiceRemovedEvent;
import org.springframework.util.Assert;

public class ServiceRegistryImpl implements ServiceRegistry {

	private static Log logger = LogFactory.getLog(ServiceRegistryImpl.class);

	private Map<String, ServiceReference> services = new ConcurrentHashMap<String, ServiceReference>();
	private Map<Object, String> entities = new IdentityHashMap<Object, String>();

	// FIXME check that this is valid
	private List<ServiceRegistryEventListener> listeners = new CopyOnWriteArrayList<ServiceRegistryEventListener>();
	private Map<String, List<ServiceRegistryEventListener>> taggedListeners = new ConcurrentHashMap<String, List<ServiceRegistryEventListener>>();

	private Object registryLock = new Object();
	private Object listenersLock = new Object();
	private Object taggedListenersLock = new Object();

	public void addEventListener(String tagName,
			ServiceRegistryEventListener listener) {
		Assert.notNull(tagName);
		Assert.notNull(listener);

		List<ServiceRegistryEventListener> list = null;
		synchronized (taggedListenersLock) {
			list = taggedListeners.get(tagName);
			if (list == null) {
				list = new CopyOnWriteArrayList<ServiceRegistryEventListener>();
				taggedListeners.put(tagName, list);
			}
			synchronized (list) {
				list.add(listener);
				if (logger.isDebugEnabled())
					logger.debug("Added service registry listener " + listener
							+ " for tag '" + tagName + "'");
			}
		}
	}

	/**
	 * Adds to global event listeners to which all service registry events will
	 * be broadcast
	 */
	public void addEventListener(ServiceRegistryEventListener listener) {
		Assert.notNull(listener);
		synchronized (listenersLock) {
			listeners.add(listener);
		}
		if (logger.isDebugEnabled())
			logger.debug("Added service registry listener " + listener);
	}

	public void removeListener(String tagName,
			ServiceRegistryEventListener listener) {
		if (tagName != null) {
			removeEventListener(tagName, listener);
		} else {
			synchronized (listenersLock) {
				removeEventListener(listener);
			}
		}
	}

	public void removeEventListener(String tagName,
			ServiceRegistryEventListener listener) {
		List<ServiceRegistryEventListener> list = null;
		synchronized (taggedListenersLock) {
			list = taggedListeners.get(tagName);
			if (list != null) {
				synchronized (list) {
					list.remove(listener);
				}
			}
		}
	}

	/**
	 * Removes global event listeners to which all service registry events will
	 * be broadcast
	 */

	public void removeEventListener(ServiceRegistryEventListener listener) {
		List<ServiceRegistryEventListener> listeners = this.listeners;
		removeListener(listener, listeners);
	}

	public void addService(String beanName, String moduleName, Object service) {
		addService(beanName, moduleName, service, null, null);
	}

	public void addService(String beanName, String moduleName, Object service,
			List<String> tags) {
		addService(beanName, moduleName, service, tags, null);
	}

	public void addService(String beanName, String moduleName, Object service,
			Map<String, ?> attributes) {
		addService(beanName, moduleName, service, null, attributes);
	}

	public void addService(String beanName, String moduleName, Object service,
			List<String> tags, Map<String, ?> attributes) {
		ServiceReference serviceReference = null;
		synchronized (registryLock) {
			serviceReference = new ServiceReference(service, beanName,
					moduleName, tags, attributes);
			services.put(beanName, serviceReference);
			entities.put(service, beanName);
		}
		if (logger.isDebugEnabled())
			logger.debug("Added service bean '" + beanName
					+ "' contributed from module '" + moduleName
					+ "' to service registry, with tags " + tags
					+ " and attributes " + attributes);

		// FIXME is any additional locking here necessary
		ServiceAddedEvent event = new ServiceAddedEvent(serviceReference);
		invokeListeners(event);
	}

	public void remove(Object service) {
		ServiceReference serviceReference = null;
		String beanName = null;
		synchronized (registryLock) {
			beanName = entities.remove(service);
			if (beanName != null) {
				serviceReference = services.remove(beanName);
			}
		}

		if (serviceReference != null) {
			if (logger.isDebugEnabled())
				logger.debug("Removed from service reference bean '" + beanName
						+ "' contributed from module '"
						+ serviceReference.getContributingModule() + "'");
			// FIXME is any additional locking here necessary
			ServiceRemovedEvent event = new ServiceRemovedEvent(
					serviceReference);
			invokeListeners(event);
		}
	}

	public ServiceReference getService(String beanName) {
		return services.get(beanName);
	}

	public ServiceReference getService(String beanName, Class<?> type) {
		ServiceReference serviceReference = services.get(beanName);

		if (serviceReference != null) {
			Object bean = serviceReference.getBean();
			if (!type.isAssignableFrom(bean.getClass())) {
				throw new InvalidStateException("Service reference bean "
						+ bean + " is not assignable from type " + type);
			}
		}
		return serviceReference;
	}

	/* ************ helper methods * ************** */

	private List<ServiceRegistryEventListener> getListeners() {
		return listeners;
	}

	private void invokeListeners(ServiceRegistryEvent event) {
		List<ServiceRegistryEventListener> listeners = getListeners();
		for (ServiceRegistryEventListener listener : listeners) {
			listener.handleServiceRegistryEvent(event);
		}
		ServiceReference serviceReference = event.getServiceReference();
		List<String> tags = serviceReference.getTags();

		for (String tag : tags) {
			List<ServiceRegistryEventListener> list = null;
			synchronized (taggedListenersLock) {
				list = taggedListeners.get(tag);
				if (list != null) {
					synchronized (list) {
						for (ServiceRegistryEventListener listener : list) {
							listener.handleServiceRegistryEvent(event);
						}
					}
				}
			}
		}
	}

	private void removeListener(ServiceRegistryEventListener listener,
			List<ServiceRegistryEventListener> listeners) {
		for (Iterator<ServiceRegistryEventListener> iterator = listeners
				.iterator(); iterator.hasNext();) {
			ServiceRegistryEventListener currentListener = iterator.next();
			if (currentListener == listener) {
				iterator.remove();
				if (logger.isDebugEnabled())
					logger.debug("Removed service registry listener "
							+ listener);
			}
		}
	}

}
