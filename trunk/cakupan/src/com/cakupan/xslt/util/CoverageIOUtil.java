package com.cakupan.xslt.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;

import com.cakupan.xslt.exception.XSLTCoverageException;

/**
 * util class dealing with IO activities
 * 
 * A part of the source is distributed with the Apache 2.0 License
 * http://www.apache.org/licenses/LICENSE-2.0
 */
public class CoverageIOUtil {

	private static final String SYS_PROP_XSLT_COVERAGE_DIR = "cakupan.dir";

	private static File destDir = null;

	/**
	 * Return the directory path where the coverage file has been written
	 * 
	 * @return the path of the coverage file or null if path not has been set
	 * @throws XSLTCoverageException
	 */
	public static String getDestDir() throws XSLTCoverageException {
		if (destDir == null && null != getXsltCoverageDir()) {
			String dir = getXsltCoverageDir();
			File file = new File(dir);
			if (!file.exists()) {
				file.mkdirs();
			}
			destDir = file;
		}
		System.out.println("destdir: " + destDir.getPath());
		return destDir != null ? destDir.getPath() : null;
	}

	/**
	 * @param destDir
	 *            the destDir to set
	 */
	public static void setDestDir(File destDir) {
		CoverageIOUtil.destDir = destDir;
	}

	/**
	 * save the css files
	 * 
	 * @param content
	 * @param remoteFile
	 */
	public static void saveCSSFiles(String content, URI remoteFile) {
		try {
			write(content, new FileOutputStream(new File(remoteFile)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks whether system variable xslt.coverage.dir is set to true in order
	 * to get the dir of the coverage xml
	 * 
	 * @return coverage dir
	 * @throws XSLTCoverageException
	 */
	public static String getXsltCoverageDir() throws XSLTCoverageException {
		String coverageFile = System.getProperty(SYS_PROP_XSLT_COVERAGE_DIR);
		if (coverageFile != null) {
			return coverageFile;
		} else {
			throw new XSLTCoverageException("System property ["
					+ SYS_PROP_XSLT_COVERAGE_DIR + "] not set");
		}
	}

	/**
	 * write files to a remote destination
	 * 
	 * @param content
	 * @param remoteFile
	 * @throws IOException
	 */
	public static void write(String content, URI remoteFile) throws IOException {
		FileOutputStream ostream = new FileOutputStream(new File(remoteFile));
		write(content, ostream);
	}

	/**
	 * write date to a outputstream
	 * 
	 * @param content
	 * @param stream
	 *            outputstream
	 * @throws IOException
	 */
	public static void write(String content, OutputStream stream)
			throws IOException {
		stream.write(content.getBytes());
	}

	public static int copy(InputStream input, OutputStream output)
			throws IOException {
		long count = copyLarge(input, output);
		if (count > 2147483647L)
			return -1;
		else
			return (int) count;
	}

	public static long copyLarge(InputStream input, OutputStream output)
			throws IOException {
		byte buffer[] = new byte[4096];
		long count = 0L;
		for (int n = 0; -1 != (n = input.read(buffer));) {
			output.write(buffer, 0, n);
			count += n;
		}

		return count;
	}

	public static String toString(InputStream input) throws IOException {
		StringWriter sw = new StringWriter();
		copy(input, sw);
		return sw.toString();
	}

	public static void copy(InputStream input, Writer output)
			throws IOException {
		InputStreamReader in = new InputStreamReader(input);
		copy(((Reader) (in)), output);
	}

	public static int copy(Reader input, Writer output) throws IOException {
		char buffer[] = new char[4096];
		int count = 0;
		for (int n = 0; -1 != (n = input.read(buffer));) {
			output.write(buffer, 0, n);
			count += n;
		}

		return count;
	}

}
