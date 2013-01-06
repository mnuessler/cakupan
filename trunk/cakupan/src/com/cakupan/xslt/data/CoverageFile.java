package com.cakupan.xslt.data;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * The <code>CoverageFile</code> stores specific XSLT data, such as filename,
 * URI, coveragepercentage and instrumented lines, see
 * {@link com.cakupan.xslt.data.CoverageLine CoverageLine}.
 * 
 * @author Patrick Oosterveld
 */
public class CoverageFile
{

    private URI uri = null;

    private String key = null;

    private Collection<CoverageLine> line = new TreeSet<CoverageLine>();

    private List<CoverageTemplate> templates = new ArrayList<CoverageTemplate>();

    @SuppressWarnings("unused")
    private double coveragePercentage;

    /**
     * @param key
     * @param uri
     * @param coverageLine
     */
    public CoverageFile(String key, URI uri, CoverageLine coverageLine)
    {
        addLine(coverageLine);
        this.uri = uri;
        this.key = key;
    }

    /*
     * (non-Javadoc)
     * @see coveragePercentage.
     */
    public double getCoveragePercentage()
    {
        double perc = 100.0d;
        double countHit = 0;
        double totalLines = line.size();
        if (totalLines > 0)
        {
            for (Object coverageLine : line.toArray())
            {
                if (((CoverageLine)coverageLine).getLineCount() > 0)
                {
                    countHit++;
                }
            }
            perc = countHit / totalLines * 100.0;
        }
        return perc;
    }

    /*
     * (non-Javadoc)
     * @see coveragePercentage
     */
    public void setCoveragePercentage(double coveragePercentage)
    {
        this.coveragePercentage = coveragePercentage;
    }

    /*
     * (non-Javadoc)
     * @see uri.
     */
    public URI getUri()
    {
        return uri;
    }

    /*
     * (non-Javadoc)
     * @see uri
     */
    public void setUri(URI uri)
    {
        this.uri = uri;
    }

    /*
     * (non-Javadoc)
     * @see fileName.
     */
    public String getFileName()
    {
        return key;
    }

    /*
     * (non-Javadoc)
     * @see fileName
     */
    public void setFileName(String fileName)
    {
        key = fileName;
    }

    /*
     * (non-Javadoc)
     * @see line.
     */
    public Collection<CoverageLine> getLine()
    {
        return line;
    }

    /*
     * (non-Javadoc)
     * @see line.
     */
    public CoverageLine getLine(int lineNumber)
    {
        for (CoverageLine coverageLine : line)
        {
            if (coverageLine.getLineNumber() == lineNumber)
            {
                return coverageLine;
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see line
     */
    public void setLine(Collection<CoverageLine> line)
    {
        this.line = line;
    }

    /*
     * (non-Javadoc)
     * @see line
     */
    public void addLine(CoverageLine line)
    {
        this.line.add(line);
    }

    /*
     * (non-Javadoc)
     * @see line
     */
    public void hitLine(int lineNumber)
    {
        System.out.println("CoverageFile: hitLine " + lineNumber);
        CoverageLine coverageLine = getLine(lineNumber);
        if (coverageLine != null)
        {
            coverageLine.setLineCount(coverageLine.getLineCount() + 1);
        }
        else
        {
            line.add(new CoverageLine(lineNumber, 1));
        }
    }

    public List<CoverageTemplate> getTemplates()
    {
        return templates;
    }

    public void setTemplates(List<CoverageTemplate> templates)
    {
        this.templates = templates;
    }

    public void startTemplate(String name, int lineNumber)
    {
        templates.add(new CoverageTemplate(name, lineNumber));
    }

    public void endTemplate(int lineNumber)
    {
        templates.get(templates.size() - 1).setLineEnd(lineNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        CoverageFile rhs = (CoverageFile) obj;
        return new EqualsBuilder().append(uri, rhs.uri).append(key, rhs.key)
                .append(line, rhs.line).append(templates, rhs.templates)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(uri).append(key).append(line)
                .append(templates).toHashCode();
    }

}
