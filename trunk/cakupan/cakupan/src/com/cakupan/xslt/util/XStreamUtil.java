package com.cakupan.xslt.util;

import java.io.InputStream;

import com.thoughtworks.xstream.XStream;

/**
  * http://xstream.codehaus.org/license.html
  */
public class XStreamUtil {

	private static XStream xstream = new XStream();
	
	public static String toXML(Object obj){
		return xstream.toXML(obj);
	}
	
	public static Object fromXML(String xml){
		return xstream.fromXML(xml);
	}

	public static Object fromXML(InputStream in) {
		return xstream.fromXML(in);
	}

}
