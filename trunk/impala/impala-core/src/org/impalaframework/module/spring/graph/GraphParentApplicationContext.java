/*
 * Copyright 2007-2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.module.spring.graph;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.io.Resource;
import org.springframework.util.ObjectUtils;

public class GraphParentApplicationContext implements ApplicationContext {
	
	private final ApplicationContext parent;
	private final long startupDate;
	private String id = ObjectUtils.identityToString(this);
	private String displayName;

	public GraphParentApplicationContext(ApplicationContext parent) {
		super();
		this.parent = parent;
		this.startupDate = System.currentTimeMillis();
	}

	/* ***************** Methods currently not supported, but for which support may be added ***************** */	
	
	public String getDisplayName() {
		return displayName;
	}

	public String getId() {
		return id;
	}

	public ApplicationContext getParent() {
		return parent;
	}

	public long getStartupDate() {
		return startupDate;
	}

	public boolean containsBeanDefinition(String beanName) {
		return parent.containsBeanDefinition(beanName);
	}

	public int getBeanDefinitionCount() {
		return parent.getBeanDefinitionCount();
	}

	public String[] getBeanDefinitionNames() {
		return parent.getBeanDefinitionNames();
	}

	@SuppressWarnings("unchecked")
	public String[] getBeanNamesForType(Class type) {
		return parent.getBeanNamesForType(type);
	}

	@SuppressWarnings("unchecked")
	public String[] getBeanNamesForType(Class type,
			boolean includeNonSingletons, boolean allowEagerInit) {
		return parent.getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
	}

	@SuppressWarnings("unchecked")
	public Map getBeansOfType(Class type) throws BeansException {
		return parent.getBeansOfType(type);
	}

	@SuppressWarnings("unchecked")
	public Map getBeansOfType(Class type, boolean includeNonSingletons,
			boolean allowEagerInit) throws BeansException {
		return parent.getBeansOfType(type);
	}

	public boolean containsBean(String name) {
		return parent.containsBean(name);
	}

	public String[] getAliases(String name) {
		return parent.getAliases(name);
	}

	public Object getBean(String name) throws BeansException {
		return parent.getBean(name);
	}

	@SuppressWarnings("unchecked")
	public Object getBean(String name, Class requiredType)
			throws BeansException {
		return parent.getBean(name, requiredType);
	}

	public Object getBean(String name, Object[] args) throws BeansException {
		return parent.getBean(name, args);
	}

	@SuppressWarnings("unchecked")
	public Class getType(String name) throws NoSuchBeanDefinitionException {
		return parent.getType(name);
	}

	public boolean isPrototype(String name)
			throws NoSuchBeanDefinitionException {
		return parent.isPrototype(name);
	}

	public boolean isSingleton(String name)
			throws NoSuchBeanDefinitionException {
		return parent.isSingleton(name);
	}

	@SuppressWarnings("unchecked")
	public boolean isTypeMatch(String name, Class targetType)
			throws NoSuchBeanDefinitionException {
		return parent.isTypeMatch(name, targetType);
	}

	public boolean containsLocalBean(String name) {
		return parent.containsLocalBean(name);
	}

	public BeanFactory getParentBeanFactory() {
		return parent.getParentBeanFactory();
	}

	public String getMessage(MessageSourceResolvable resolvable, Locale locale)	throws NoSuchMessageException {
		return parent.getMessage(resolvable, locale);
	}

	public String getMessage(String code, Object[] args, Locale locale)	throws NoSuchMessageException {
		return parent.getMessage(code, args, locale);
	}

	public String getMessage(String code, Object[] args, String defaultMessage,	Locale locale) {
		return parent.getMessage(code, args, defaultMessage, locale);
	}

	public void publishEvent(ApplicationEvent event) {
		parent.publishEvent(event);
	}

	public Resource[] getResources(String locationPattern) throws IOException {
		return parent.getResources(locationPattern);
	}

	public Resource getResource(String location) {
		return parent.getResource(location);
	}	

	public ClassLoader getClassLoader() {
		return parent.getClassLoader();
	}
	
	public AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException {
		return parent.getAutowireCapableBeanFactory();
	}
	
	/* ********************* wired in setters ********************* */

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
