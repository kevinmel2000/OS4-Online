<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ecb="jabber:iq:roster">
    <xsl:output encoding="utf-8" indent="no"/>
    <xsl:template match="iq">
	<html>
		<head>
		<title>Pajubel.com</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
		<link rel="stylesheet" type="text/css" href="css/framework.css" media="all"/>
		<!--<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>-->
		<script src="js/jquery.min.js"></script>
		<script type="text/javascript">
			function callActivity(jid){
				android.callActivity(jid);
			}
		</script>	  
		</head>
		<body>
	   		<div id="main" role="main">	
	     		<div class="hb-container">
	     			<!--<div class="ads">PUT YOUR ADS HERE</div>-->
					<div class="hb-grid">
						<section id="list-line" class="hb-span-12">
							<div class="widget w-box recents-widget">
								<div class="panel">
									<form class="form-search">
										<input class="form-input" placeholder="Search contact..."/>
									</form>
								</div>
								<ul class="contact-list">

						        <xsl:for-each select="ecb:query">
						            <xsl:for-each select="ecb:item">
						                <xsl:choose>
						                <xsl:when test="(position() mod 2) = 1">
											<li onclick="callActivity('senopati@os4-it.com')">
												<img class="online" src="images/48.png"/>
												<div class="contact-info">
													<h3 class="heading--name"><xsl:value-of select="@name"/></h3>
													<div class="contact-item"><i class="fa fa-map-marker"></i><xsl:value-of select="@jid"/></div>
													<div class="contact-item"><i class="fa fa-briefcase"></i><xsl:value-of select="@subscription"/></div>
												</div>												
											</li>
						                </xsl:when>
						                <xsl:otherwise>
											<li onclick="callActivity('senopati@os4-it.com')">
												<img class="offline" src="images/48.png"/>
												<div class="contact-info">
													<h3 class="heading--name"><xsl:value-of select="@name"/></h3>
													<div class="contact-item"><i class="fa fa-map-marker"></i> Bandung</div>
													<div class="contact-item"><i class="fa fa-briefcase"></i> 7.45 pm</div>
												</div>												
											</li>
						                </xsl:otherwise>
						                </xsl:choose>
						            </xsl:for-each>
						        </xsl:for-each>
								
								</ul>
							</div>							
						</section>
					</div><!--.hb-grid -->
				</div><!-- .container-->
			</div><!-- #main -->			
		</body>
	</html>
    </xsl:template>
</xsl:stylesheet>
