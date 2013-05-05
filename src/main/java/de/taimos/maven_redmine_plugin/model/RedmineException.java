package de.taimos.maven_redmine_plugin.model;

public class RedmineException extends RuntimeException {
	
	private static final long serialVersionUID = -3369633874599913628L;
	
	
	/**
	 * @param message
	 * @param cause
	 */
	public RedmineException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * @param message
	 */
	public RedmineException(String message) {
		super(message);
	}
	
}
