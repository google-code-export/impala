/*
 * Copyright 2007 the original author or authors.
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

package net.java.impala.spring.web;

import java.io.File;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.java.impala.monitor.Reloadable;
import net.java.impala.spring.monitor.ContextReloader;

import org.springframework.beans.BeansException;
import org.springframework.context.ConfigurableApplicationContext;

public class ReloadingImpalaServlet extends ImpalaServlet implements Reloadable {

	private static final long serialVersionUID = 1L;

	private ContextReloader contextReloader;

	protected void initFrameworkServlet() throws BeansException, ServletException {
		super.initFrameworkServlet();

		if (this.contextReloader == null) {
			this.contextReloader = new ContextReloader();
			this.contextReloader.start(this);
		}
	}

	@Override
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
		synchronized (contextReloader) {
			super.doService(request, response);
		}
	}

	/* ******************* Reloadable ******************* */

	public File[] getResourcesToMonitor() {
		return getContextDirectories();
	}

	public void reload() {
		synchronized (contextReloader) {
			System.out.println("Reloading web context for servlet " + getServletName());
			closeContext();
			try {
				initServletBean();
				// FIXME decide what to do with exception
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void destroy() {
		getServletContext().log("Closing WebApplicationContext of Spring FrameworkServlet '" + getServletName() + "'");
		closeContext();
	}

	private void closeContext() {
		if (this.getWebApplicationContext() instanceof ConfigurableApplicationContext) {
			((ConfigurableApplicationContext) this.getWebApplicationContext()).close();
		}
	}

}
