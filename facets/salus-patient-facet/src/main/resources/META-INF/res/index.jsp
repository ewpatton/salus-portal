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
		.ui-draggable-helper{
			z-index: 100;
			border: 1px solid #000;
			padding: 6px;
			background: #fff;
			font-size: 1.2em;
		}
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
	</style>
    <core:scripts />
    <module:scripts />
	<script src="http://code.highcharts.com/stock/highstock.js"></script>
	<script>
		// Will hold the list of characteristics 
		var activeGraph = [];
		var patientData;
		var chart;
		
		// default chart
		$(function(){
			chart = new Highcharts.StockChart({
					chart: {
						renderTo: 'map_canvas'
					},
					plotBorderWidth: 2,
					rangeSelector : {
						selected : 1
					},
					title : {
						text : 'Patient Blood Data'
					},
					subtitle: {
						text: 'drag and drop characteristics to add to the chart',
					},
					tooltip : {
						shared: true/*,
						formatter: function() {
							var s = [];
							$.each(this.points, function(i, point) {
								s.push('<span>'+ point.series.name +' '+
								point.y + point.series.unit + '<span>');
							});
							return s.join('\n');
						}*/// /formatter function
					},
					series : [{
						type: 'flags',
						name: 'Placeholder flags',
						data: [{
							x: Date.UTC(2013, 7, 14),
							title: 'Placeholder'
						}, {
							x: Date.UTC(2013, 8, 1),
							title: 'Placeholder'
						}, {
							x: Date.UTC(2013, 8, 15),
							title: 'Placeholder'
						}, {
							x: Date.UTC(2013, 9, 28),
							title: 'Placeholder'
						}],
						shape: 'squarepin'
					}]// /series
			});// /new chart
			var theText, theURI;
			$( ".draggable" ).draggable({ 
				opacity: 0.7,  
				revert: "invalid",
				helper: "clone",
				start: function (event, ui) {
					theText = $(this).text();
					theURI = $(this).attr('href');
					$(ui.helper).addClass("ui-draggable-helper");
				}
			});// /draggable
			$( ".droppable" ).droppable({
				accept: ".draggable",
				activeClass: "droppable-active",
				drop: function( event, ui ) {
					chart.showLoading();
					var theData = PatientModule.getPatientMeasurements({"characteristic":[theURI]}, getPatientDataCallback);
				}// /drop
			});// /droppable
		});// /function
		
		function getPatientDataCallback(data){
			console.log("patient data returned:");
			data=JSON.parse(data);
			patientData = data.results.bindings;
			console.log(patientData);
			// graph stuff!
			graphPatientData(patientData);
		}
		
		function parseToSeries(theData){
			var newSeries = []; 
			var seriesData = [];
			newSeries.name = theData[0].label.value;
			units = theData[0].unit.value.split("#")[1];
			activeGraph.push([newSeries.name,units]);
			$.each(theData, function(index,dataEntry){
				if(theData[index].unit.value.split("#")[1] == units){
					seriesData.push([Date.parse(theData[index].date.value), parseFloat(theData[index].value.value)]);
				}
			});// /each
			newSeries.data = seriesData;
			return newSeries;
		}// /parseToSeries
		
		function graphPatientData(theData){
			theSeries = parseToSeries(theData);
			chart.addSeries(theSeries);
			chart.hideLoading();
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
			<div id="map_canvas" class="droppable" style="float:center; height: 500px; min-width: 500px;"></div>
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
