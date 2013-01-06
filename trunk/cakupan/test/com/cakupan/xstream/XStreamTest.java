package com.cakupan.xstream;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.util.TreeMap;

import org.junit.Test;

import com.cakupan.xslt.data.CoverageFile;
import com.cakupan.xslt.data.CoverageLine;
import com.cakupan.xslt.util.XStreamUtil;

/**
 * Tests related to XStream.
 *
 * @author Matthias Nuessler
 */
public class XStreamTest {

    @Test
    public void testThatCoverageFileCanBeDeserialized() throws Exception {
        // given
        String key = "test.xsl";
        CoverageLine coverageLine = new CoverageLine(17);
        CoverageFile coverageFile = new CoverageFile(key, new URI("file:///foo/bar"), coverageLine);
        TreeMap<String, CoverageFile> originalmap = new TreeMap<String, CoverageFile>();
        originalmap.put(coverageFile.getFileName(), coverageFile);

        // when
        String xml = XStreamUtil.toXML(originalmap);
        Object fromXml = XStreamUtil.fromXML(xml);
        @SuppressWarnings("unchecked")
        TreeMap<String, CoverageFile> mapFromXML = (TreeMap<String, CoverageFile>) fromXml;

        // then
        assertEquals(originalmap, mapFromXML);
    }

}
