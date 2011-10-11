package com.cakupan.xslt.transform;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.TooManyListenersException;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;

import org.apache.xalan.processor.TransformerFactoryImpl;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;

import com.cakupan.xslt.exception.XSLTCoverageException;
import com.cakupan.xslt.trace.XalanTraceListener;

/**
 * The <code>SaxonCakupanTransformerInstrumentFactoryImpl</code> is an
 * implemententation of Xalan <link>TransformerFactoryImpl<link> which makes it
 * possible to measure coverage statistics of used XSLTs.
 * 
 * @author Patrick Oosterveld
 * @author Andrew Martignoni III 
 */
public class XalanTransformerInstrumentFactoryImpl extends
        TransformerFactoryImpl
{
    @Override
    public Templates newTemplates(Source source)
            throws TransformerConfigurationException
    {
        return new TemplatesWrapper(super.newTemplates(source), source);
    }

    @Override
    public Transformer newTransformer(Source source)
            throws TransformerConfigurationException
    {
        TransformerImpl transformerImpl = (TransformerImpl)super.newTransformer(source);
        addTraceListener(transformerImpl, source);
        return transformerImpl;
    }

    static void addTraceListener(TransformerImpl transformerImpl, Source source)
    {
        URL url = toURL(source);
        TraceManager trMgr = transformerImpl.getTraceManager();
        try
        {
            trMgr.addTraceListener(new XalanTraceListener(url));
        }
        catch (TooManyListenersException e)
        {
            throw new RuntimeException(e);
        }
        catch (XSLTCoverageException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static URL toURL(Source source)
    {
        String urlString = source.getSystemId();
        URL url;
        if (null != urlString)
        {
            try
            {
                url = new URL(urlString);
            }
            catch (MalformedURLException e)
            {
                throw new RuntimeException("URL is not correct!."+urlString);
            }
        }
        else
        {
            throw new RuntimeException("SystemId not set!.");
        }
        return url;
    }

    private static class TemplatesWrapper implements Templates
    {
        private Templates delegate;

        private Source source;

        TemplatesWrapper(Templates t, Source s)
        {
            delegate = t;
            source = s;
        }

        /** {@inheritDoc} */
        @Override
        public Transformer newTransformer()
                throws TransformerConfigurationException
        {
            Transformer transformer = delegate.newTransformer();
            addTraceListener((TransformerImpl)transformer, source);
            return transformer;
        }

        /** {@inheritDoc} */
        @Override
        public Properties getOutputProperties()
        {
            return delegate.getOutputProperties();
        }
    }
}