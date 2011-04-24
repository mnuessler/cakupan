package com.cakupan.xslt.ant;

import java.io.File;


import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;

import com.cakupan.xslt.exception.XSLTCoverageException;
import com.cakupan.xslt.util.CoverageIOUtil;
import com.cakupan.xslt.util.XSLTCakupanUtil;

/**
 * <code>ReportEmmaTask</code> converts xml data of the coverage file into Emma reports.
 * @author Patrick Oosterveld
 *
 */
public class EmmaReportTask extends MatchingTask {
	private File destDir;

	@Override
	public void execute() throws BuildException {
		System.out.println("Start emmareporttask");
		CoverageIOUtil.setDestDir(destDir);
		try {
			XSLTCakupanUtil.generateEmmaReport();
		} catch (XSLTCoverageException e) {
			if (e.getRefId() == XSLTCoverageException.NO_COVERAGE_FILE) {
				getProject().log("No coverage files found in ["+destDir.getPath()+"]!", Project.MSG_WARN);
			} else {
				throw new BuildException("Failed to make a coverage report!", e);
			}
		}
		System.out.println("End reporttask");
		super.execute();
	}

	public void setDestDir(File destDir) {
		this.destDir = destDir;
	}
}
