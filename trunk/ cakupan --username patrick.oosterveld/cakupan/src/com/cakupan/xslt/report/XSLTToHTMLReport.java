package com.cakupan.xslt.report;

import java.io.*;


import org.apache.commons.lang.*;

import com.cakupan.xslt.data.*;
import com.cakupan.xslt.exception.XSLTCoverageException;

/**
 * The <code>XSLTToHTMLReport<code> represents the HTML content of an XSLT file with covarage data.
 */
public class XSLTToHTMLReport {
	
	/**
	 * the XSLT content as HTML
	 */
	private StringBuilder builder = new StringBuilder();

    
	public XSLTToHTMLReport() {
		super();
	}
    
	
	/**
	 * build the HTML representation of the XSLT
	 * @param coverageFile
	 * @param xsltFile
	 * @return
	 * @throws XSLTCoverageException
	 */
	public String build(CoverageFile coverageFile, String xsltFile) throws XSLTCoverageException {
		setHeader(coverageFile);
		setCoveragedHtml(xsltFile, coverageFile);
		setFooter();
		return builder.toString();
	}

	/**
	 * @return the HTML representation of the XSLT
	 */
	public String getXSLTReport() {
		return builder.toString();
	}

	
	/**
     * Writes the header of an XSLT report 
     * @param file 
     */
    private void setHeader(CoverageFile file) {
        builder.append("<html>\n");
        builder.append("<head>\n");
        builder.append("<title>Coverage Report ["+file.getFileName()+"]</title>\n");
        builder.append("<link title=\"Style\" type=\"text/css\" rel=\"stylesheet\" href=\"main.css\"/>\n");
        builder.append("</head>\n");
        builder.append("<body>\n");
        builder.append("<h5>Coverage Report - "+file.getFileName()+" &nbsp;<font color=\"blue\">("+String.format("%.0f", file.getCoveragePercentage())+"%)</font>\n");
        builder.append("<div class=\"separator\">&nbsp;</div>\n");
        builder.append("<table cellspacing=\"0\" cellpadding=\"0\" class=\"src\">\n");
    }

    /**
     * Writes the Footer of an XSLT report
     */
    private void setFooter() {
        builder.append("</table>\n");
        builder.append("</body>\n");
        builder.append("</html>\n");
    }
    
    /**
     * @param xml
     * @param file 
     * @throws XSLTCoverageException 
     */
    private void setCoveragedHtml(String xml, CoverageFile file) throws XSLTCoverageException {
        BufferedReader reader = new BufferedReader(new StringReader(xml));
        String str = null;
        StringBuilder output = new StringBuilder();
        int rowCount = 1;
        CoverageLine line = null;
        try {
            while ((str = reader.readLine()) != null) {
                line = file.getLine(rowCount);
                if (line == null){
                    //not instrumented
                    output.append("<tr>  <td class=\"numLine\">");
                    output.append(" "+rowCount+"</td>");
                    output.append("<td class=\"nbHits\">");
                    output.append("&nbsp;");
                    output.append("</td>\n");
                    output.append("<td class=\"src\"><pre class=\"src\">&nbsp");
                    output.append(StringEscapeUtils.escapeHtml(str));
                    output.append("</pre></td></tr>\n");
                }else if (line.getLineCount() == 0){
                    //we got no hit
                    output.append("<tr>  <td class=\"numLineCover\">");
                    output.append("&nbsp;"+rowCount+"</td>");
                    output.append("<td class=\"nbHitsUncovered\">");
                    output.append("&nbsp;"+line.getLineCount());
                    output.append("</td>\n");
                    output.append("<td class=\"src\"><pre class=\"src\"><span class=\"srcUncovered\">&nbsp");
                    output.append(StringEscapeUtils.escapeHtml(str));
                    output.append("</pre></td></tr>\n");
                }else{
                    //we got a hit
                    output.append("<tr>  <td class=\"numLineCover\">");
                    output.append("&nbsp;"+rowCount+"</td>");
                    output.append("<td class=\"nbHitsCovered\">");
                    output.append("&nbsp;"+line.getLineCount());
                    output.append("</td>\n");
                    output.append("<td class=\"src\"><pre class=\"src\"><span class=\"src\">&nbsp");
                    output.append(StringEscapeUtils.escapeHtml(str));
                    output.append("</pre></td></tr>\n");
                }
                rowCount++;
            }
        } catch (Exception e) {
           throw new XSLTCoverageException("XSLT document: HTML report couldnot be written!", e);
        }
        builder.append(output);
    }
 
}
