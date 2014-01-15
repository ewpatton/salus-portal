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
	<script src="js/highstock.js"></script>
	<script>
		// Will hold the list of characteristics 
		var activeGraph = [];
		var patientData, regData;
		var chart;
		
		// default chart
		$(function(){
			chart = new Highcharts.StockChart({
					chart: {
						renderTo: 'map_canvas',
						zoomType: 'xy',
						pinchType: 'xy'
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
						shared: true,
						formatter: function() {
							var pointUnit;
							var date =  Highcharts.dateFormat("%Y-%m-%d", new Date(this.x));
							var s = '<span style="font-size:10px;">' + date + "</span><br/>";
							$.each(this.points, function(i, point) {
								var color = point.series.color;
								pointUnit = (findElement(activeGraph, "characteristic", point.series.name)).units;
								s += '<span style="color:'+color+'; font-weight:bold;">' + point.series.name +'</span> '+
									point.y + ' ' + pointUnit + '<br/>';
							});
							return s;
						}// /formatter function
					},
					xAxis : {
						endOnTick: true,
						maxPadding: 0.10,
						minPadding: 0.10
					},
					series : [{
						type: 'flags',
						name: 'Placeholder flags',
						data: [{
							x: Date.UTC(2013, 7, 19),
							title: 'steroids'
						},{
							x: Date.UTC(2013, 7, 20),
							title: 'steroids'
						},{
							x: Date.UTC(2013, 7, 27),
							title: 'surgery'
						},{
							x: Date.UTC(2013, 8, 3),
							title: 'steroids'
						},{
							x: Date.UTC(2013, 8, 10),
							title: 'steroids'
						},{
							x: Date.UTC(2013, 9, 28),
							title: 'appointment'
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
					var check = findElement(activeGraph, "characteristic", theText);
					if (check != null){
						console.log("Characteristic already in active graph");
						return;
					}
					console.log("Characteristic not in active graph, making server call....");
					chart.showLoading();
					var theData = PatientModule.getPatientMeasurements({"characteristic":[theURI]}, getPatientDataCallback);
				}// /drop
			});// /droppable
		});// /function
		
		function getPatientDataCallback(data){
			console.log("patient data returned:");
			data=JSON.parse(data);
			patientData = data.results.bindings;
			if(patientData.length == 0){
				console.log("No data returned!");
				chart.hideLoading();
				return;
			}
			console.log(patientData);
			// graph stuff!
			graphPatientData(patientData);
		}
		
		function addPlotBar(data){
			console.log("threshold data returned:");
			data=JSON.parse(data);
			regData = data.results.bindings;
			if(regData.length == 0){
				console.log("No threshold data returned!");
				chart.hideLoading();
				return;
			}
			var thresh = filterThresholds();
			console.log(thresh);
			var newBar = [];
			// from < to
			var a = parseFloat(thresh[0].limit.value)
			var b = parseFloat(thresh[1].limit.value)
			if(a < b){
				newBar.push({
					from: a,
					to: b,
					color: "#dddddd",
					label: "Normal Range"
				});
			}
			else if( a >= b){
				newBar.push({
					from: b,
					to: a,
					color: "#dddddd",
					label: "Normal Range"
				});
			}		
			else console.log("wat do?");
			console.log(newBar);
			chart.yAxis[0].addPlotBand(newBar[0]);
		}
		
		// NOTE 12/17: this method may become unnecessary when server-side filtering is implemented
		function filterThresholds(){
			var chara = activeGraph[(activeGraph.length)-1].uri.split("#")[1];
			var needed = [];
			$.each(regData, function(index,regEntry){
				if(regData[index].characteristic.value.split("#")[1] == chara){
					needed.push(regEntry);
				}
			});// /each
			return needed;
		}
			
		function parseToSeries(theData){
			var newSeries = []; 
			var seriesData = [];
			newSeries.name = theData[0].label.value;
			seriesURI = theData[0].characteristic.value;
			units = theData[0].unit.value.split("#")[1];
			$.each(theData, function(index,dataEntry){
				if(theData[index].unit.value.split("#")[1] == units){
					seriesData.push([Date.parse(theData[index].date.value), parseFloat(theData[index].value.value)]);
				}
			});// /each
			newSeries.data = seriesData;
			if(findElement(activeGraph,"units",units) == null){
				chart.addAxis({
					title: {
						text: units
					}
				});
			}
			activeGraph.push({
				uri: seriesURI,
				characteristic: newSeries.name,
				units: units,
				data: seriesData
			});
			return newSeries;
		}// /parseToSeries
		
		function findElement(arr, propName, propValue) {
			for (var i=0; i < arr.length; i++)
				if (arr[i][propName] == propValue)
					return arr[i];
			return null;
		}// /findElement
		
		function addMark(warning, xCoord, yCoord){
			var flag;
			if (warning)
				flag = "images/red-mark.png";
			else flag = "images/yellow-mark.png";
			chart.renderer.image(flag, xCoord, yCoord, 6, 21).add();
		}// /addMark
		
		function graphPatientData(theData){
			theSeries = parseToSeries(theData);
			chart.addSeries(theSeries);
			console.log("making server call for threshold values....");
			if(!regData){
				// NOTE 12/17: returns threshold data for ALL characteristics in the ontology
				var theReg = RegulationModule.getThresholds({"characteristic":[seriesURI]}, addPlotBar);
			}
			// remove "loading" overlay
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
