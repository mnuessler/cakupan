<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" />
	
	<xsl:template match="/">
		<report>
			<xsl:variable name="fileNodes" select="tree-map/entry/com.cakupan.xslt.data.CoverageFile" />				
			<stats>
				<xsl:variable name="files" select="count($fileNodes)" />				
				<xsl:variable name="lines" select="count($fileNodes/line/com.cakupan.xslt.data.CoverageLine)" />				
				<packages value="1" />
				<classes value="{$files}" />
				<methods value="{$lines}" />
				<srcfiles value="{$files}" />
				<srclines value="{$lines}" />
			</stats>
			<data>
				<xsl:apply-templates select="tree-map" />
			</data>
		</report>
	</xsl:template>
	
	<xsl:template match="tree-map">
		<all name="all XSLT files">
			<xsl:call-template name="summary">
				<xsl:with-param name="top" select="entry/com.cakupan.xslt.data.CoverageFile" />
			</xsl:call-template>
			<package name="cakupan.xslt">
				<xsl:call-template name="summary">
					<xsl:with-param name="top" select="entry/com.cakupan.xslt.data.CoverageFile" />
				</xsl:call-template>
				<xsl:apply-templates select="entry/com.cakupan.xslt.data.CoverageFile" />
			</package>
		</all>
	</xsl:template>
	
	<xsl:template match="com.cakupan.xslt.data.CoverageFile">
		<srcfile name="{preceding-sibling::string}">
			<xsl:call-template name="summary" />
			<class name="{preceding-sibling::string}">
				<xsl:call-template name="summary" />
				<xsl:apply-templates select="templates/com.cakupan.xslt.data.CoverageTemplate" />
			</class>
		</srcfile>
	</xsl:template>
	
	<xsl:template match="com.cakupan.xslt.data.CoverageTemplate">
		<xsl:variable name="lineStart" select="number(lineStart)" />
		<xsl:variable name="lineEnd" select="number(lineEnd)" />
		<method name="{name}">
			<xsl:call-template name="methodSummary">
				<xsl:with-param name="top" select="../..//com.cakupan.xslt.data.CoverageLine[number(lineNumber)>=$lineStart and number(lineNumber)&lt;=$lineEnd]" />
			</xsl:call-template>
		</method>
	</xsl:template>
	
	<xsl:key use="concat(../../../key,.)" name="start" match="lineStart" />	

	<xsl:template name="summary">
		<xsl:param name="top" select="." />
		<xsl:call-template name="coverage">
			<xsl:with-param name="type" select="'class, %'" />
			<xsl:with-param name="value" select="count($top[.//lineCount[.!='0']])" />
			<xsl:with-param name="max" select="count($top)" />
		</xsl:call-template>
		<xsl:variable name="lineStarts" select="$top//lineStart" />
		<xsl:call-template name="coverage">
			<xsl:with-param name="type" select="'method, %'" />
			<xsl:with-param name="value" select="count($top//lineNumber[key('start',concat(../../../key,.)) and ../lineCount!='0'])" />
			<xsl:with-param name="max" select="count($top//com.cakupan.xslt.data.CoverageTemplate)" />
		</xsl:call-template>
		<xsl:call-template name="linesSummary">
			<xsl:with-param name="top" select="$top" />
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template name="methodSummary">
		<xsl:param name="top" select="." />
		<xsl:call-template name="coverage">
			<xsl:with-param name="type" select="'method, %'" />
			<xsl:with-param name="value" select="number(count($top//lineCount[.!='0'])>0)" />
			<xsl:with-param name="max" select="1" />
		</xsl:call-template>
		<xsl:call-template name="linesSummary">
			<xsl:with-param name="top" select="$top" />
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template name="linesSummary">
		<xsl:param name="top" select="." />
		<xsl:call-template name="coverage">
			<xsl:with-param name="type" select="'block, %'" />
			<xsl:with-param name="value" select="count($top//lineCount[.!='0'])" />
			<xsl:with-param name="max" select="count($top//lineCount)" />
		</xsl:call-template>
		<xsl:call-template name="coverage">
			<xsl:with-param name="type" select="'line, %'" />
			<xsl:with-param name="value" select="count($top//lineCount[.!='0'])" />
			<xsl:with-param name="max" select="count($top//lineCount)" />
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template name="coverage">
		<xsl:param name="type"/>
		<xsl:param name="value"/>
		<xsl:param name="max"/>
		<xsl:element name="coverage">
			<xsl:attribute name="type">
				<xsl:value-of select="$type" />
			</xsl:attribute>
			<xsl:attribute name="value">
				<xsl:value-of select="format-number(100 * $value div $max,'0.00')" />
				<xsl:text>% (</xsl:text>
				<xsl:value-of select="$value" />
				<xsl:text>/</xsl:text>
				<xsl:value-of select="$max" />
				<xsl:text>)</xsl:text>
			</xsl:attribute>
		</xsl:element>
	</xsl:template>
</xsl:stylesheet>