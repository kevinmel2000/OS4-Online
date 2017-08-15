<?xml version="1.0"?>
<xsl:stylesheet xmlns:t="jabber:client" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:template match="messages">
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1"/>
            <link rel="stylesheet" type="text/css" href="css/framework.css" media="all"/>
            <script type="text/javascript">
                function callActivity(jid){
                android.callActivity(jid);
                }
            </script>
        </head>
        <body>
        <div data-role="page" id="pageone">

        <div id="main" data-role="main" data-theme="a">
        <!--h4>Contacts</h4-->
        <form class="ui-filterable" style="margin:10px;">
            <input id="myFilter" data-type="search" placeholder="Search for names.."/>
        </form>
        <ul data-role="listview" data-filter="true" data-input="#myFilter" data-autodividers="false" style="margin: 0 10px;">

        <xsl:for-each select="t:message">
            <xsl:choose>
            <xsl:when test="(position() mod 2) = 1">
            <li class="self">
                <div class="messages">
                    <a href="#" class="hb-thumbnail floatleft"><img src="images/48.png" style="width:48px;hight:48px;border-radius: 100%;"/></a>
                    <span class="title"><a href="profil.html"><xsl:value-of select="@from"/></a></span>
                    <p><xsl:value-of select="@from"/></p>
                    <time datetime="2009-11-13T20:00"><xsl:value-of select="@from"/></time>
                </div>
            </li>
            </xsl:when>
            <xsl:otherwise>
            <li class="other">
                <div class="messages">
                    <a href="#" class="hb-thumbnail floatleft"><img src="images/48.png" style="width:48px;hight:48px;border-radius: 100%;"/></a>
                    <span class="title"><a href="profil.html"><xsl:value-of select="@from"/></a></span>
                    <p><xsl:value-of select="@from"/></p>
                    <time datetime="2009-11-13T20:00"><xsl:value-of select="@from"/></time>
                </div>
            </li>
            </xsl:otherwise>
            </xsl:choose>
        </xsl:for-each>

        </ul>
        </div>
        </div>
        </body>
        </html>
    </xsl:template>
</xsl:stylesheet>