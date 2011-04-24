package com.cakupan.xslt.data;


/**
 * The <code>CoverageTemplate</code> stores the start and end lines of an XSLT template
 *
 * @author Andrew Martignoni III 
 */

public class CoverageTemplate
{
    private String name;

    private Integer lineStart;

    private Integer lineEnd;

    public CoverageTemplate(String templateName, int lineNumber)
    {
        name = templateName;
        lineStart=lineNumber;
    }
    
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getLineStart()
    {
        return lineStart;
    }

    public void setLineStart(Integer lineStart)
    {
        this.lineStart = lineStart;
    }

    public Integer getLineEnd()
    {
        return lineEnd;
    }

    public void setLineEnd(Integer lineEnd)
    {
        this.lineEnd = lineEnd;
    }
}
