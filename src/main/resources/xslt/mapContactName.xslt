<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/">
        <Person xmlns="http://guidewire.com/cc/gx/PersonModel.gx">
            <FirstName>
                <xsl:value-of select="//*[contains(local-name(),'_firstName')]"/>
            </FirstName>
            <LastName>
                <xsl:value-of select="//*[substring(name(), string-length(name()) - 8) = '_lastName']"/>
            </LastName>
            <PublicID>
                <xsl:value-of select="//*[substring(name(), string-length(name()) - 8) = '_publicID']"/>
            </PublicID>
        </Person>
    </xsl:template>

</xsl:stylesheet>