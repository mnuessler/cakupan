package com.cakupan.xslt.report;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

import com.cakupan.xslt.data.CoverageFile;
import com.cakupan.xslt.exception.XSLTCoverageException;
import com.cakupan.xslt.util.CoverageIOUtil;
import com.cakupan.xslt.util.XMLHelper;
import com.cakupan.xslt.util.XStreamUtil;


public class HTMLCoverageReport {

	private static final String MAIN_CSS = "main.css";
	private static final String SORT_CSS = "sortabletable.css";
	private static final String SORT_JS = "sortabletable.js";
	private static final String CUSTOM_JS = "customsorttypes.js";
	private static final String BLANK_PNG = "blank.png";
	private static final String DOWNSIMPLE_PNG = "downsimple.png";
	private static final String UPSIMPLE_PNG = "upsimple.png";
	private static final String SOURCE_VIEWER_CSS = "source-viewer.css";
	private static final String FRAME_SUMMARY = "xslt_summary.html";
	private static final String HTML_EXT = ".html";

	
	public void writeCoverageReport(Map<String, CoverageFile> coverageMap, String outputDir) throws XSLTCoverageException{
		try {
			String content = getCoverageStatsAsHTML(coverageMap);
			CoverageIOUtil.write(content, new FileOutputStream(outputDir + File.separator + FRAME_SUMMARY));
			CoverageIOUtil.write(CoverageIOUtil.toString(HTMLCoverageReport.class.getResourceAsStream(MAIN_CSS)), new File(outputDir + File.separator + MAIN_CSS).toURI());
			CoverageIOUtil.write(CoverageIOUtil.toString(HTMLCoverageReport.class.getResourceAsStream(SORT_CSS)), new File(outputDir + File.separator + SORT_CSS).toURI());
			CoverageIOUtil.write(CoverageIOUtil.toString(HTMLCoverageReport.class.getResourceAsStream(SORT_JS)), new File(outputDir + File.separator + SORT_JS).toURI());
			CoverageIOUtil.write(CoverageIOUtil.toString(HTMLCoverageReport.class.getResourceAsStream(DOWNSIMPLE_PNG)), new File(outputDir + File.separator + DOWNSIMPLE_PNG).toURI());
			CoverageIOUtil.write(CoverageIOUtil.toString(HTMLCoverageReport.class.getResourceAsStream(BLANK_PNG)), new File(outputDir + File.separator + BLANK_PNG).toURI());
			CoverageIOUtil.write(CoverageIOUtil.toString(HTMLCoverageReport.class.getResourceAsStream(UPSIMPLE_PNG)), new File(outputDir + File.separator + UPSIMPLE_PNG).toURI());
			CoverageIOUtil.write(CoverageIOUtil.toString(HTMLCoverageReport.class.getResourceAsStream(CUSTOM_JS)), new File(outputDir + File.separator + CUSTOM_JS).toURI());
			CoverageIOUtil.write(CoverageIOUtil.toString(HTMLCoverageReport.class.getResourceAsStream(SOURCE_VIEWER_CSS)), new File(outputDir + File.separator + SOURCE_VIEWER_CSS).toURI());
		} catch (Exception e) {
			throw new XSLTCoverageException("could not write frame-summary data to file!", e);
		}
		String xml = null;
		calculateCoveragePercentage(coverageMap);
		XSLTToHTMLReport report = null;
		for (CoverageFile file : coverageMap.values()) {
			try {
				report = new XSLTToHTMLReport();
				xml = CoverageIOUtil.toString(new FileInputStream(new File(file.getUri())));
				CoverageIOUtil.write(report.build(file, xml), new FileOutputStream(outputDir + File.separator + file.getFileName() + HTML_EXT));
			} catch (Exception e) {
				throw new XSLTCoverageException("could not write the XSL report(s)!", e);
			}
		}
		
	}
	
	/**
	 * Calculates each XSLT coverage percentage
	 * @param coverageMap
	 */
	private void calculateCoveragePercentage(Map<String, CoverageFile> coverageMap) {
		for (CoverageFile file : coverageMap.values()) {
			// calculate coverage
			file.setCoveragePercentage(file.getCoveragePercentage());
		}
	}

	/**
	 * Gets coverage stats
	 * 
	 * @return coverage stats xml
	 * @throws XSLTCoverageException
	 */
	private String getCoverageStatsAsHTML(Map<String, CoverageFile> coverageMap) throws XSLTCoverageException {
		StreamSource xsl = new StreamSource(HTMLCoverageReport.class.getResourceAsStream("XsltCoverageStats.xsl"), HTMLCoverageReport.class.getResource("XsltCoverageStats.xsl").toString());
		StreamSource xml = new StreamSource(new ByteArrayInputStream(getCoverageStats(coverageMap).getBytes()));
		String response = null;
		try {
			response = XMLHelper.doXSLT20(xml, xsl, null, null);
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
