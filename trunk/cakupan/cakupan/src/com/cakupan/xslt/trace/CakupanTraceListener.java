package com.cakupan.xslt.trace;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import com.cakupan.xslt.exception.XSLTCoverageException;
import com.cakupan.xslt.util.CoverageIOUtil;
import com.cakupan.xslt.util.XSLTCakupanUtil;

import net.sf.saxon.expr.ExpressionLocation;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.trace.InstructionInfo;
import net.sf.saxon.trace.XSLTTraceListener;

/**
 * This listener reacts on each XSLT event (mostly XSLT instruction).
 * The <code>XSLTCoverageTraceListener</code> is an extension of the SAXON {@link net.sf.saxon.trace.XSLTTraceListener XSLTTraceListener}.
 *
 * The Saxon XSLT and XQuery Processor from Saxonica Limited.
 * http://www.saxonica.com/
 */
public class CakupanTraceListener extends XSLTTraceListener {

    private String xsltRootFileName;

    private URI xsltURI;

    /**
     * @param resource
     * @throws XSLTCoverageException 
     */
    public CakupanTraceListener(URL resource) throws XSLTCoverageException {
        if (null != resource){
            try {
                this.xsltURI = resource.toURI();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            xsltRootFileName = ExpressionLocation.truncateURI(resource.toString());
        }
        setOutputDestination(null);
        XSLTCakupanUtil.loadCoverageStats();
    }

    /**
     * @param resource
     * @throws XSLTCoverageException 
     */
    public CakupanTraceListener(String resource) throws XSLTCoverageException {
        xsltRootFileName = resource;
        setOutputDestination(null);
        XSLTCakupanUtil.loadCoverageStats();
    }

    /* (non-Javadoc)
     * @see net.sf.saxon.trace.AbstractTraceListener#setOutputDestination(java.io.PrintStream)
     */
    @Override
    public void setOutputDestination(PrintStream stream) {
        try {
            super.setOutputDestination(new PrintStream(CoverageIOUtil.getDestDir() + File.separator + xsltRootFileName + ".xml"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (XSLTCoverageException e) {
            throw new RuntimeException(e);
		}
    }
    
    /* (non-Javadoc)
     * @see net.sf.saxon.trace.AbstractTraceListener#enter(net.sf.saxon.trace.InstructionInfo, net.sf.saxon.expr.XPathContext)
     */
    @Override
    public void enter(InstructionInfo info, XPathContext context) {
        String file = ExpressionLocation.truncateURI(info.getSystemId());
        String fileName = escape(file);
        URI uri = null;
        int infotype = info.getConstructType();
        String tag = tag(infotype);
        if (tag==null) {
            // this TraceListener ignores some events to reduce the volume of output
            return;
        }
        if (null == fileName || fileName.length() < 1){
            fileName = xsltRootFileName;
            uri = xsltURI;
        }else{
            try {
                uri = new URI(info.getSystemId());
            } catch (URISyntaxException e) {
                 e.printStackTrace();
            }
        }
        XSLTCakupanUtil.setHit(fileName, uri, info.getLineNumber());
        //use the original code
        super.enter(info, context);
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.saxon.trace.XSLTTraceListener#getOpeningAttributes()
     */
    @Override
    protected String getOpeningAttributes() {
        return " module=\"" + xsltRootFileName + "\"";
    }
    
    /* (non-Javadoc)
     * @see net.sf.saxon.trace.AbstractTraceListener#close()
     */
    @Override
    public void close() {
        try {
			XSLTCakupanUtil.dumpCoverageStats();
		} catch (XSLTCoverageException e) {
			//TODO notify called class that dumping the file was failed!
		}
        super.close();
    }

}
