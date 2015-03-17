For discussing issues, bugs and features, please use [Cakupan forum](http://groups.google.com/group/xslt_cakupan).<br />
<a href='Hidden comment: 
17 Dec 2011: <b>NEW

Unknown end tag for &lt;/b&gt;

 Saxon plugin supports Indigo (eclipse 3.7.1), see  [http://95.211.222.102/saxon.debugger/update-site/ update-site]. _thanks to Rusty_ <br/>
9 May 2011: Eclipse(Helios) Saxon plugin with XSL debugging, see org.eclipse.wst.xsl.saxon_1.0.100.*v201004290328*.jar at [http://code.google.com/p/cakupan/downloads/list downloads]. _thanks to Colin_<br/>
6 Nov 2009: Eclipse(Galileo) Saxon plugin with XSL debugging, see [http://code.google.com/p/cakupan/downloads/list downloads].<br/>
_Instruction_<br/>
Just replace the default saxon plugin which is distributed with XSL tools with this one and select from processors the Saxon variant and add the Saxon jars and off you go, see <a href="http://cakupan.googlecode.com/files/saxon_debug_procs.gif">picture

Unknown end tag for &lt;/a&gt;

!<br/>
_Note:_ the version of the replacing one should be the same as the replaced one  org.eclipse.wst.xsl.saxon_1.0.0.*v200904240436*.jar (Galileo version) or org.eclipse.wst.xsl.saxon_1.0.100.*v201004290328*.jar (Helios version).
----
<br/>
'></a>
01 Aug 2012: Cakupan is added to the Maven central repo. _thanks to Matthias_ <br />
20 Oct 2011: Speeding up Cakupan. Redesign of the serialization process leads to a better performance of Cakupan during the execution of your tests.<br />
14 Oct 2011: A Maven plugin for the XSLT test coverage tool Cakupan, see [Maven plugin](http://cakupan-maven.sourceforge.net/). _thanks to Matthias Nuessler_<br />
13 Oct 2011: Bug fix release (issues 4,5,6)<br />
23 Apr 2011: New feature; generating [Emma](http://emma.sourceforge.net/index.html) XML report file which can be used with Continuous Integration (CI) Servers.
<br />
17 Feb 2011: Fixed Null pointer bug in `XalanTransformerInstrumentFactoryImpl`
<br />
19 Nov 2009: Xalan support <br />
Cakupan coverage tool is a free Java based tool that calculates the percentage of XSLT instruction lines which were executed during unit tests. The tool is using the Saxon or Xalan trace facilities and stripped HTML layout of Cobertura.
<br />
<a href='Hidden comment: 
<br/>
You can donate to the project and finance our cost.<br/>
<a href="https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=3NWCBGPFEABFJ&lc=NL&item_name=Cakupan&currency_code=EUR&bn=PP%2dDonationsBF%3abtn_donate_SM%2egif%3aNonHosted"><img src="https://www.paypalobjects.com/en_US/i/btn/btn_donate_SM.gif" border="0" name="submit" alt="PayPal - The safer, easier way to pay online!"/>

Unknown end tag for &lt;/a&gt;


'></a>
<br />

A sample report (embedded in the jar file):
<img src='http://cakupan.googlecode.com/svn/trunk/cakupan/images/cakupan_summary.gif' />
<br />
A sample XSLT coverage report:
<img src='http://cakupan.googlecode.com/svn/trunk/cakupan/images/cakupan_coverage.gif' />
<br />