package de.felixperko.worldgenconfig.GUI.PropertyGUI;

public class InvalidPropertyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5890392191896152070L;
	
	public InvalidPropertyException(FailureReason reason) {
		this.reason = reason;
	}
	
	public FailureReason reason;
}
