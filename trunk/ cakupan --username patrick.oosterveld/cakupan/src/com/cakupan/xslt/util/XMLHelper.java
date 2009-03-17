package com.cakupan.xslt.util;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamResult;

import net.sf.saxon.TransformerFactoryImpl;

/**
 * This is a helper class for all kinds of xml manipulations
 * 
 * @author Patrick Oosterveld
 */
public class XMLHelper {

	/**
	 * Makes an XSLT translation and return the resulting string.
	 * 
	 * @param xmlSource
	 *            xml source
	 * @param xsltSource
	 *            xsl source
	 * @param parameters
	 *            for stylesheet
	 * @param resolver
	 *            the URI resolver
	 * @return xslt result
	 * @throws TransformerConfigurationException
	 *             configuration exception
	 * @throws TransformerException
	 *             transformer exception
	 */
	public static <T> String doXSLT20(Source xmlSource, Source xsltSource, Map<String, T> parameters, URIResolver resolver) throws TransformerConfigurationException, TransformerException {
		// Prepare transformer
		ByteArrayOutputStream transformOut = new ByteArrayOutputStream();
		Result result = new StreamResult(transformOut);
		TransformerFactory transFact = new TransformerFactoryImpl();
		if (resolver != null) {
			transFact.setURIResolver(resolver);
		}
		// Perform Transform
		Transformer trans = transFact.newTransformer(xsltSource);
		// set the parameters that are passed to the stylesheet
		if (parameters != null) {
			Iterator<Entry<String, T>> it = parameters.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, T> entry = it.next();
				String key = (String) entry.getKey();
				Object value = entry.getValue();
				if (value != null) {
					trans.setParameter(key, value);
				}
			}
		}
		trans.transform(xmlSource, result);
		return transformOut.toString();
	}
}