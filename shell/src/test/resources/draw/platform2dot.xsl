<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="text" omit-xml-declaration="yes" />
	<xsl:template match="/">
		digraph G {
		<xsl:apply-templates />
		}
	</xsl:template>
	<!-- SERVICES -->
	<xsl:template match="dietServices">
		subgraph clusterServices {
		node [fontsize=10,shape=circle];
		color=grey

		<xsl:apply-templates />
		label = "Services";
		}
	</xsl:template>

	<xsl:template match="omniNames">

		<xsl:value-of select="@id" />
		->
		<xsl:value-of select="child::config/@server" />

	</xsl:template>


	<!-- Infras -->
	<xsl:template match="dietInfrastructure">
		subgraph clusterdietInfrastructure {
		node [fontsize=10,shape=ellipse];
		color=grey

		<xsl:apply-templates />
		label = "Platform";
		}
	</xsl:template>
	<xsl:template match="masterAgent">
		<xsl:value-of select="@id" />
		->
		<xsl:value-of select="child::config/@server" />
		<xsl:apply-templates />

	</xsl:template>
	<xsl:template match="localAgent">
		<xsl:value-of select="@id" />
		->
		<xsl:value-of select="child::config/@server" />

	</xsl:template>
	<xsl:template match="sed">
		<xsl:value-of select="@id" />
		->
		<xsl:value-of select="child::config/@server" />
	</xsl:template>
</xsl:stylesheet>
