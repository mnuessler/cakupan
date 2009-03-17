package com.cakupan.xslt.ant;

import java.io.File;


import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;

import com.cakupan.xslt.exception.XSLTCoverageException;
import com.cakupan.xslt.util.CoverageIOUtil;
import com.cakupan.xslt.util.XSLTCakupanUtil;

/**
 * <code>MergeTask</code> converts xml data of the coverage file into HTML reports.
 * @author Patrick Oosterveld
 *
 */
public class ReportTask extends MatchingTask {
	private File destDir;

	@Override
	public void execute() throws BuildException {
		System.out.println("Start reporttask");
		CoverageIOUtil.setDestDir(destDir);
		try {
			XSLTCakupanUtil.generateCoverageReport();
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
