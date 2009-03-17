package com.cakupan.xslt.exception;


@SuppressWarnings("serial")
/**
 * @author Patrick Oosterveld
 */
public class XSLTCoverageException extends Exception{
	
	public static final int NO_COVERAGE_FILE = 0; 
	
	private int refId = -1;
	
	/**
     * Constructor
	 * @param message message
	 * @param cause cause
     */
	public XSLTCoverageException(int refId, String message, Throwable cause) {
		super(message, cause);
		this.refId = refId;
	}
	
	/**
	 * @return the refId
	 */
	public int getRefId() {
		return refId;
	}

	/**
	 * @param refId the refId to set
	 */
	public void setRefId(int refId) {
		this.refId = refId;
	}

	/**
     * Constructor
	 * @param message message
	 * @param cause cause
     */
	public XSLTCoverageException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
     * Constructor
	 * @param message message
     */
	public XSLTCoverageException(String message) {
		super(message);
	}

	public XSLTCoverageException(int refId, String message) {
		super(message);
		this.refId = refId;
	}
}
