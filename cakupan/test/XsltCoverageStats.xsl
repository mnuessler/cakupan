<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:xdt="http://www.w3.org/2005/xpath-datatypes">
	<xsl:output method="html" encoding="UTF-8" indent="yes"/>
	<xsl:include href="common.xsl"/>
	<xsl:template match="/">
		<html>
			<head>
				<title>XSLT Coverage Report</title>
				<link title="Style" type="text/css" rel="stylesheet" href="main.css"/>
			</head>
			<body>
				<h5>XSLT Coverage Report - All Files </h5>
				<xsl:variable name="stats0Percentage" select="round(10000 * number(count(//lineCount[. != '0'])) div number(count(//lineCount))) div 100"/>
				<xsl:variable name="statsTotal" select="count(//lineCount)"/>
				<xsl:variable name="stats0" select="count(//lineCount[. = '0'])"/>
				<xsl:variable name="greenBarPerc">
					<xsl:value-of select="format-number($stats0Percentage,'##.###')"/>
				</xsl:variable>
				<h3>line matches: <xsl:value-of select="$statsTotal - $stats0"/> (<xsl:value-of select="format-number($stats0Percentage,'##.###')"/>%) | no line matches: <xsl:value-of select="$stats0"/> (<xsl:value-of select="format-number(100 - $stats0Percentage,'##.###')"/>%)</h3>
				<xsl:text> </xsl:text>
				<table class="report" id="packageResults">				
				<thead>
					<tr>
						<td class="heading">XSLT file</td>
						<td class="heading"># Lines</td>
						<td class="heading">Line Coverage</td>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>
							<b>All XSLTs (<xsl:value-of select="count(//entry)"/>)</b>
						</td>
						<td class="value">
							<xsl:value-of select="$statsTotal"/></td>
						<td>
							<table cellpadding="0px" cellspacing="0px" class="percentgraph">
								<tr class="percentgraph">
									<td align="right" class="percentgraph" width="40">
										<xsl:value-of select="format-number($stats0Percentage,'##.###')"/>%</td>
									<td class="percentgraph">
										<div class="percentgraph">
											<div class="greenbar" style="width: {$greenBarPerc}px">
												<span class="text">
													<xsl:value-of select="$statsTotal - $stats0"/>/<xsl:value-of select="$statsTotal"/>
												</span>
											</div>
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<xsl:apply-templates select="*/entry"/>
				</tbody>
				</table>
			</body>
		</html>
	</xsl:template>
	<xsl:template match="entry">
		<xsl:variable name="perc">
			<xsl:call-template name="calcPerc">
				<xsl:with-param name="count" select="number(count(*/line/*//lineCount[.!='0']))"/>
				<xsl:with-param name="total" select="number(count(*/line/*//lineCount))"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="total" select="count(*/line/*//lineCount)"/>
		<xsl:variable name="totOk" select="count(*/line/*//lineCount[.!='0'])"/>
		<xsl:variable name="uri" select="concat(string/text(),'.html')"/>
		<xsl:variable name="percentage">
			<xsl:choose>
				<xsl:when test="$total - $totOk = $total">0</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="format-number($perc,'##')"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="greenBarPerc">
			<xsl:value-of select="$percentage"/>
		</xsl:variable>
		<tr>
			<td>
				<a href="{$uri}">
					<xsl:value-of select="string/text()"/>
				</a>
					<i>
					<xsl:text> (</xsl:text>
					<xsl:value-of select="$percentage"/>%
					<xsl:text>)</xsl:text>
					</i>
			</td>
			<td class="value">
				<xsl:value-of select="count(*/line/*/lineNumber)"/>
			</td>
			<td>
				<table cellpadding="0px" cellspacing="0px" class="percentgraph">
					<tr class="percentgraph">
						<td align="right" class="percentgraph" width="40">
							<xsl:value-of select="$percentage"/>%</td>
						<td class="percentgraph">
							<div class="percentgraph">
								<div class="greenbar" style="width: {$greenBarPerc}px">
									<span class="text">
										<xsl:value-of select="$totOk"/>/<xsl:value-of select="$total"/>
									</span>
								</div>
							</div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</xsl:template>
</xsl:stylesheet>
