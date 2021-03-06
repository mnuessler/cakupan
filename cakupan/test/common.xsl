<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:xdt="http://www.w3.org/2005/xpath-datatypes">
	<xsl:template name="calcPerc">
		<xsl:param name="count"/>
		<xsl:param name="total"/>
		<xsl:value-of select="round(10000 * $count div $total) div 100"/>
	</xsl:template>
</xsl:stylesheet>
