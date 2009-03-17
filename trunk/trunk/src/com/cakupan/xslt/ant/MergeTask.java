package com.cakupan.xslt.ant;

import java.io.File;
import java.util.LinkedList;
import java.util.List;


import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.FileSet;

import com.cakupan.xslt.exception.XSLTCoverageException;
import com.cakupan.xslt.util.CoverageIOUtil;
import com.cakupan.xslt.util.XSLTCakupanUtil;

/**
 * <code>MergeTask</code> merges all the coverage files found in different directories.
 * @author Patrick Oosterveld
 *
 */
public class MergeTask extends MatchingTask {
	private File destDir;

	private List<FileSet> fileSets = new LinkedList<FileSet>();

	@Override
	public void execute() throws BuildException {
		CoverageIOUtil.setDestDir(destDir);
		System.out.println("Start merging files");
		if (fileset != null) {
			File file = null;
			for (FileSet fileset : fileSets) {
				file = fileset.getDir(getProject());
				try {
					XSLTCakupanUtil.addCoverageStats(file);
				} catch (XSLTCoverageException e) {
					getProject().log("No coverage files found in ["+file.getPath()+"]!", Project.MSG_WARN);
				}
			}
		} else {
			throw new BuildException("No coverage file directories set!");
		}
		try {
			XSLTCakupanUtil.dumpCoverageStats();
		} catch (XSLTCoverageException e) {
			throw new BuildException("Could not dump the coverage file!");
		}
		System.out.println("End merging files");
		super.execute();
	}

	public void setDestDir(File destDir) {
		if (!destDir.exists()) {
			getProject().log("destDir is not yet a directory, it will be in a minute!", Project.MSG_INFO);
			destDir.mkdirs();
		}
		this.destDir = destDir;
	}

	public void addFileset(FileSet fileSet) {
		fileSets.add(fileSet);
	}
}
