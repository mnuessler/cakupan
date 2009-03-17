<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.topografix.com/GPX/1/0">
	<xsl:template match="/">
			<xsl:apply-templates select="*[local-name()='gpx']"/>
	</xsl:template>
	<xsl:template match="*[local-name()='gpx']">
		<xsl:element name="nl.lin.googlemaps.model.GPX">
			<xsl:element name="metadata">
				<xsl:attribute name="class">nl.lin.googlemaps.model.Metadata</xsl:attribute>
				<xsl:apply-templates select="*[local-name()='time']"/>
				<xsl:apply-templates select="*[local-name()='bounds']"/>
			</xsl:element>
			<xsl:element name="points">
				<xsl:attribute name="class">list</xsl:attribute>
				<!--xsl:apply-templates select="*[local-name()='trk']/*[local-name()='trkseg']/*[local-name()='trkpt'] | *[local-name()='wpt']"/-->
				<xsl:apply-templates select="*[local-name()='trk']"/>
			</xsl:element>
		</xsl:element>
	</xsl:template>
	<xsl:template match="*[local-name()='trk']">
		<xsl:apply-templates select="*[local-name()='trkseg']"/>
	</xsl:template>
	<xsl:template match="*[local-name()='trkseg']">
		<xsl:apply-templates select="*[local-name()='trkpt']"/>
	</xsl:template>
	<xsl:template match="*[local-name()='time']">
		<time>
			<xsl:value-of select="text()"/>
		</time>
	</xsl:template>
	<xsl:template match="*[local-name()='bounds']">
		<xsl:element name="bounds">
			<xsl:attribute name="class">nl.lin.googlemaps.model.Bounds</xsl:attribute>
			<minLat>
				<xsl:value-of select="@minlat"/>
			</minLat>
			<maxLat>
				<xsl:value-of select="@maxlat"/>
			</maxLat>
			<minLon>
				<xsl:value-of select="@minlon"/>
			</minLon>
			<maxLon>
				<xsl:value-of select="@maxlon"/>
			</maxLon>
		</xsl:element>
	</xsl:template>
	<xsl:template match="*[local-name()='wpt' or local-name()='trkpt']">
	<xsl:element name="nl.lin.googlemaps.model.Point">
		<lon>
			<xsl:value-of select="@lon"/>
		</lon>
		<lat>
			<xsl:value-of select="@lat"/>
		</lat>
		<xsl:apply-templates select="*[local-name()='time']"/>
		<xsl:apply-templates select="*[local-name()='ele' or local-name()='geoidheight']"/>
		<xsl:apply-templates select="*[local-name()='speed']"/>
		<xsl:apply-templates select="*[local-name()='desc']"/>
		</xsl:element>
	</xsl:template>
	<xsl:template match="*[local-name()='ele' or local-name()='geoidheight']">
		<height>
			<xsl:value-of select="text()"/> 
		</height>
	</xsl:template>
	<xsl:template match="*[local-name()='speed']">
		<speed>
			<xsl:value-of select="text()"/> 
		</speed>
	</xsl:template>
	<xsl:template match="*[local-name()='desc']">
		<distance>
			<xsl:value-of select="substring-after(text(),'Distance from Start:')"/> 
		</distance>
	</xsl:template>
		
</xsl:stylesheet>
