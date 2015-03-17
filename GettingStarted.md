# Introduction #

Assuming a project with 50000 lines of Java code and having enough unit tests to get a decent Java code coverage. The same project could also have 12000 lines of XSLT code. Although XSLT code could be tested (by means of unit tests) the coverage will be unknown. There is simple no good XSLT coverage tool available on the market yet.<br />
This is the reason why we started to build our own XSLT coverage tool. The first version was based on extended XSLT with static java instruction in each possible xsl:match. Unfortunately this approach was not reliable enough and the of XSLT lines coverage was too summarily. In 2008 we change to the Saxon api. This API has the advantage of having a decent tracing mechanism which we used for the XSLT coverage tool. <br />
Recently Cakupan implemented the trace facility of Xalan.


# Getting started with ant tasks (thanks to cobertura) #

Cakupan works with custom ant tasks. These tasks has to be declared in you ant build file, see below.
```
<taskdef classpath="cakupan-x.x.jar" resource="cakupantasks.properties"/>
```
_Even it is possible to use these Ant tasks with Maven by using Antrun._

Okay we're ready for the next step.

# Instrumenting XSLTs #

Cokupan instruments your XSLTs by reading the file and saving the linenumbers of XSLT instructions. Default some instructions will be

skipped, see skipped elements for more info.
This data is saved in the destDir directory mentioned in the ant task.
The data contains:
  * URL of the XSLT
  * linenumbers of the XSLT instructions
  * unique key (is the filename of the XSLT)

An example of the ant task is given below. Destdir is the directory where the instrumentation file is stored and with fileset you can point

out where the XSLTs are stored.
```
   <target name="instrumentXSLTCoverage">
        <cakupan-instrument destdir="${instrmtxslt.dir}">
         <fileset dir="../Business/src" includes="**/*.xsl"/>
        </cakupan-instrument>
    </target>
```

# Running an instrumented application #

The most important task of the coverage tool is testing your application by running the unit tests. Be sure that instrumentation file is present and make it available for using by setting the system property 'cakupan.dir'. Another system property is used for retrieving the Cakupan TransformerFactoryImpl, see system property javax.xml.transform.TransformerFactor '. This class takes care of updating the When you
> are using the Saxon S9 api you have to write your own class for setting the Saxon trace hook, see using Saxon S9 api.

```
   <target name="junit" description="Runs the unit tests">
        <delete dir="${junit.out.dir.xml}"/>
        <mkdir  dir="${junit.out.dir.xml}"/>
        <junit printsummary="yes" haltonfailure="no">
                        <!--sysproperty
                                key="javax.xml.transform.TransformerFactory"
                           value="com.cakupan.xslt.transform.XalanTransformerInstrumentFactoryImpl" /-->
                        <sysproperty
                                key="javax.xml.transform.TransformerFactory"
                           value="com.cakupan.xslt.transform.SaxonCakupanTransformerInstrumentFactoryImpl" />
                        <sysproperty
                                key="cakupan.dir"
                                value="${instrmtxslt.dir}"/>
            <classpath refid="classpath.test"/>
            <formatter type="xml"/>
            <batchtest fork="yes" todir="${junit.out.dir.xml}">
                <fileset dir="${test.dir}" includes="**/*Test.java"/>
            </batchtest>
        </junit>
    </target>
```
NOTE: TransformerFactory needs for creating a new template a source object of the XSLT file. Be sure that the systemId is filled in correctly, meaning the absolute URL of the XSLT file, see example XSLTTest.java.

# Cakupan reporting #

During the unit tests the instrumentation file is updated with latest data. In other words the count field of a linenumber increases with one whenever a hit is encountered.
The report ant task transfer gathered coverage data, which is stored in instrumentation file, into an HTML coverage report.

```
   <target name="reportCakupan">
        <cakupan-report destdir="report/cakupan"/>
    </target> 
```

# Cakupan coverage data merging #

Sometimes it is necessary to merge several instrumentation/coverage files together, for example, to produce a single consolidated coverage report from several different test runs of an application.
Coverage merging with the merge tag.

```
      <target name="mergeCakupan">
        <cakupan-merge destdir="report/merged/cakupan">
              <fileset dir="${instrmtxslt.business.dir}"/>
              <fileset dir="${instrmtxslt.interfaces.dir}"/>
        </cakupan-merge>
    </target>
```

# Emma code coverage XML #

Generate [Emma](http://emma.sourceforge.net/index.html) XML report file which can be used with Continuous Integration (CI) Servers.

```
	<!-- building the emma report-->   
        <target name="emmaReportCakupan">
	 <cakupan-emma-report destdir="report/cakupan"/>
        </target>
```

# Using Saxon S9api #

When using S9api you will instantiate a processor which will create XsltCompiler object, with these two objects is it possible to create a hook, see example below.

```
    public XsltCompiler createXsltCompiler(URL url) {
        Processor proc = new Processor(false);
        XsltCompiler comp = proc.newXsltCompiler();
        try {
            proc.getUnderlyingConfiguration().setTraceListener(new CakupanTraceListener(url));
            // enable tracing if succeeded to loaded coverage trace listener
            comp.setCompileWithTracing(true);
 
        } catch (XSLTCoverageException e) {
            // if xsl's where not instrumented before, there is no coverageFile
            // in that case, do not tracing xsl's
            if (e.getRefId() == CakupanException.NO_COVERAGE_FILE) {
                System.out.println("No coverage file found for url: " + url);
            } else {
                // something really wrong happened
                throw new ApplicationException(e);
            }
        }
        return comp;
    }
```
You can wrap this up with an abstract factory design pattern which returns an xsltCompiler with tracelistener or a clean XsltCompiler. The first you can use during unit tests and the latter in your production environment.

# External Libs #
Cakupan depends on some external libs. These libs can be found at http://code.google.com/p/cakupan/source/browse/#svn/trunk/cakupan/lib. <br />
The libs are:
  * commons-lang-2.1.jar (HTML escaping)
  * saxon9.jar
  * junit-3.8.1.jar
  * xpp3-1.1.4c.jar
  * xstream-1.2.2.jar
  * Xalan-2.**.**.jar (ico using Xalan iso Saxon)

# Known Limitations #

  * When running junitreport ant task be sure that you first load the Xalan jar and then the Saxon jar. This is due classloader of the junitreport ant task. It requests only for a specific Xalan `TransformerFactoryImpl`.