package com.cakupan.xslt.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.Map;
import java.util.TreeMap;

import com.cakupan.xslt.data.CoverageFile;
import com.cakupan.xslt.data.CoverageIndex;
import com.cakupan.xslt.data.CoverageLine;
import com.cakupan.xslt.exception.XSLTCoverageException;
import com.cakupan.xslt.report.EmmaReport;
import com.cakupan.xslt.report.HTMLCoverageReport;

/**
 * The <code>XSLTSaxonCoverageUtil</code> class keeps track of the hits per
 * instrumented XSLT. The data will be stored in a static map. The map contains
 * a key (the filename of an XSLT) and a
 * {@link com.cakupan.xslt.data.CoverageFile CoverageFile} data store.
 */
public class XSLTCakupanUtil
{

    private static final String COVERAGEXSTREAMFILE = "coverageFile";

    /** coverageMap */
    static Map<String, CoverageFile> coverageMap = new TreeMap<String, CoverageFile>();

    /**
     * Set coverage hit
     * 
     * @param key key
     * @param uri
     * @param lineNumber
     */
    public static void setHit(String key, URI uri, int lineNumber)
    {
        CoverageIndex index = CoverageIndex.get(key);
        index.setLineCount(lineNumber, index.getLineCount(lineNumber) + 1);
    }

    /**
     * Set initial hit value (during instrumentation)
     * 
     * @param uri
     * @param key key
     * @param lineNumber
     */
    public static void setInitHit(URI uri, String key, int lineNumber)
    {
        if (null != key && null != coverageMap)
        {
            CoverageFile coverageFile = coverageMap.get(key);
            if (null != coverageFile)
            {
                coverageFile.addLine(new CoverageLine(lineNumber, 0));
            }
            else
            {
                // key doesn't contain content
                coverageMap.put(key, new CoverageFile(key, uri, new CoverageLine(lineNumber, 0)));
                try
                {
                    CoverageIndex.init(key, lineNumber, 0);
                }
                catch (XSLTCoverageException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void startTemplate(String key, String name, int lineNumber)
    {
        if (null != key && null != coverageMap)
        {
            if (coverageMap.containsKey(key))
            {
                CoverageFile coverageFile = coverageMap.get(key);
                coverageFile.startTemplate(name, lineNumber);
            }
        }
    }

    public static void endTemplate(String key, int lineNumber)
    {
        if (null != key && null != coverageMap)
        {
            if (coverageMap.containsKey(key))
            {
                CoverageFile coverageFile = coverageMap.get(key);
                coverageFile.endTemplate(lineNumber);
            }
        }
    }

    /**
     * Xsl coverage statistics
     * 
     * @return coverageMap
     */
    public static Map<String, CoverageFile> getCoverageMap()
    {
        return coverageMap;
    }

    /**
     * Get the Coverage File Name, if found in properties file
     * 
     * @return
     */
    public static String getCoverageXstreamFileName()
    {
        return PropertyReader.getProperty(COVERAGEXSTREAMFILE);
    }

    /**
     * Dump the coverage statistics
     * 
     * @throws XSLTCoverageException
     */
    public static void dumpCoverageStats() throws XSLTCoverageException
    {
        // bewaar xstream map to file
        String resultaat = XStreamUtil.toXML(coverageMap);
        try
        {
            CoverageIOUtil.write(resultaat, new FileOutputStream(CoverageIOUtil.getDestDir() + File.separator + getCoverageXstreamFileName()));
        }
        catch (Exception e)
        {
            throw new XSLTCoverageException("Could not dump the coverage file!", e);
        }
    }

    /**
     * cleans up the coveragemap, removes index files
     */
    public static void cleanCoverageStats()
    {
        try
        {
            CoverageIndex.removeIndexes(coverageMap.keySet());
        }
        catch (XSLTCoverageException e)
        {
            throw new RuntimeException(e);
        }
        coverageMap = new TreeMap<String, CoverageFile>();
    }

    /**
     * Load coverage statistics
     * 
     * @throws XSLTCoverageException
     */
    @SuppressWarnings("unchecked")
    public static void loadCoverageStats() throws XSLTCoverageException
    {
        try
        {
            Object object = XStreamUtil.fromXML(CoverageIOUtil.toString(new FileInputStream(CoverageIOUtil.getDestDir() + File.separator + getCoverageXstreamFileName())));
            if (object instanceof Map)
            {
                coverageMap = (Map<String, CoverageFile>)object;
                CoverageIndex.load(coverageMap);
            }
        }
        catch (Exception e)
        {
            // no file found....
            // System.out.println(e.getMessage());
            throw new XSLTCoverageException(XSLTCoverageException.NO_COVERAGE_FILE, "Coverage file not found!", e);
        }
    }

    /**
     * add a coverage statistics (merging coverage statistics)
     * 
     * @param coverageDir dir with coverage file
     * @throws XSLTCoverageException
     */
    @SuppressWarnings("unchecked")
    public static void addCoverageStats(File coverageDir)
            throws XSLTCoverageException
    {
        Object object = null;
        if (coverageDir != null && coverageDir.isDirectory())
        {
            try
            {
                String coveragefile = null;
                File file = null;
                coveragefile = coverageDir.getPath() + File.separator + getCoverageXstreamFileName();
                file = new File(coveragefile);
                if (file.exists())
                {
                    object = XStreamUtil.fromXML(CoverageIOUtil.toString(new FileInputStream(file)));
                    if (object instanceof Map)
                    {
                        Map<String, CoverageFile> newMap = (Map<String, CoverageFile>)object;
                        CoverageIndex.load(newMap);
                        coverageMap.putAll(newMap);
                    }
                }
                else
                {
                    throw new XSLTCoverageException(XSLTCoverageException.NO_COVERAGE_FILE, "Could not find a coverage file!");
                }
            }
            catch (Exception e)
            {
                // no file found....
                throw new XSLTCoverageException("Could not find (a)(ny) coverage file(s)!", e);
            }
        }
    }

    /**
     * Generate coverage reports
     * 
     * @throws XSLTCoverageException
     */
    public static void generateCoverageReport() throws XSLTCoverageException
    {
        // load the coverage statistics into coverageMap
        loadCoverageStats();
        // sync up XML file with indexes
        dumpCoverageStats();
        // generate content
        HTMLCoverageReport report = new HTMLCoverageReport();
        report.writeCoverageReport(coverageMap, CoverageIOUtil.getDestDir());
    }

    /**
     * Generate emma report
     * 
     * @throws XSLTCoverageException
     */
    public static void generateEmmaReport() throws XSLTCoverageException
    {
        // load the coverage statistics into coverageMap
        loadCoverageStats();
        // sync up XML file with indexes
        dumpCoverageStats();
        // generate content
        EmmaReport report = new EmmaReport();
        report.writeEmmaReport(coverageMap, CoverageIOUtil.getDestDir());
    }
}
