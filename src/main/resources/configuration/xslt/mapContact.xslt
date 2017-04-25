<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/">
        <Contact xmlns="http://guidewire.com/cc/gx/com.businessagility.agilebridge.gx.contactmodel">
            <entity-Person>
                <FirstName>
                    <xsl:value-of select="//*[contains(local-name(),'_firstName')]"/>
                </FirstName>
                <LastName>
                    <xsl:value-of select="//*[contains(local-name(), '_lastName')]"/>
                </LastName>
            </entity-Person>
            <PublicID>
                <xsl:value-of select="//*[contains(local-name(),'_publicID')]"/>
            </PublicID>
            <WorkPhone>
                <xsl:value-of select="//*[contains(local-name(),'_phoneNumber')]"/>
            </WorkPhone>
        </Contact>
    </xsl:template>

</xsl:stylesheet>