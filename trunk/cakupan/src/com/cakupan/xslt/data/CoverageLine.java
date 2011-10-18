package com.cakupan.xslt.data;

/**
 * The <code>CoverageLine</code> keeps track of the total hits per instrumented
 * instruction(line) per XSLT.
 * 
 * @author Patrick Oosterveld
 */
public class CoverageLine implements Comparable<CoverageLine>
{

    private int lineNumber;

    private int lineCount;

    private String function;

    /*
     * (non-Javadoc)
     * @see function.
     */
    public String getFunction()
    {
        return function;
    }

    /*
     * (non-Javadoc)
     * @see function
     */
    public void setFunction(String function)
    {
        this.function = function;
    }

    /**
     * @param lineNumber
     * @param count
     */
    public CoverageLine(int lineNumber, int count)
    {
        this.lineNumber = lineNumber;
        lineCount = count;
    }

    /**
     * @param lineNumber
     */
    public CoverageLine(int lineNumber)
    {
        this.lineNumber = lineNumber;
    }

    /*
     * (non-Javadoc)
     * @see lineNumber.
     */
    public int getLineNumber()
    {
        return lineNumber;
    }

    /*
     * (non-Javadoc)
     * @see lineNumber
     */
    public void setLineNumber(int lineNumber)
    {
        this.lineNumber = lineNumber;
    }

    /*
     * (non-Javadoc)
     * @see lineCount.
     */
    public int getLineCount()
    {
        return lineCount;
    }

    /*
     * (non-Javadoc)
     * @see lineCount
     */
    public void setLineCount(int lineCount)
    {
        this.lineCount = lineCount;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof CoverageLine)
        {
            return lineNumber == ((CoverageLine)obj).lineNumber;
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        return lineNumber;
    }

    /** {@inheritDoc} */
    public int compareTo(CoverageLine o)
    {
        return lineNumber - o.lineNumber;
    }
}
