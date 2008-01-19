package org.impalaframework.exception;

public class RuntimeException extends java.lang.RuntimeException {

	private static final long serialVersionUID = 1L;

	public RuntimeException() {
	}

	public RuntimeException(String message) {
		super(message);
	}

	public RuntimeException(Throwable cause) {
		super(cause);
	}

	public RuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

}
