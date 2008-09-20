package org.impalaframework.web.servlet.wrapper;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.util.Assert;

public class DelegatingWrapperHttpSession implements HttpSession {

	private HttpSession realSession;

	public DelegatingWrapperHttpSession(HttpSession realSession) {
		super();
		Assert.notNull(realSession);
		this.realSession = realSession;
	}

	public Object getAttribute(String name) {
		return realSession.getAttribute(name);
	}

	@SuppressWarnings("unchecked")
	public Enumeration getAttributeNames() {
		return realSession.getAttributeNames();
	}

	public long getCreationTime() {
		return realSession.getCreationTime();
	}

	public String getId() {
		return realSession.getId();
	}

	public long getLastAccessedTime() {
		return realSession.getLastAccessedTime();
	}

	public int getMaxInactiveInterval() {
		return realSession.getMaxInactiveInterval();
	}

	public ServletContext getServletContext() {
		return realSession.getServletContext();
	}

	@SuppressWarnings("deprecation")
	public javax.servlet.http.HttpSessionContext getSessionContext() {
		return realSession.getSessionContext();
	}

	@SuppressWarnings("deprecation")
	public Object getValue(String name) {
		return realSession.getValue(name);
	}

	@SuppressWarnings("deprecation")
	public String[] getValueNames() {
		return realSession.getValueNames();
	}

	public void invalidate() {
		realSession.invalidate();
	}

	public boolean isNew() {
		return realSession.isNew();
	}

	@SuppressWarnings("deprecation")
	public void putValue(String name, Object value) {
		realSession.putValue(name, value);
	}

	public void removeAttribute(String name) {
		realSession.removeAttribute(name);
	}

	@SuppressWarnings("deprecation")
	public void removeValue(String name) {
		realSession.removeValue(name);
	}

	public void setAttribute(String name, Object value) {
		realSession.setAttribute(name, value);
	}

	public void setMaxInactiveInterval(int interval) {
		realSession.setMaxInactiveInterval(interval);
	}

	protected HttpSession getRealSession() {
		return realSession;
	}

}
