package com.cakupan.xslt.ant;

import java.io.File;
import java.util.LinkedList;
import java.util.List;


import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.FileSet;

import com.cakupan.xslt.exception.XSLTCoverageException;
import com.cakupan.xslt.instrument.InstrumentXSLT;
import com.cakupan.xslt.util.CoverageIOUtil;
import com.cakupan.xslt.util.XSLTCakupanUtil;

/**
 * <code>InstrumentTask</code> instrument the XSLTs by means of writing instrumental data to coverage file.
 * @author Patrick Oosterveld
 *
 */
public class InstrumentTask extends MatchingTask {
	private File destDir;
	private List<FileSet> fileSets = new LinkedList<FileSet>();

	@Override
	public void execute() throws BuildException {
		InstrumentXSLT instrumentXslt = new InstrumentXSLT();
		System.out.println("Start instrumenting XSLTs.");
		CoverageIOUtil.setDestDir(destDir);
		if (fileset != null) {
			for (FileSet fileset : fileSets) {
				String[] files = fileset.getDirectoryScanner(getProject()).getIncludedFiles();
				if (files != null && files.length >0){
					for (int i = 0; i < files.length; i++) {
						try {
							instrumentXslt.initCoverageMap(fileset.getDir(getProject()).getPath()+File.separator+files[i]);
						} catch (XSLTCoverageException e) {
							getProject().log("Instrumenting file ["+files[i]+"] failed!", Project.MSG_WARN);
						}
					}
					try {
						XSLTCakupanUtil.dumpCoverageStats();
						System.out.println("Instrumented XSLTs: "+XSLTCakupanUtil.getCoverageMap().size());
					} catch (XSLTCoverageException e) {
						throw new BuildException("Dump coverage file failed!");
					}finally{
						XSLTCakupanUtil.cleanCoverageStats();
					}
				}else{
					getProject().log("No XSLTs found!", Project.MSG_WARN);
				}
			}
		} else {
			throw new BuildException("XSLT source directories not set!");
		}
		super.execute();
	}

	public void setDestDir(File destDir) {
		if(!destDir.exists()){
			System.out.println("destDir is not yet a directory, it will be in a minute!");
			destDir.mkdirs();
		}
		this.destDir = destDir;
	}
	
	

    public void addFileset(FileSet fileSet)
    {
        fileSets.add(fileSet);
    }
}
