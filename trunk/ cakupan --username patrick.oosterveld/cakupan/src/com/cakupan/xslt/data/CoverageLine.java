package com.cakupan.xslt.data;

/**
 * The <code>CoverageLine</code> keeps track of the total hits per instrumented instruction(line) per XSLT.
 *
 *	@author Patrick Oosterveld
 * 
 */
public class CoverageLine {
    
    private Integer lineNumber;
    
    private Integer lineCount;
    
    private String function;

    /* (non-Javadoc)
     * @see function.
     */
    public String getFunction() {
        return function;
    }

    /* (non-Javadoc)
     * @see function
     */
    public void setFunction(String function) {
        this.function = function;
    }

    /**
     * @param lineNumber
     * @param count
     */
    public CoverageLine(Integer lineNumber, Integer count) {
        this.lineNumber = lineNumber;
        this.lineCount = count;
    }

    /**
     * @param lineNumber
     */
    public CoverageLine(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    /* (non-Javadoc)
     * @see lineNumber.
     */
    public Integer getLineNumber() {
        return lineNumber;
    }

    /* (non-Javadoc)
     * @see lineNumber
     */
    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    /* (non-Javadoc)
     * @see lineCount.
     */
    public Integer getLineCount() {
        return lineCount;
    }

    /* (non-Javadoc)
     * @see lineCount
     */
    public void setLineCount(Integer lineCount) {
        this.lineCount = lineCount;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Integer){
            return lineNumber.equals(obj);   
        }
        return super.equals(obj);
    }

    public boolean compare(Object obj){
        if (obj instanceof Integer){
            return lineNumber.equals(obj);   
        }
        return false;
    }

}
