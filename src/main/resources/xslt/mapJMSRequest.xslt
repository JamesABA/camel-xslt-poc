<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/">
        <Person xmlns="http://guidewire.com/cc/gx/PersonModel.gx">
            <FirstName>
                <xsl:value-of select="//*[contains(local-name(),'irstName')]"/>
            </FirstName>
            <LastName>
                <xsl:value-of select="//*[substring(name(), string-length(name()) - 6) = 'astName']"/>
            </LastName>
            <PublicID>
                <xsl:value-of select="//*[substring(name(), string-length(name()) - 6) = 'ublicID']"/>
            </PublicID>
        </Person>
    </xsl:template>

</xsl:stylesheet>