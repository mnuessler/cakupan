package com.cakupan.xslt.data;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


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

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        CoverageTemplate rhs = (CoverageTemplate) obj;
        return new EqualsBuilder().append(name, rhs.name)
                .append(lineStart, rhs.lineStart).append(lineEnd, rhs.lineEnd)
                .isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(name).append(lineStart).append(lineEnd)
                .toHashCode();
    }

}
