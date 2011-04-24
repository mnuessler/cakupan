package com.cakupan.xslt.example;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.MalformedURLException;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import junit.framework.TestCase;

public class XSLTTest extends TestCase {
	
	@Override
	protected void setUp() throws Exception {
		System.setProperty("javax.xml.transform.TransformerFactory","com.cakupan.xslt.transform.SaxonCakupanTransformerInstrumentFactoryImpl");
//		System.setProperty("javax.xml.transform.TransformerFactory","com.cakupan.xslt.transform.XalanTransformerInstrumentFactoryImpl");
		System.setProperty("cakupan.dir","report/cakupan");
		super.setUp();
	}
	
	public void testTransformation(){
		Source xml = null;
		Source xsl = null;
		try {
			File xsltFile = new File("test"+File.separator+"XsltCoverageStats.xsl");
			xml = new StreamSource("test"+File.separator+"coverage.xml");
			//use always the URL of the XSLT
			xsl =  new StreamSource(xsltFile.toURI().toURL().toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			String result = doXSLT(xml, xsl);
			assertNotNull(result);
		} catch (Exception e) {
			fail("Exception occured: " + e.getMessage());
		}
	}
	
    private static String doXSLT(Source xml, Source xsl) throws TransformerConfigurationException, TransformerException {
        // Prepare transformer
        ByteArrayOutputStream transformOut = new ByteArrayOutputStream();
        Result result = new StreamResult(transformOut);
        TransformerFactory transFact = TransformerFactory.newInstance();

        // Perform Transform
        Transformer trans = transFact.newTransformer(xsl);
        trans.transform(xml, result);
        return transformOut.toString();
    }

}
