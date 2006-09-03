<?xml version="1.0" encoding="UTF-8" ?>
<!--
  XMLに適用する場合の注意点
  
    1. XMLの先頭に以下の記述をしてください。
        <?xml version="1.0" encoding="UTF-8" ?>
        <?xml-stylesheet href="YahooSearchResult.xsl" type="text/xsl" ?>
   
    2. ResultSet の xmlns="urn:yahoo:srch" という属性を削除してください。
  
  上記の対応をしたXMLをIEで開くと、XMLデータをテーブルで一覧できます。
 -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output encoding="UTF-8" indent="yes"/>

<xsl:template match="/">
    <html>
        <head>
            <title>Yahoo!Webサービス Web検索結果</title>
        </head>
        <body>
            <h1>Yahoo!Webサービス Web検索結果</h1>
            <table border="1">
                <tr bgcolor="#6699FF">
                    <th>Title</th>
                    <th>Url</th>
                    <th>Summary</th>
                </tr>
                <xsl:apply-templates/>
            </table>
        </body>
    </html>
</xsl:template>

<xsl:template match="Result">
    <tr>
        <td>
            <xsl:element name="a">
                <xsl:attribute name="href"><xsl:value-of select="ClickUrl"/></xsl:attribute>
                <xsl:attribute name="target">_blank</xsl:attribute>
                <xsl:value-of select="Title"/>
            </xsl:element>
            <br/><br/>
            <xsl:element name="a">
                <xsl:attribute name="href"><xsl:value-of select="Cache/Url"/></xsl:attribute>
               <xsl:attribute name="target">_blank</xsl:attribute>
                [キャッシュ]
            </xsl:element>
        </td>
        <td><xsl:value-of select="Url"/></td>
        <td><xsl:value-of select="Summary"/></td>
    </tr>
</xsl:template>

</xsl:stylesheet>

