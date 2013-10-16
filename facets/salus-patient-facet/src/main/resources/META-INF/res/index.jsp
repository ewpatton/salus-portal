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
		
		var patientData;
		
		// Graphing variable initializations:
		var margin = {top: 20, right: 20, bottom: 30, left: 50},
			width = 700 - margin.left - margin.right,
			height = 500 - margin.top - margin.bottom;
		var parseDate = d3.time.format("%Y-%m-%d").parse; // what is the format of the dates in the returned JSON object?
		
		var x = d3.time.scale()
			.range([0, width]);
		var y = d3.scale.linear()
			.range([height, 0]);
			
		var lineColor = d3.scale.category10();
		
		
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
			});// /draggable
			$( ".droppable" ).droppable({
				accept: ".draggable",
				activeClass: "droppable-active",
				drop: function( event, ui ) {
					activeGraph.push(theURI);
					var newListItem = '<li class="graph-legend"><a href=\"'+theURI+'\">'+theText+'</a></li>';
					$(this).find("ul").append(newListItem);
					//$.bbq.pushState({"characteristic":theURI});
					var theData = PatientModule.getPatientMeasurements({"characteristic":[theURI]}, getPatientDataCallback);
				}// /drop
			});// /droppable
		});// /function
		
		function getPatientDataCallback(data){
			console.log("patient data returned:");
			data=JSON.parse(data);
			patientData = data.results.bindings;
			activeGraph.push(patientData);
			console.log(patientData);
			// graph stuff!
			graphPatientData(patientData);
		}
			
		function graphPatientData(data){
			var xAxis = d3.svg.axis()
				.scale(x)
				.orient("bottom");
			var yAxis = d3.svg.axis()
				.scale(y)
				.orient("left");
				
			var line = d3.svg.line()
			    .interpolate("linear")
				.x(function(d) {
					return x(parseDate(d.date.value));
				})
				.y(function(d) {
					return y(d.value.value);
			    });
			
			var svg = d3.select("#map_canvas").append("svg")
				.attr("width", width + margin.left + margin.right)
				.attr("height", height + margin.top + margin.bottom)
				.append("g")
				.attr("transform", "translate(" + margin.left + "," + margin.top + ")");

			var data = typeof data === "string" ? JSON.parse(data) : data;

			x.domain(d3.extent(data, function(d) {
				return parseDate(d.date.value);
			}));
			y.domain(d3.extent(data, function(d) { return parseInt(d.value.value); }));
			
			svg.append("g")
				.attr("class", "x axis")
				.attr("transform", "translate(0," + height + ")")
				.call(xAxis);
			svg.append("g")
				.attr("class", "y axis")
				.call(yAxis)
				.append("text")
				.attr("transform", "rotate(-90)")
				.attr("y", 6)
				.attr("dy", ".71em")
				.style("text-anchor", "end")
				.text("Value");
			svg.append("path")
			    .attr("stroke", "#000")
			    .attr("fill", "none")
			    .data([data]).attr("d", line);
		}// /graphPatientData
		
		
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
          <div id="display" style="position:relative;">
			<div id="drop-legend" style="float:left;height:500px;width:200px;" class="droppable">
				<p>Drop Characteristics here to graph:</p>
				<ul></ul>
			</div>
            <div id="map_canvas" style="float:center"></div>
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
