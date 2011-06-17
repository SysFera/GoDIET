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
		<xsl:variable name="currentDomain" select="@label" />
		subgraph cluster<xsl:value-of select="@label" />
		{
		node
		[fontsize=10,shape=none,image="/home/phi/Dev/GoDIET/shell/src/test/resources/infrastructure/fig/node.png"];
		color=grey
		<xsl:for-each select="//node">
			<xsl:if test="ssh/@domain = $currentDomain">
				"<xsl:value-of select="@id" />"
				<xsl:text>
		</xsl:text>
			</xsl:if>
		</xsl:for-each>
		label = "<xsl:value-of select="@label" />";
		}
	</xsl:template>

	<xsl:template match="//link[@fromDomain]">
		
			<!-- Get one node of this fromDomain -->
			<xsl:call-template name="arrowBuilder">
			<xsl:with-param name="linkDomain" select="@fromDomain"></xsl:with-param>
			<xsl:with-param name="to" select="@to"></xsl:with-param>
			</xsl:call-template>
	</xsl:template>
	<xsl:template match="//link[@from]">
		
			<!-- Get one node of this fromDomain -->
			<xsl:call-template name="arrowBuilder">
			<xsl:with-param name="linkFrom" select="@from"></xsl:with-param>
			<xsl:with-param name="to" select="@to"></xsl:with-param>
			</xsl:call-template>
	</xsl:template>
		
	<xsl:template name="arrowBuilder">
		  <xsl:param name="linkDomain"></xsl:param>
		  <xsl:param name="linkFrom"></xsl:param>
		  <xsl:param name="to"></xsl:param>
		  <xsl:for-each select="//node">
			<xsl:if test="ssh/@domain = $linkDomain ">
				"<xsl:value-of select="@id"/>"->"<xsl:value-of select="$to"/>"  [ltail=cluster<xsl:value-of select="$linkDomain"/>];
			</xsl:if>
			<xsl:if test="@id = $linkFrom ">
				"<xsl:value-of select="@id"/>"->"<xsl:value-of select="$to"/>";
			</xsl:if>
		  </xsl:for-each>
	</xsl:template>
	<!-- <xsl:template match="node"> <xsl:value-of select="@id" /> -> <xsl:value-of 
		select="following-sibling::gateway/@id" /> [dir="both"] </xsl:template> <xsl:template 
		match="link"> <xsl:value-of select="@from" /> -> <xsl:value-of select="@to" 
		/> </xsl:template> <xsl:template match="gateway"> <xsl:value-of select="@id" 
		/> [shape=none,image="/home/phi/Dev/GoDIET/shell/src/test/resources/platform/fig/gateway.png"]; 
		</xsl:template> -->
</xsl:stylesheet>
