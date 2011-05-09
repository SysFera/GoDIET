<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="text" omit-xml-declaration="yes" />
	<xsl:template match="/">
		digraph G {
		<xsl:apply-templates />
		}
	</xsl:template>

	<xsl:template match="domain">
		subgraph cluster<xsl:value-of select="@label" /> {
		node [fontsize=10,shape=none,image="/home/phi/Dev/GoDIET/shell/src/test/resources/platform/fig/node.png"];
		color=blue
		
		<xsl:apply-templates />
		label = "<xsl:value-of select="@label" />";
		}
	</xsl:template>

	<xsl:template match="node">
	
		<xsl:value-of select="@id" />-><xsl:value-of select="following-sibling::gateway/@id" /> [dir="both"]
		
	</xsl:template>
	<xsl:template match="link">
		<xsl:value-of select="@from" />-><xsl:value-of select="@to" />
	</xsl:template>
	<xsl:template match="gateway">
		<xsl:value-of select="@id" /> [shape=none,image="/home/phi/Dev/GoDIET/shell/src/test/resources/platform/fig/gateway.png"];
		

	</xsl:template>
	
</xsl:stylesheet>
