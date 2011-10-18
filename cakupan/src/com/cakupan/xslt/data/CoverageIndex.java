package com.cakupan.xslt.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.cakupan.xslt.exception.XSLTCoverageException;
import com.cakupan.xslt.util.CoverageIOUtil;

/**
 * The <code>CoverageIndex</code> maintains a random access file that contains
 * an index of all lines and their counts. No need for
 * <code>dumpCoverageStats()</code> after each trace operation.
 * 
 * @author Andrew Martignoni III
 */
public class CoverageIndex
{
    private static final Map<String, CoverageIndex> INDEX_MAP = new HashMap<String, CoverageIndex>();

    private static final int NOT_A_LINE = Integer.MIN_VALUE;

    private final RandomAccessFile file;

    private int size;

    public CoverageIndex(File indexFile) throws FileNotFoundException
    {
        size = (int)indexFile.length();
        file = new RandomAccessFile(indexFile, "rw");
    }

    public int getLineCount(int lineNumber)
    {
        try
        {
            int pos = lineNumber << 2;
            if (pos >= size)
            {
                return 0;
            }
            else
            {
                file.seek(pos);
                int count = file.readInt();
                if (count == NOT_A_LINE)
                {
                    return 0;
                }
                return count;
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void setLineCount(int lineNumber, int count)
    {
        try
        {
            int pos = lineNumber << 2;
            if (pos >= size)
            {
                file.seek(size);
                for (int i = pos - size >> 2; i > 0; i--)
                {
                    file.writeInt(NOT_A_LINE);
                }
                size = pos + 4;
            }
            else
            {
                file.seek(pos);
            }
            file.writeInt(count);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void loadInto(CoverageFile cf) throws XSLTCoverageException
    {
        if (size > 0)
        {
            try
            {
                file.seek(0);
                for (int i = 0, max = size >> 2; i < max; i++)
                {
                    int count = file.readInt();
                    if (count != NOT_A_LINE)
                    {
                        CoverageLine line = cf.getLine(i);
                        if (line != null)
                        {
                            line.setLineCount(count);
                        }
                        else
                        {
                            cf.addLine(new CoverageLine(i, count));
                        }
                    }
                }
            }
            catch (IOException e)
            {
                throw new XSLTCoverageException("Error loading coverage index", e);
            }
        }
    }

    private void close()
    {
        try
        {
            file.close();
        }
        catch (IOException ignore)
        {}
    }

    public static void init(String key, int lineNumber, int count)
            throws XSLTCoverageException
    {
        CoverageIndex index = createIndex(key);
        index.setLineCount(lineNumber, count);
        INDEX_MAP.put(key, index);
    }

    public static void load(Map<String, CoverageFile> coverageMap)
            throws XSLTCoverageException
    {
        String destDir = CoverageIOUtil.getDestDir();
        try
        {
            for (Map.Entry<String, CoverageFile> e : coverageMap.entrySet())
            {
                String key = e.getKey();
                File indexFile = getIndexFile(destDir, key);
                CoverageIndex index = new CoverageIndex(indexFile);
                INDEX_MAP.put(key, index);
                index.loadInto(e.getValue());
            }
        }
        catch (IOException e)
        {
            throw new XSLTCoverageException(e.getMessage(), e);
        }
    }

    public static CoverageIndex get(String key)
    {
        return INDEX_MAP.get(key);
    }

    public static void removeIndexes(Collection<String> keys)
            throws XSLTCoverageException
    {
        for (String key : keys)
        {
            CoverageIndex index = INDEX_MAP.remove(key);
            if (index != null)
            {
                index.close();
            }
        }
        String destDir = CoverageIOUtil.getDestDir();
        for (String key : keys)
        {
            File indexFile = getIndexFile(destDir, key);
            if (indexFile.exists() && !indexFile.delete())
            {
                throw new XSLTCoverageException("Couldn't delete index file: " + indexFile);
            }
        }
    }

    private static CoverageIndex createIndex(String key)
            throws XSLTCoverageException
    {
        try
        {
            return new CoverageIndex(getIndexFile(CoverageIOUtil.getDestDir(), key));
        }
        catch (FileNotFoundException e)
        {
            throw new XSLTCoverageException(e.getMessage(), e);
        }
    }

    private static File getIndexFile(String destDir, String key)
    {
        return new File(destDir, key + ".dat");
    }
}
