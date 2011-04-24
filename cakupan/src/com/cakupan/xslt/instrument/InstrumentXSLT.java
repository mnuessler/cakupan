package com.cakupan.xslt.instrument;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

import com.cakupan.xslt.exception.XSLTCoverageException;
import com.cakupan.xslt.util.CoverageIOUtil;
import com.cakupan.xslt.util.PropertyReader;
import com.cakupan.xslt.util.XSLTCakupanUtil;

/**
 * <code>InstrumentXSLT</code> instruments each line with XSLT-instructions
 * 
 * @author Patrick Oosterveld
 * 
 */
public class InstrumentXSLT {

	private static String[] elementsToBeSkipped = null;

	private static final String ELEMENTS_TO_BE_SKIPPED = "skipElements";

	private static final String TEMPLATE = "<xsl:template";

	private static final String END_TEMPLATE = "</xsl:template>";

	public InstrumentXSLT() {
		init();
	}

	/**
	 * Initialiseert de coverage data op basis van een of meerdere directories
	 * (String)
	 * 
	 * @param dir
	 *            directory
	 * @throws XSLTCoverageException
	 */
	public void initCoverageMap(String file) throws XSLTCoverageException {
		File xslt = new File(file);
		if (!xslt.isDirectory() && file.contains(".xsl")) {
			try {
				instrument(CoverageIOUtil.toString(new FileInputStream(xslt)),
						xslt);
			} catch (IOException e) {
				throw new XSLTCoverageException(
						"Error while intrumenting XSLTs!", e);
			}
		}
	}

	/**
	 * Instrumenteert xsl
	 * 
	 * @param xsltInhoud
	 *            content
	 * @param fileName
	 *            filename
	 * @return geinstrumenteerd xsl
	 * @throws XSLTCoverageException
	 */
	private void instrument(String xsltInhoud, File fileName)
			throws XSLTCoverageException {
		BufferedReader reader = new BufferedReader(new StringReader(xsltInhoud));
		String str = null;
		int rowCount = 1;
		boolean skipComment = false;
		try {
			while ((str = reader.readLine()) != null) {
				str = str.trim();
				if (str.startsWith("<!--")) {
					skipComment = true;
				}
				if (!skipComment && str.startsWith("<")) {
					if (!skipElement(str)) {
						XSLTCakupanUtil.setInitHit(fileName.toURI(), fileName
								.getName(), rowCount);
					}

					if (str.startsWith(TEMPLATE)) {
						String name = str.substring(TEMPLATE.length()).trim();
						int i = name.indexOf('>');
						if (i >= 0) {
							name = name.substring(0, i);
						}
						XSLTCakupanUtil.startTemplate(fileName.getName(), name,
								rowCount);
					} else if (str.startsWith(END_TEMPLATE)) {
						XSLTCakupanUtil.endTemplate(fileName.getName(),
								rowCount);
					}
				}
				if (str.contains("-->")) {
					skipComment = false;
				}
				rowCount++;
			}
		} catch (Exception e) {
			throw new XSLTCoverageException(
					"Error while intrumenting an XSLT!", e);
		}
	}

	/**
	 * skip not traceable elements
	 * 
	 * @param str
	 * @return skip yes or no
	 */
	private boolean skipElement(String str) {
		boolean result = false;
		if (elementsToBeSkipped != null) {
			for (int i = 0; i < elementsToBeSkipped.length; i++) {
				result = str.startsWith(elementsToBeSkipped[i]);
				if (result) {
					break;
				}
			}
		}
		return result;
	}

	/**
	 * load the elements to be skipped from the property file
	 */
	private void init() {
		Collection<String> elementList = new ArrayList<String>();
		String props = PropertyReader.getProperty(ELEMENTS_TO_BE_SKIPPED);
		if (null != props) {
			StringTokenizer token = new StringTokenizer(props, ";");
			while (token.hasMoreElements()) {
				elementList.add((String) token.nextElement());
			}
		}
		elementsToBeSkipped = elementList.toArray(new String[] {});
	}

}
