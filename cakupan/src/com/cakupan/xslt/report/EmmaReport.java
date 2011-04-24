package com.cakupan.xslt.report;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

import com.cakupan.xslt.data.CoverageFile;
import com.cakupan.xslt.exception.XSLTCoverageException;
import com.cakupan.xslt.util.CoverageIOUtil;
import com.cakupan.xslt.util.XMLHelper;
import com.cakupan.xslt.util.XStreamUtil;


public class EmmaReport {

	private static final String HTML_EXT = ".xml";

	
	/**
	 * Gets Emma report 
	 * 
	 * @return Emma  xml
	 * @throws XSLTCoverageException
	 */
	public String writeEmmaReport(Map<String, CoverageFile> coverageMap, String outputDir) throws XSLTCoverageException {
		StreamSource xsl = new StreamSource(EmmaReport.class.getResourceAsStream("cakupan-to-emma.xslt"), EmmaReport.class.getResource("cakupan-to-emma.xslt").toString());
		StreamSource xml = new StreamSource(new ByteArrayInputStream(getCoverageStats(coverageMap).getBytes()));
		String response = null;
		try {
			response = XMLHelper.doXSLT20(xml, xsl, null, null);
			CoverageIOUtil.write(response, new FileOutputStream(outputDir + File.separator + "emma-report"+HTML_EXT));
		} catch (Exception e) {
			throw new XSLTCoverageException("XSLT transformation error!", e);
		}
		return response;
	}

	private String getCoverageStats(Map<String, CoverageFile> coverageMap) {
		String resultaat = XStreamUtil.toXML(coverageMap);
		return resultaat;
	}


}
