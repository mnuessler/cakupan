package com.cakupan.xslt.transform;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;

import com.cakupan.xslt.exception.XSLTCoverageException;
import com.cakupan.xslt.trace.CakupanTraceListener;

import net.sf.saxon.TransformerFactoryImpl;
import net.sf.saxon.trans.CompilerInfo;

/**
 * The <code>TransformerInstrumentFactoryImpl</code> is an implemententation of SAXON <link>TransformerFactoryImpl<link>
 * which makes it possible to measure coverage statistics of used XSLTs. 
 *
 * The Saxon XSLT and XQuery Processor from Saxonica Limited.
 * http://www.saxonica.com/
 *
 * @author Patrick Oosterveld
 *
 */
public class TransformerInstrumentFactoryImpl extends TransformerFactoryImpl {
	
	@Override
	public Templates newTemplates(Source source) throws TransformerConfigurationException {
		String  urlString = source.getSystemId();
		if (null != urlString){
			try {
				URL url = new URL(urlString);
				getConfiguration().setTraceListener(new CakupanTraceListener(url));
			} catch (XSLTCoverageException e) {
				throw new RuntimeException("Error during creation of XSLTcoverage!.", e);
			} catch (MalformedURLException e) {
				throw new RuntimeException("URL is not correct!.");
			}
			getConfiguration().setCompileWithTracing(true);
		}else{
			throw new RuntimeException("SystemId not set!.");
		}
		return super.newTemplates(source);
	}
	
	@Override
	public Templates newTemplates(Source source, CompilerInfo info) throws TransformerConfigurationException {
		String  urlString = source.getSystemId();
		if (null != urlString){
			try {
				URL url = new URL(urlString);
				getConfiguration().setTraceListener(new CakupanTraceListener(url));
			} catch (XSLTCoverageException e) {
				throw new RuntimeException("Error during creation of XSLTcoverage!.", e);
			} catch (MalformedURLException e) {
				throw new RuntimeException("URL is not correct!.");
			}
			getConfiguration().setCompileWithTracing(true);
		}else{
			throw new RuntimeException("SystemId not set!.");
		}
		return super.newTemplates(source, info);
	}
	
}
