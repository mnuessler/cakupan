package com.cakupan.xslt.ant;

import org.apache.tools.ant.BuildFileTest;

public class TaskTest extends BuildFileTest {
	
	   public void setUp() {
	        // initialize Ant
	        configureProject("build.xml");
	    }


	    public void testMergeCakupan() {
	        // execute target 'mergeCakupan' and expect a message
	        // ' ' in the log
	        expectLogContaining("mergeCakupan", "");
	    }
	    public void testReportCakupanNoCoverage() {
	        // execute target 'reportCakupant' and expect a message
	        // 'coverage' in the log
	    	expectLogContaining("reportCakupanError", "coverage");
	    }
	    public void testReportCakupan() {
	        // execute target 'reportCakupant' and expect no message
	    	expectLogContaining("reportCakupan", "");
	    }
	    public void testEmmaReportCakupan() {
	        // execute target 'emmaReportCakupan' and expect no message
	    	expectLogContaining("emmaReportCakupan", "");
	    }
	    public void testInstrumentCakupan(){
	    	expectLogContaining("instrumentCakupan", "");
	    }

}
