<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/">
        <pn:Person xmlns:pn="http://guidewire.com/cc/gx/PersonModel.gx">
            <pn:FirstName>
                <xsl:value-of select="//*[contains(local-name(),'irstName')]"/>
            </pn:FirstName>
            <pn:LastName>
                <xsl:value-of select="//*[substring(name(), string-length(name()) - 6) = 'astName']"/>
            </pn:LastName>
            <pn:PublicID>
                <xsl:value-of select="//*[substring(name(), string-length(name()) - 6) = 'ublicID']"/>
            </pn:PublicID>
        </pn:Person>
    </xsl:template>

</xsl:stylesheet>