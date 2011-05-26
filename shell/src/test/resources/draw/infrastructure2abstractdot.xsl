<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="text" omit-xml-declaration="yes" />
	<xsl:template match="/">
		digraph G {
		compound = true;
		<xsl:apply-templates />
		}
	</xsl:template>

	<xsl:template match="domain">

		"<xsl:value-of select="@id"/>" [fontsize=10,shape=ellipse];
	
	</xsl:template>
		
	<xsl:template match="storage"> 
		<xsl:variable name="currentStorageId" select="@id"/>
		"<xsl:value-of select="$currentStorageId" />" 	[fontsize=10,shape=none,image="/home/phi/Dev/GoDIET/shell/src/test/resources/infrastructure/fig/gateway.png"];
		<xsl:for-each select="ssh">
			"<xsl:value-of select="$currentStorageId" />" -> "<xsl:value-of select="@domain"/>"
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="node"> 
		<xsl:variable name="currentNodeId" select="@id"/>
		"<xsl:value-of select="$currentNodeId" />" 	[fontsize=10,shape=none,image="/home/phi/Dev/GoDIET/shell/src/test/resources/infrastructure/fig/node.png"];
		<xsl:for-each select="ssh">
			"<xsl:value-of select="$currentNodeId" />" -> "<xsl:value-of select="@domain"/>"
		</xsl:for-each>
		<xsl:for-each select="disk">
			"<xsl:value-of select="$currentNodeId" />" -> "<xsl:value-of select="@ref"/>" [style=dotted]
		</xsl:for-each>
	</xsl:template>
	
	
	
	<xsl:template match="//link[@fromDomain]">
	 	"<xsl:value-of select="@fromDomain"/>"->"<xsl:value-of select="@to"/>" [color=".7 .3 1.0"];
	</xsl:template>
	<xsl:template match="//link[@from]">
		"<xsl:value-of select="@from"/>"->"<xsl:value-of select="@to"/>" [style=dotted,color=".7 .3 1.0"];
	</xsl:template>
		
		
		<xsl:template match="//link[@toDomain]">
	 	"<xsl:value-of select="@from"/>"->"<xsl:value-of select="@toDomain"/>" [color=".7 .3 1.0"];
	</xsl:template>

</xsl:stylesheet>
