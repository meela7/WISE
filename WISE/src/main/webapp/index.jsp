<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
	if (request.getProtocol().equals("HTTP/1.1"))
		response.setHeader("Cache-Control", "no-cache");
%>
<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html ng-app="wiseApp">
<head>
<!-- Responsive Design -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title> WISE - Weather Information Service Engine </title>

<script	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
<script	src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/js/bootstrap.min.js"></script>

<script src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.5/angular.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.5/angular-route.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.5/angular-resource.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.5/angular-cookies.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.5/angular-animate.min.js"></script>

<!-- UI BootStrap -->
<script src="https://angular-ui.github.io/bootstrap/ui-bootstrap-tpls-1.1.2.js"></script>

<!-- Angular - Leaflet Directive -->
<link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.7.5/leaflet.css">
<script src="http://cdn.leafletjs.com/leaflet-0.7.5/leaflet.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/angular-leaflet-directive/0.10.0/angular-leaflet-directive.js"></script>

<!-- Leaflet Marker Cluster -->
<link href="https://cdnjs.cloudflare.com/ajax/libs/leaflet.markercluster/0.4.0/MarkerCluster.css" rel="stylesheet">
<link href="https://cdnjs.cloudflare.com/ajax/libs/leaflet.markercluster/0.4.0/MarkerCluster.Default.css" rel="stylesheet">
<script src="https://cdnjs.cloudflare.com/ajax/libs/leaflet.markercluster/0.4.0/leaflet.markercluster.js"></script>

<!-- DatePicker -->
<script	src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.11.2/moment.min.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.37/css/bootstrap-datetimepicker.min.css">
<script	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.37/js/bootstrap-datetimepicker.min.js"></script>
	
<!-- Smart Table -->
<script	src="https://cdnjs.cloudflare.com/ajax/libs/angular-smart-table/2.1.7/smart-table.js"></script>

<!-- Fusion Chart -->
<script	src="resources/js/fusioncharts/fusioncharts.js"></script>
<script	src="resources/js/fusioncharts/angular-fusioncharts.min.js"></script>
<link href="resources/css/wise.css" rel="stylesheet">
<script src="resources/js/wise.js"></script>

<!-- Multi Select -->
<script src="resources/js/angular-bootstrap-multiselect.js"></script>

<!-- LightBOx - Image Gallery (Not Used) 
<script src="resources/js/angular-bootstrap-lightbox.min.js"></script>
<link href="resources/css/angular-bootstrap-lightbox.min.css" rel="stylesheet">
-->

</head>
<body>

	<div class="navbar-wrapper">
		<div class="container">
			<nav class="navbar navbar-inverse"> <!-- and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target="#myNavBar">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#/"> WISE </a>
			</div>
			<div class="collapse navbar-collapse" id="myNavBar">
				<ul class="nav navbar-nav">
					<li class="dropdown"><a href="" class="dropdown-toggle"
						data-toggle="dropdown"><span class="glyphicon glyphicon-list"></span>
							Stream 관리 </a>
						<ul class="dropdown-menu inverse">
							<!-- <li><a href="#tag-list"> Tag </a></li>
							<li><a href="#meta-list"> Metadata</a></li> -->							
							<li><a href="#stream-list"> Stream</a></li>
							<li><a href="#log-list"> Log</a></li>
							<li><a href="#sensor-list"> Sensor</a></li>
						</ul>
					</li>
					<li class="dropdown"><a href="" class="dropdown-toggle"
						data-toggle="dropdown"><span class="glyphicon glyphicon-list"></span>
							MetaData Management </a>
						<ul class="dropdown-menu inverse">				
							<!-- <li><a href="#fish-list"> Fish</a></li> -->
							<li><a href="#site-list"> Site</a></li>
							<!-- <li><a href="#river-list"> River</a></li> -->
							<li><a href="#variable-list"> Variable</a></li>
							<li><a href="#unit-list"> Unit</a></li>
							<li><a href="#instrument-list"> Sensor </a></li>
							<li><a href="#model-list"> Prediction</a></li>
							<li><a href="#source-list"> Source</a></li>
							<li><a href="#dataSet-list"> DataSet</a></li>
						</ul></li>

					
					<li><a href="#/site-map"> <span
							class="glyphicon glyphicon-globe"></span> Site Map
					</a>
					</li>				
					<li><a href="#api"> <span
							class="glyphicon glyphicon-list-alt"></span> API 문서
					</a>
					</li>

				</ul>
				<ul class="nav navbar-nav navbar-right">
					
					<li><a href="#"><span class="glyphicon glyphicon-log-in"></span>
							Login</a></li>
				</ul>
			</div>
			</nav>
		</div>
	</div>
	<div class="container">
		<div ng-view></div>
	</div>
	<script src="resources/js/app.js"></script>
	<script src="resources/js/controller.js" ></script>
	<script src="resources/js/service.js"></script>
	<script src="resources/js/filter.js"></script>
	<script src="resources/js/directive.js"></script>
	

	<footer class="container-fluid text-center">
	  <p> Copyright 2016 by <a href="http://www.cilaboratory.org" title="Visit CI Lab">CI Lab</a>. All Rights Reserved</p>		
	</footer>

</body>
</html>