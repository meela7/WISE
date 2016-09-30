/**
 * @ S4RM System
 * @File Name: app.js
 * @Description: Angular JS Module using modules: ngResource, ngRoute(Angular
 *               JS) and ui.bootstrap (BootStrap)
 * 
 * @author Meilan Jiang
 * @since 2016.06.09
 * @version 1.0
 * 
 * Copyright(c) 2016 by CILAB All right reserved.
 */

var wiseApp = angular.module('wiseApp', [ 'ngResource', 'ngRoute', 'ui.multiselect',
		'ngAnimate', 'ui.bootstrap', 'smart-table', 'leaflet-directive', 'ng-fusioncharts']);
var resourceRoot = 'resources/templates';

// metadata management configuration
wiseApp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/', {				
		templateUrl : resourceRoot + '/home.jsp'
	}).when('/api', {				
		templateUrl : resourceRoot + '/api.jsp'
	}).when('/tag-list', {
		templateUrl : resourceRoot + '/tag/list.html',
		controller : 'TagListCtrl'
	}).when('/tag-detail/:id', {
		templateUrl : resourceRoot + '/tag/detail.html',
		controller : 'TagDetailCtrl'
	}).when('/tag-creation', {
		templateUrl : resourceRoot + '/tag/creation.html',
		controller : 'TagCreationCtrl'
	}).when('/meta-list', {
		templateUrl : resourceRoot + '/metadata/list.html',
		controller : 'MetadataListCtrl'
	}).when('/meta-detail/:id', {
		templateUrl : resourceRoot + '/metadata/detail.html',
		controller : 'MetadataDetailCtrl'
	}).when('/meta-creation', {
		templateUrl : resourceRoot + '/metadata/creation.html',
		controller : 'MetadataCreationCtrl'
	}).when('/sensor-list', {
		templateUrl : resourceRoot + '/sensor/list.html',
		controller : 'SensorListCtrl'
	}).when('/sensor-detail/:id', {
		templateUrl : resourceRoot + '/sensor/detail.html',
		controller : 'SensorDetailCtrl'
	}).when('/sensor-creation', {
		templateUrl : resourceRoot + '/sensor/creation.html',
		controller : 'SensorCreationCtrl'
	}).when('/stream-list', {
		templateUrl : resourceRoot + '/stream/list.html',
		controller : 'StreamListCtrl'
	}).when('/stream-detail/:id', {
		templateUrl : resourceRoot + '/stream/detail.html',
		controller : 'StreamDetailCtrl'
	}).when('/stream-creation', {
		templateUrl : resourceRoot + '/stream/creation.html',
		controller : 'StreamCreationCtrl'
	}).when('/log-list', {
		templateUrl : resourceRoot + '/log/list.html',
		controller : 'LogListCtrl'
	}).when('/log-detail/:id', {
		templateUrl : resourceRoot + '/log/detail.html',
		controller : 'LogDetailCtrl'
	}).when('/log-creation', {
		templateUrl : resourceRoot + '/log/creation.html',
		controller : 'LogCreationCtrl'
	}).	when('/site-map', {								//======M4 Metadata=====
		templateUrl : resourceRoot + '/site/map.html',
		controller : 'SiteMapCtrl'
	}).when('/entity-list', {
		templateUrl : resourceRoot + '/entity/list.html',
		controller : 'EntityListCtrl'
	}).when('/entity-detail/:id', {
		templateUrl : resourceRoot + '/entity/detail.html',
		controller : 'EntityDetailCtrl'
	}).when('/entity-creation', {
		templateUrl : resourceRoot + '/entity/creation.html',
		controller : 'EntityCreationCtrl'
	}).when('/source-list', {
		templateUrl : resourceRoot + '/source/list.html',
		controller : 'SourceListCtrl'
	}).when('/source-detail/:id', {
		templateUrl : resourceRoot + '/source/detail.html',
		controller : 'SourceDetailCtrl'
	}).when('/source-creation', {
		templateUrl : resourceRoot + '/source/creation.html',
		controller : 'SourceCreationCtrl'
	}).when('/site-list', {
		templateUrl : resourceRoot + '/site/list.html',
		controller : 'SiteListCtrl'
	}).when('/site-detail/:id', {
		templateUrl : resourceRoot + '/site/detail.html',
		controller : 'SiteDetailCtrl'
	}).when('/site-creation', {
		templateUrl : resourceRoot + '/site/creation.html',
		controller : 'SiteCreationCtrl'
	}).when('/river-list', {
		templateUrl : resourceRoot + '/river/list.html',
		controller : 'RiverListCtrl'
	}).when('/river-detail/:id', {
		templateUrl : resourceRoot + '/river/detail.html',
		controller : 'RiverDetailCtrl'
	}).when('/river-creation', {
		templateUrl : resourceRoot + '/river/creation.html',
		controller : 'RiverCreationCtrl'
	}).when('/variable-list', {
		templateUrl : resourceRoot + '/variable/list.html',
		controller : 'VariableListCtrl'
	}).when('/variable-detail/:id', {
		templateUrl : resourceRoot + '/variable/detail.html',
		controller : 'VariableDetailCtrl'
	}).when('/variable-creation', {
		templateUrl : resourceRoot + '/variable/creation.html',
		controller : 'VariableCreationCtrl'
	}).when('/fish-list', {
		templateUrl : resourceRoot + '/fish/list.html',
		controller : 'FishListCtrl'
	}).when('/fish-detail/:id', {
		templateUrl : resourceRoot + '/fish/detail.html',
		controller : 'FishDetailCtrl'
	}).when('/fish-creation', {
		templateUrl : resourceRoot + '/fish/creation.html',
		controller : 'FishCreationCtrl'
	}).when('/method-list', {
		templateUrl : resourceRoot + '/method/list.html',
		controller : 'MethodListCtrl'
	}).when('/method-detail/:id', {
		templateUrl : resourceRoot + '/method/detail.html',
		controller : 'MethodDetailCtrl'
	}).when('/method-creation', {
		templateUrl : resourceRoot + '/method/creation.html',
		controller : 'MethodCreationCtrl'
	}).when('/unit-list', {
		templateUrl : resourceRoot + '/unit/list.html',
		controller : 'UnitListCtrl'
	}).when('/unit-detail/:id', {
		templateUrl : resourceRoot + '/unit/detail.html',
		controller : 'UnitDetailCtrl'
	}).when('/unit-creation', {
		templateUrl : resourceRoot + '/unit/creation.html',
		controller : 'UnitCreationCtrl'
	}).when('/instrument-list', {
		templateUrl : resourceRoot + '/instrument/list.html',
		controller : 'InstrumentListCtrl'
	}).when('/instrument-detail/:id', {
		templateUrl : resourceRoot + '/instrument/detail.html',
		controller : 'InstrumentDetailCtrl'
	}).when('/instrument-creation', {
		templateUrl : resourceRoot + '/instrument/creation.html',
		controller : 'InstrumentCreationCtrl'
	}).when('/prediction-list', {
		templateUrl : resourceRoot + '/prediction/list.html',
		controller : 'PredictionListCtrl'
	}).when('/prediction-detail/:id', {
		templateUrl : resourceRoot + '/prediction/detail.html',
		controller : 'PredictionDetailCtrl'
	}).when('/prediction-creation', {
		templateUrl : resourceRoot + '/prediction/creation.html',
		controller : 'PredictionCreationCtrl'
	}).when('/dataSet-list', {
		templateUrl : resourceRoot + '/dataset/list.html',
		controller : 'DataSetListCtrl'
	}).when('/dataSet-detail/:id', {
		templateUrl : resourceRoot + '/dataset/detail.html',
		controller : 'DataSetDetailCtrl'
	}).when('/dataSet-creation', {
		templateUrl : resourceRoot + '/dataset/creation.html',
		controller : 'DataSetCreationCtrl'
	}).when('/model-list', {
		templateUrl : resourceRoot + '/model/list.html',
		controller : 'ModelListCtrl'
	}).when('/model-detail/:id', {
		templateUrl : resourceRoot + '/model/detail.html',
		controller : 'ModelDetailCtrl'
	}).when('/model-creation', {
		templateUrl : resourceRoot + '/model/creation.html',
		controller : 'ModelCreationCtrl'
	});
} ]);
