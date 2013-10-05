<%@ taglib uri="/WEB-INF/semanteco-core.tld" prefix="core" %>
<%@ taglib uri="/WEB-INF/semanteco-module.tld" prefix="module" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <title>Salus MobileHealth Portal</title>
    <core:styles />
    <module:styles />
	<style>
		.droppable{
			border-style:solid;
			border-width:1px;
			border-color:black;
		}
		.droppable-active{
			border-style:solid;
			border-width:1px;
			border-color:#888;
		}
		.graph-legend{
		}
		.axis path, .axis line {
			fill: none;
			stroke: #000;
			shape-rendering: crispEdges;
		}
		.x.axis path {
			display: none;
		}
		.line {
			fill: none;
			<!--stroke: steelblue;	//we'll be using a bunch of different colors for lines. -->
			stroke-width: 1.5px;
		}
	</style>
    <core:scripts />
    <module:scripts />
	<script>
		// Will hold the list of characteristics 
		var activeGraph = [];
		
		var lineColor = d3.scale.category10();
		var patientData;
		
		$(function() {
			var theText, theURI;
			$( ".draggable" ).draggable({ 
				opacity: 0.7,  
				revert: "invalid",
				start: function (event, ui) {
					theText = $(this).text();
					theURI = $(this).attr('href');
				},
				helper: "clone"
					//function( event, ui ) {
					//return $( "<a href=\"" + theURI + "\">"+ theText + "</a>" );
			});// /draggable
			$( ".droppable" ).droppable({
				accept: ".draggable",
				activeClass: "droppable-active",
				drop: function( event, ui ) {
					activeGraph.push(theURI);
					var newListItem = '<li class="graph-legend"><a href=\"'+theURI+'\">'+theText+'</a></li>';
					$(this).find("ul").append(newListItem);
					//$.bbq.pushState({"characteristic":theURI});
					var theData = PatientModule.getPatientMeasurements({"characteristic":theURI}, getPatientDataCallback);
				}// /drop
			});// /droppable
		});// /function
		
		function getPatientDataCallback(data){
			console.log("patient data returned:");
			data=JSON.parse(data);
			console.log(data);
			patientData=data.results.bindings;
			// graph stuff!
		}
		
		
	</script>
  </head>
  <body onload="SemantEco.initialize()">
        <div id="header">
          <div class="header-text">
            <img src="images/header.png" alt="Salus" />
          </div>
        </div>
        <div id="content">
          <div id="sidebar" class="sidebar">
            <div class="search button float">Search</div>
            <div id="facets" style="float:right;width:20%">
              <module:facets />
            </div>
            <div class="search button">Search</div>
          </div>
          <div id="display">
			<div id="drop-legend" style="height:500px;width:200px;" class="droppable">
				<p>Drop Characteristics here to graph:</p>
				<ul></ul>
			</div>
            <div id="map_canvas"></div>
            <div id="page">&nbsp;</div>
          </div>
          <div class="clear" style="clear:both;"></div>
          <div id="footer"><a href="about.html">About</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href="privacy.html">Privacy</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href="contact.html">Contact</a></div>
          <div id="spinner"><img src="images/spinner.gif" alt="Processing..."/><br/>Processing your request...</div>
          <div id="sparql2"><div></div></div>
          <div id="sparql"><div id="sparql-content"></div><div class="big"><a onclick="javascript:hideSparql();">Close this window</a></div></div>
        </div>
		
        <div class="lightbox" style="display:none;">
            <div class="lb_shadow">
                <div class="positioner">
                    <div class="lb_container" style="display:none;">
                        <div class="lb_loading"><img src="images/spinner.gif" alt="Processing..."/><br/>Processing your request...</div>
                        <div class="lb_closebutton"></div>
                        <div class="clear"></div>
                        <div class="lb_content" id="id_lb_content"></div>
                    </div>
                </div>
            </div>
        </div>
</body>
</html>
