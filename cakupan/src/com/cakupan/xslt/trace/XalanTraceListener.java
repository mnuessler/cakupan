package com.cakupan.xslt.trace;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.transform.TransformerException;

import net.sf.saxon.expr.ExpressionLocation;

import org.apache.xalan.trace.EndSelectionEvent;
import org.apache.xalan.trace.ExtensionEvent;
import org.apache.xalan.trace.GenerateEvent;
import org.apache.xalan.trace.SelectionEvent;
import org.apache.xalan.trace.TraceListenerEx2;
import org.apache.xalan.trace.TracerEvent;

import com.cakupan.xslt.exception.XSLTCoverageException;
import com.cakupan.xslt.util.XSLTCakupanUtil;

/**
 * This listener reacts on each XSLT event (mostly XSLT instruction).
 * The <code>XalanTraceListener</code> is an extension of the Xalan {@link org.apache.xalan.trace.TraceListenerEx2 TraceListenerEx2}.
 *
 */
public class XalanTraceListener implements TraceListenerEx2 // TraceListenerEx3
{
    private static boolean C0WHITE[] = {
        false, false, false, false, false, false, false, false, false, true, 
        true, false, false, true, false, false, false, false, false, false, 
        false, false, false, false, false, false, false, false, false, false, 
        false, false, true
    };
    private String xsltRootFileName;

    private URI xsltURI;
    
	public XalanTraceListener(URL resource) throws XSLTCoverageException {
        if (null != resource){
            try {
                this.xsltURI = resource.toURI();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            xsltRootFileName = ExpressionLocation.truncateURI(resource.toString());
        }
        XSLTCakupanUtil.loadCoverageStats();
    }
	
	public XalanTraceListener(String resource) throws XSLTCoverageException {
        xsltRootFileName = resource;
        XSLTCakupanUtil.loadCoverageStats();
    }


	public void trace(TracerEvent ev)
	{
		if (null != ev.m_styleNode){
//			System.out.println("trace: "+ev.m_styleNode.getSystemId());
	        String file = ExpressionLocation.truncateURI(ev.m_styleNode.getSystemId());
	        String fileName = escape(file);
	        URI uri = null;
	        if (null == fileName || fileName.length() < 1){
	            fileName = xsltRootFileName;
	            uri = xsltURI;
	        }else{
	            try {
	                uri = new URI(ev.m_styleNode.getSystemId());
	            } catch (URISyntaxException e) {
	                 e.printStackTrace();
	            }
	        }
	        XSLTCakupanUtil.setHit(fileName, uri, ev.m_styleNode.getLineNumber());
		}else{
			System.out.println("Nothing found, no styleNode");
		}
	}

	public void traceEnd(TracerEvent ev)
	{
	}
	public void selected(SelectionEvent ev) throws TransformerException
	{
	}

	public void selectEnd(EndSelectionEvent ev) throws TransformerException
	{
	       try {
				XSLTCakupanUtil.dumpCoverageStats();
			} catch (XSLTCoverageException e) {
				throw new RuntimeException(e);
			}

	}

	public void generated(GenerateEvent ev)
	{
	}

	public void extension(ExtensionEvent ee)
	{
	}

	public void extensionEnd(ExtensionEvent ee)
	{
	}

	
	/*TODO move the following methodes to a utility class*/
    public String escape(String in)
    {
        if(in == null)
            return "";
        CharSequence collapsed = collapseWhitespace(in);
        StringBuffer sb = new StringBuffer(collapsed.length() + 10);
        for(int i = 0; i < collapsed.length(); i++)
        {
            char c = collapsed.charAt(i);
            if(c == '<')
            {
                sb.append("&lt;");
                continue;
            }
            if(c == '>')
            {
                sb.append("&gt;");
                continue;
            }
            if(c == '&')
            {
                sb.append("&amp;");
                continue;
            }
            if(c == '"')
            {
                sb.append("&#34;");
                continue;
            }
            if(c == '\n')
            {
                sb.append("&#xA;");
                continue;
            }
            if(c == '\r')
            {
                sb.append("&#xD;");
                continue;
            }
            if(c == '\t')
                sb.append("&#x9;");
            else
                sb.append(c);
        }

        return sb.toString();
    }
    public static CharSequence collapseWhitespace(CharSequence in)
    {
        int len = in.length();
        if(len == 0 || !containsWhitespace(in))
            return in;
        StringBuffer sb = new StringBuffer(len);
        boolean inWhitespace = true;
        for(int i = 0; i < len; i++)
        {
            char c = in.charAt(i);
            switch(c)
            {
            case 9: // '\t'
            case 10: // '\n'
            case 13: // '\r'
            case 32: // ' '
                if(!inWhitespace)
                {
                    sb.append(' ');
                    inWhitespace = true;
                }
                break;

            default:
                sb.append(c);
                inWhitespace = false;
                break;
            }
        }

        int nlen = sb.length();
        if(nlen > 0 && sb.charAt(nlen - 1) == ' ')
            sb.setLength(nlen - 1);
        return sb;
    }
    public static boolean containsWhitespace(CharSequence value)
    {
        int len = value.length();
        for(int i = 0; i < len;)
        {
            char c = value.charAt(i++);
            if(c <= ' ' && C0WHITE[c])
                return true;
        }

        return false;
    }
}
