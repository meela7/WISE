/**
 * @ S4RM System
 * @File Name: controller.js
 * @Description: Angular JS Controllers
 * 
 * @author Meilan Jiang
 * @since 2016.06.17
 * @version 1.0
 * 
 * Copyright(c) 2016 by CILAB All right reserved.
 */


/**
 * Tag Controller
 */
wiseApp.controller('TagListCtrl',['$scope', 'TagCollectionService', 'TagService', '$location',
		function($scope, TagCollectionService, TagService, $location) {
	
		$scope.cancel = function() {
			$scope.search = "";
		}
		// callback for ng-click 'editTag':
		$scope.editTag = function(tagID) {
			$location.path('/tag-detail/' + tagID);
		};

		// callback for ng-click 'deleteTag':
		$scope.deleteTag = function(tagID) {
			TagService.remove({id:tagID});
			$location.path('/tag-list');
		};
		// callback for ng-click 'createTag':
		$scope.createTag = function() {
			$location.path('/tag-creation');
		};
		$scope.tags = TagCollectionService.find();

		// panination
		$scope.currentPage = 1, $scope.numPerPage = 10,
		$scope.maxSize = 10;
}]);

wiseApp.controller('TagDetailCtrl', [ '$scope', '$routeParams', 'TagService', '$location',
		function($scope, $routeParams, TagService, $location) {
			
		// callback for ng-click 'updateTag':
		$scope.updateTag = function(tag) {
			TagService.update({id:tag.tagID}, tag);
			$location.path('/tag-list');
		};
		// callback for ng-click 'cancel':
		$scope.cancel = function() {
			window.history.back();
		};
		$scope.tag = TagService.find({
			id : $routeParams.id
		});
}]);

wiseApp.controller('TagCreationCtrl', [ '$scope', 'NewTagService', '$location', 
        function($scope, NewTagService, $location) {
		
		// callback for ng-click 'createNewTag':
		$scope.createTag = function(tag) {
			NewTagService.create(tag);
			$location.path('/tag-list');
		};
		$scope.cancel = function() {
			window.history.back();
		};
}]);

/** 
 * Metadata Controller
 */
wiseApp.controller('MetadataListCtrl',['$scope',	'MetadataCollectionService', 'MetadataService',	'$location',
		function($scope, MetadataCollectionService, MetadataService, $location) {

		$scope.cancel = function() {
			$scope.search = "";
		};
		
		// callback for ng-click 'editMetadata':
		$scope.editMetadata = function(metaID) {
			$location.path('/meta-detail/' + metaID);
		};
		
		// callback for ng-click 'deleteMetadata':
		$scope.deleteMetadata = function(metaID) {
			MetadataService.remove({id:metaID});
			$location.path('/meta-list');
		};
		// callback for ng-click 'createMetadata':
		$scope.createMetadata = function() {
			$location.path('/meta-creation');
		};
		$scope.metas = MetadataCollectionService.find();
		// panination
		$scope.currentPage = 1, $scope.numPerPage = 10,
		$scope.maxSize = 10;
}]);
wiseApp.controller('MetadataDetailCtrl', [ '$scope', '$routeParams', 'MetadataService', '$location',
		function($scope, $routeParams, MetadataService, $location) {
		// callback for ng-click 'updateMetadata':
		$scope.updateMetadata = function(meta) {
			MetadataService.update({id:meta.metadataID}, meta);			
			$location.path('/meta-list');
		};
		// callback for ng-click 'cancel':
		$scope.cancel = function() {
			window.history.back();
		};
		$scope.meta = MetadataService.find({
			id : $routeParams.id
		});
}]);
wiseApp.controller('MetadataCreationCtrl', [ '$scope', 'NewMetadataService', '$location', 
        function($scope, NewMetadataService, $location) {
	
		// callback for ng-click 'createNewMetadata':
		$scope.createMetadata = function() {
			NewMetadataService.create($scope.meta);
			$location.path('/meta-list');
		};
		$scope.cancel = function() {
			window.history.back();
		};
}]);

/**
 * Sensor Controller
 */
wiseApp.controller('SensorListCtrl', ['$scope', 'SensorCollectionService', 'SensorService', '$location',
        function($scope, SensorCollectionService, SensorService, $location) {

		// callback for ng-click 'editSensor':
		$scope.editSensor = function(sensorID) {
			$location.path('/sensor-detail/' + sensorID);
		};
		// callback for ng-click 'deleteSensor':
		$scope.deleteSensor = function(sensorID) {
			SensorService.remove({id: sensorID});
			$location.path('/sensor-list');
		};
		// callback for ng-click 'createSensor':
		$scope.createSensor = function() {
			$location.path('/sensor-creation');
		};
	
		$scope.cancel = function() {
			$scope.search = "";
		};
	
		$scope.sensors = SensorCollectionService.find();
	
		// panination
		$scope.currentPage = 1, $scope.numPerPage = 10,
		$scope.maxSize = 10;
}]);

wiseApp.controller('SensorDetailCtrl', [ '$scope', '$routeParams', 'SensorService', '$location',
		function($scope, $routeParams, SensorService, $location) {
	
		// callback for ng-click 'updateSensor':
		$scope.updateSensor = function(sensor) {
			SensorService.update({id: sensor.id}, sensor);
			$location.path('/sensor-list');
		};
		// callback for ng-click 'cancel':
		$scope.cancel = function() {
			window.history.back();
		};
		$scope.sensor = SensorService.find({
			id : $routeParams.id
		});
}]);

wiseApp.controller('SensorCreationCtrl', [ '$scope', 'NewSensorService', 'MetadataCollectionService', 'TagCollectionService', 'StreamCollectionService', 'UserCollectionService', '$location', 
        function($scope, NewSensorService, MetadataCollectionService, TagCollectionService, StreamCollectionService, UserCollectionService, $location) {
	
		$scope.metas = MetadataCollectionService.find();
		$scope.tags = TagCollectionService.find();
		$scope.streams = StreamCollectionService.find();
		$scope.users = UserCollectionService.find();
		
		// callback for ng-click 'createSensor':
		$scope.createSensor = function(sensor) {
			NewSensorService.create(sensor);
			$location.path('/sensor-list');
		};
		$scope.cancel = function() {
			window.history.back();
		};
}]);


/**
 * Stream Controller
 */
wiseApp.controller('StreamListCtrl', ['$scope', 'StreamCollectionService', 'StreamService', '$location',
		function($scope, StreamCollectionService, StreamService, $location) {
					
		$scope.cancel = function() {
			$scope.search = "";
		}
		// callback for ng-click 'editStream':
		$scope.editStream = function(streamID) {
			$location.path('/stream-detail/' + streamID);
		};

		// callback for ng-click 'deleteStream':
		$scope.deleteStream = function(streamID) {
			StreamService.remove({id:streamID});
			$location.path('/stream-list');
		};
		// callback for ng-click 'createStream':
		$scope.createStream = function() {
			$location.path('/stream-creation');
		};
		$scope.streams = StreamCollectionService.find();

		// panination
		$scope.currentPage = 1, $scope.numPerPage = 10,
		$scope.maxSize = 10;
}]);

wiseApp.controller('StreamDetailCtrl', [ '$scope', '$routeParams',	'StreamService', 'MetadataCollectionService', 'TagCollectionService', '$location', '$log',
		function($scope, $routeParams, StreamService, MetadataCollectionService, TagCollectionService, $location, $log) {
	
		// callback for ng-click 'updateStream':
		$scope.updateStream = function(stream) {
			StreamService.update({id:stream.id}, stream);
			$location.path('/stream-list');
		};
		// callback for ng-click 'cancel':
		$scope.cancel = function() {
			window.history.back();
		};
		$scope.stream = StreamService.find({
			id : $routeParams.id
		});
		
		$scope.selectedA = [];
		$scope.selectedB = [];
		
		$scope.selectedTagA = [];
		$scope.selectedTagB = [];
				
		$scope.items = MetadataCollectionService.find();
		$scope.listA = MetadataCollectionService.find();
		
		$scope.tagItems = TagCollectionService.find();
		$scope.tagListA = TagCollectionService.find();
				
		function arrayObjectIndexOf(myArray, searchTerm, property) {
		    for(var i = 0, len = myArray.length; i < len; i++) {
		        if (myArray[i][property] === searchTerm) return i;
		    }
		    return -1;
		}
		  
		$scope.aToB = function() {
		  for (i in $scope.selectedA) {
		    var moveId = arrayObjectIndexOf($scope.items, $scope.selectedA[i], "key");
		    var meta= {}; 
		    meta.key = $scope.items[moveId].key;
		    meta.value = $scope.items[moveId].value;
		    meta.streamID = $scope.stream.id;
		    
		    $scope.stream.metas.push(meta);
		    var delId = arrayObjectIndexOf($scope.listA, $scope.selectedA[i], "key"); 
		    $scope.listA.splice(delId,1);
		  }
		  reset();
		};
		  
		$scope.bToA = function() {
		  for (i in $scope.selectedB) {
		    var moveId = arrayObjectIndexOf($scope.items, $scope.selectedB[i], "key"); 
		    $scope.listA.push($scope.items[moveId]);
		    var delId = arrayObjectIndexOf($scope.stream.metas, $scope.selectedB[i], "key"); 
		    $scope.stream.metas.splice(delId,1);
		  }
		  reset();
		};
		
		function reset(){
		  	$scope.selectedA=[];
		    $scope.selectedB=[];
		  }
		  
		  $scope.selectA = function(i) {
		    $scope.selectedA.push(i);
		  };

		  $scope.selectB = function(i) {
		  	$scope.selectedB.push(i);
		  };

		  $scope.tagToB = function() {
			  for (i in $scope.selectedTagA) {
			    var moveId = arrayObjectIndexOf($scope.tagItems, $scope.selectedTagA[i], "name");
			    // to add Tag to Stream_Tag, should add streamID element.
			    var tag= {}; 
			    tag.name = $scope.tagItems[moveId].name;
			    tag.streamID = $scope.stream.id;
			    $scope.stream.tags.push(tag);
			    
			    $log.info( 'aToB ===> Added Tag Info: ' + $scope.tagItems[moveId].name );
			    var delId = arrayObjectIndexOf($scope.tagListA, $scope.selectedTagA[i], "name"); 
			    $scope.tagListA.splice(delId,1);
			  }
			  resetTag();
			};
			  
			$scope.tagToA = function() {
			  for (i in $scope.selectedTagB) {
			    var moveId = arrayObjectIndexOf($scope.tagItems, $scope.selectedTagB[i], "name"); 
			    $scope.tagListA.push($scope.tagItems[moveId]);
			    var delId = arrayObjectIndexOf($scope.stream.tags, $scope.selectedTagB[i], "kenamey"); 
			    $scope.stream.tags.splice(delId,1);
			  }
			  resetTag();
			};
			
			function resetTag(){
			  	$scope.selectedTagA=[];
			    $scope.selectedTagB=[];
			  }
			  
			  $scope.selectTagA = function(i) {
			    $scope.selectedTagA.push(i);
			  };

			  $scope.selectTagB = function(i) {
			  	$scope.selectedTagB.push(i);
			  };
}]);

wiseApp.controller('StreamCreationCtrl', [ '$scope', 'NewStreamService', 'SensorCollectionService', 'MetadataCollectionService','TagCollectionService', 'UserCollectionService', '$location', 
         function($scope, NewStreamService, SensorCollectionService,MetadataCollectionService, TagCollectionService, UserCollectionService, $location) {
		
		$scope.sensors = SensorCollectionService.find();
		$scope.metas = MetadataCollectionService.find();
		$scope.tags = TagCollectionService.find();
		$scope.users = UserCollectionService.find();
		
		// callback for ng-click 'createNewStream':
		$scope.createStream = function(stream) {
			NewStreamService.create(stream);
			$location.path('/stream-list');
		};
		$scope.cancel = function() {
			window.history.back();
		};
}]);

/**
 * Log Controller
 */
wiseApp.controller('LogListCtrl',['$scope', 'LogCollectionService', 'LogService', '$location',
		function($scope, LogCollectionService, LogService, $location) {
	
		$scope.cancel = function() {
			$scope.search = "";
		}
		// callback for ng-click 'editLog':
		$scope.editLog = function(logID) {
			$location.path('/log-detail/' + logID);
		};

		// callback for ng-click 'deleteLog':
		$scope.deleteLog = function(logID) {
			LogService.remove({id:logID});
			$location.path('/log-list');
		};
		// callback for ng-click 'createLog':
		$scope.createLog = function() {
			$location.path('/log-creation');
		};
		$scope.logs = LogCollectionService.find();

		// panination
		$scope.currentPage = 1, $scope.numPerPage = 10,
		$scope.maxSize = 10;
}]);

wiseApp.controller('LogDetailCtrl', [ '$scope', '$routeParams', 'LogService', '$location',
		function($scope, $routeParams, LogService, $location) {

		// callback for ng-click 'updateLog':
		$scope.updateLog = function(log) {
			LogService.update({id:log.logID}, log);
			$location.path('/log-list');
		};
		// callback for ng-click 'cancel':
		$scope.cancel = function() {
			window.history.back();
		};
		$scope.log = LogService.find({
			id : $routeParams.id
		});
}]);

wiseApp.controller('LogCreationCtrl', [ '$scope', 'NewLogService','StreamCollectionService','$location', 
        function($scope, NewLogService, StreamCollectionService, $location) {
			
		$scope.streams = StreamCollectionService.find();
			
		// callback for ng-click 'createNewFeature':
		$scope.createLog = function(log) {
			NewLogService.create(log);
			$location.path('/log-list');
		};
		$scope.cancel = function() {
			window.history.back();
		};
}]);

/*
* M4 Metadata Controller
*/

/**
 * Entity
 */
wiseApp.controller('EntityListCtrl',['$scope', 'EntityCollectionService', 'EntityService', '$location',
		function($scope, EntityCollectionService, EntityService, $location) {
	
		$scope.cancel = function() {
			$scope.search = "";
		}
		// callback for ng-click 'editEntity':
		$scope.editEntity = function(entityID) {
			$location.path('/entity-detail/' + entityID);
		};

		// callback for ng-click 'deleteEntity':
		$scope.deleteEntity = function(entity) {
			EntityService.remove({id:entity.entityID});
			$location.path('/entity-list');
		};
		// callback for ng-click 'createEntity':
		$scope.createEntity = function() {
			$location.path('/entity-creation');
		};
		$scope.entities = EntityCollectionService.find();

		// panination
		$scope.currentPage = 1, $scope.numPerPage = 10,
		$scope.maxSize = 10;
}]);

wiseApp.controller('EntityDetailCtrl', [ '$scope', '$routeParams', 'EntityService', '$location',
		function($scope, $routeParams, EntityService, $location) {
			
		// callback for ng-click 'updateEntity':
		$scope.updateEntity = function(entity) {
			EntityService.update({id:entity.entityID}, entity);
			$location.path('/entity-list');
		};
		// callback for ng-click 'cancel':
		$scope.cancel = function() {
			window.history.back();
		};
		$scope.entity = EntityService.find({
			id : $routeParams.id
		});
}]);

wiseApp.controller('EntityCreationCtrl', [ '$scope', 'NewEntityService', '$location', 
        function($scope, NewEntityService, $location) {
		
		// callback for ng-click 'createNewEntity':
		$scope.createEntity = function(entity) {
			NewEntityService.create(entity);
			$location.path('/entity-list');
		};
		$scope.cancel = function() {
			window.history.back();
		};
}]);

/** 
 * Source Controller
 */
wiseApp.controller('SourceListCtrl',['$scope',	'SourceCollectionService', 'SourceService',	'$location',
		function($scope, SourceCollectionService, SourceService, $location) {

		$scope.cancel = function() {
			$scope.search = "";
		};
		// callback for ng-click 'editSource':
		$scope.editSource = function(sourceID) {
			$location.path('/source-detail/' + sourceID);
		};
		// callback for ng-click 'deleteSource':
		$scope.deleteSource = function(sourceID) {
			SourceService.remove(sourceID);
			$location.path('/source-list');
		};
		// callback for ng-click 'createSource':
		$scope.createSource = function() {
			$location.path('/source-creation');
		};
		$scope.sources = SourceCollectionService.find();
		// panination
		$scope.currentPage = 1, $scope.numPerPage = 10,
		$scope.maxSize = 10;
}]);
wiseApp.controller('SourceDetailCtrl', [ '$scope', '$routeParams', 'SourceService', '$location',
		function($scope, $routeParams, SourceService, $location) {
		// callback for ng-click 'updateSource':
		$scope.updateSource = function(source) {
			SourceService.update({id:source.sourceID}, source);			
			$location.path('/source-list');
		};
		// callback for ng-click 'cancel':
		$scope.cancel = function() {
			window.history.back();
		};
		$scope.source = SourceService.find({
			id : $routeParams.id
		});
}]);
wiseApp.controller('SourceCreationCtrl', [ '$scope', 'NewSourceService', '$location', 
        function($scope, NewSourceService, $location) {
	
		// callback for ng-click 'createNewSource':
		$scope.createSource = function() {
			NewSourceService.create($scope.source);
			$location.path('/source-list');
		};
		$scope.cancel = function() {
			window.history.back();
		};
}]);

/**
 * Site Controller
 */
wiseApp.controller('SiteListCtrl', ['$scope', 'SiteCollectionService', 'SiteService', '$location',
        function($scope, SiteCollectionService, SiteService, $location) {

		// callback for ng-click 'editSite':
		$scope.editSite = function(siteID) {
			$location.path('/site-detail/' + siteID);
		};
		// callback for ng-click 'deleteSite':
		$scope.deleteSite = function(site) {
			SiteService.remove({id: site.siteID});
			$location.path('/site-list');
		};
		// callback for ng-click 'createSite':
		$scope.createSite = function() {
			$location.path('/site-creation');
		};
	
		$scope.cancel = function() {
			$scope.search = "";
		};
	
		$scope.sites = SiteCollectionService.find();
	
		// panination
		$scope.currentPage = 1, $scope.numPerPage = 10,
		$scope.maxSize = 10;
}]);

wiseApp.controller('SiteDetailCtrl', [ '$scope', '$routeParams', 'SiteService', '$location',
		function($scope, $routeParams, SiteService, $location) {
	
		// callback for ng-click 'updateSite':
		$scope.updateSite = function(site) {
			SiteService.update({id: site.siteID}, site);
			$location.path('/site-list');
		};
		// callback for ng-click 'cancel':
		$scope.cancel = function() {
			window.history.back();
		};
		$scope.site = SiteService.find({
			id : $routeParams.id
		});
}]);

wiseApp.controller('SiteCreationCtrl', [ '$scope', 'NewSiteService', '$location', 
        function($scope, NewSiteService, $location) {
	
		// callback for ng-click 'createSite':
		$scope.createSite = function(site) {
			NewSiteService.create(site);
			$location.path('/site-list');
		};
		$scope.cancel = function() {
			window.history.back();
		};
}]);

/**
 * River Controller
 */
wiseApp.controller('RiverListCtrl', ['$scope',	'RiverCollectionService', 'RiverService', '$location',
		function($scope, RiverCollectionService, RiverService, 	$location) {

		// callback for ng-click 'editRiver':
		$scope.editRiver = function(riverID) {
			$location.path('/river-detail/' + riverID);
		};
		// callback for ng-click 'deleteRiver':
		$scope.deleteRiver = function(river) {
			RiverService.remove({id:river.riverID});
			$location.path('/river-list');
		};
		// callback for ng-click 'createRiver':
		$scope.createRiver = function(river) {
			$location.path('/river-creation');
		};
	
		$scope.cancel = function() {
			$scope.search = "";
		};
	
		$scope.rivers = RiverCollectionService.find();
	
		// panination
		$scope.currentPage = 1, $scope.numPerPage = 10,
				$scope.maxSize = 10;
}]);

wiseApp.controller('RiverDetailCtrl', [ '$scope', '$routeParams', 'RiverService', '$location',
		function($scope, $routeParams, RiverService, $location) {
	
		// callback for ng-click 'updateRiver':
		$scope.updateRiver = function(river) {
			RiverService.update({id:river.riverID}, river);
			$location.path('/river-list');
		};
		// callback for ng-click 'cancel':
		$scope.cancel = function() {
	//		$location.path('/river-list');
			window.history.back();
		};
		$scope.river = RiverService.find({
			id : $routeParams.id
		});
	
}]);
wiseApp.controller('RiverCreationCtrl', [ '$scope', 'NewRiverService', '$location', 
        function($scope, NewRiverService, $location) {

		// callback for ng-click 'createRiver':
		$scope.createRiver = function(river) {
			NewRiverService.create(river);
			$location.path('/river-list');
		};
		$scope.cancel = function() {
			$location.path('/river-list');
		};
	
}]);

/**
 * Fishes
 */
wiseApp.controller('FishListCtrl',	['$scope', 'FishCollectionService', 'FishService', '$location',
		function($scope, FishCollectionService, FishService, $location) {
		
		$scope.cancel = function() {
			$scope.search = "";
		};
		// callback for ng-click 'editFish':
		$scope.editFish = function(fishID) {
			$location.path('/fish-detail/' + fishID);
		};
		// callback for ng-click 'deleteFish':
		$scope.deleteFish = function(fish) {
			FishService.remove({id:fish.entityID});
			$location.path('/fish-list');
		};
		// callback for ng-click 'createFish':
		$scope.createFish = function() {
			$location.path('/fish-creation');
		};
		$scope.fishes = FishCollectionService.find();
	
		// panination
		$scope.currentPage = 1, $scope.numPerPage = 10,
				$scope.maxSize = 10;

}]);

wiseApp.controller('FishDetailCtrl', [ '$scope', '$routeParams', 'FishService', '$location',
		function($scope, $routeParams, FishService, $location) {
	
		// callback for ng-click 'updateFish':
		$scope.updateFish = function(fish) {
			FishService.update({id:fish.entityID},fish);
			$location.path('/fish-list');
		};
		// callback for ng-click 'cancel':
		$scope.cancel = function() {
			window.history.back();
		};
		$scope.fish = FishService.find({
			id : $routeParams.id
		});
}]);
wiseApp.controller('FishCreationCtrl', [ '$scope', 'NewFishService', '$location', 
            function($scope, NewFishService, $location) {
			// callback for ng-click 'createNewFish':
			$scope.createFish = function(fish) {
				NewFishService.create(fish);
				$location.path('/fish-list');
			};
			$scope.cancel = function() {
				$location.path('/fish-list');
			};
		} ]);

/**
 * Methods
 */
wiseApp.controller('MethodListCtrl', ['$scope', 'MethodCollectionService', 'MethodService', '$location',
		function($scope, MethodCollectionService, MethodService, $location) {
					
		$scope.cancel = function() {
			$scope.search = "";
		}
		// callback for ng-click 'editMethod':
		$scope.editMethod = function(methodID) {
			$location.path('/method-detail/' + methodID);
		};

		// callback for ng-click 'deleteMethod':
		$scope.deleteMethod = function(method) {
			MethodService.remove({id:method.methodID});
			$location.path('/method-list');
		};
		// callback for ng-click 'createMethod':
		$scope.createMethod = function() {
			$location.path('/method-creation');
		};
		$scope.methods = MethodCollectionService.find();

		// panination
		$scope.currentPage = 1, $scope.numPerPage = 10,
		$scope.maxSize = 10;
}]);

wiseApp.controller('MethodDetailCtrl', [ '$scope', '$routeParams',	'MethodService', '$location',
		function($scope, $routeParams, MethodService, $location) {

		// callback for ng-click 'updateMethod':
		$scope.updateMethod = function(method) {
			MethodService.update({id:method.methodID}, method);
			$location.path('/method-list');
		};
		// callback for ng-click 'cancel':
		$scope.cancel = function() {
			window.history.back();
		};
		$scope.method = MethodService.find({
			id : $routeParams.id
		});
}]);

wiseApp.controller('MethodCreationCtrl', [ '$scope', 'NewMethodService', '$location', 
         function($scope, NewMethodService, $location) {
		
		// callback for ng-click 'createNewMethod':
		$scope.createMethod = function(method) {
			NewMethodService.create(method);
			$location.path('/method-list');
		};
		$scope.cancel = function() {
			window.history.back();
		};
}]);

/**
 * Instruments
 */
wiseApp.controller('InstrumentListCtrl',['$scope', 'InstrumentCollectionService', 'InstrumentService', '$location',
		function($scope, InstrumentCollectionService, InstrumentService, $location) {
	
		$scope.cancel = function() {
			$scope.search = "";
		}
		// callback for ng-click 'editInstrument':
		$scope.editInstrument = function(instrumentID) {
			$location.path('/instrument-detail/' + instrumentID);
		};

		// callback for ng-click 'deleteInstrument':
		$scope.deleteInstrument = function(instrument) {
			InstrumentService.remove({id:instrument.instrumentID});
			$location.path('/instrument-list');
		};
		// callback for ng-click 'createInstrument':
		$scope.createInstrument = function() {
			$location.path('/instrument-creation');
		};
		$scope.instruments = InstrumentCollectionService.find();

		// panination
		$scope.currentPage = 1, $scope.numPerPage = 10,
		$scope.maxSize = 10;
}]);

wiseApp.controller('InstrumentDetailCtrl', [ '$scope', '$routeParams', 'InstrumentService', '$location',
		function($scope, $routeParams, InstrumentService, $location) {
			
		// callback for ng-click 'updateInstrument':
		$scope.updateInstrument = function(instrument) {
			InstrumentService.update({id:instrument.instrumentID}, instrument);
			$location.path('/instrument-list');
		};
		// callback for ng-click 'cancel':
		$scope.cancel = function() {
			window.history.back();
		};
		$scope.instrument = InstrumentService.find({
			id : $routeParams.id
		});
}]);

wiseApp.controller('InstrumentCreationCtrl', [ '$scope', 'NewInstrumentService', '$location', 
        function($scope, NewInstrumentService, $location) {
		
		// callback for ng-click 'createNewInstrument':
		$scope.createInstrument = function(instrument) {
			NewInstrumentService.create(instrument);
			$location.path('/instrument-list');
		};
		$scope.cancel = function() {
			window.history.back();
		};
}]);

/**
 * Predictions
 */
wiseApp.controller('PredictionListCtrl', ['$scope', 'PredictionCollectionService', 'PredictionService', '$location',
		function($scope, PredictionCollectionService, PredictionService, $location) {
					
		$scope.cancel = function() {
			$scope.search = "";
		}
		// callback for ng-click 'editPrediction':
		$scope.editPrediction = function(methodID) {
			$location.path('/prediction-detail/' + methodID);
		};

		// callback for ng-click 'deletePrediction':
		$scope.deletePrediction = function(prediction) {
			PredictionService.remove({id:prediction.methodID});
			$location.path('/prediction-list');
		};
		// callback for ng-click 'createPrediction':
		$scope.createPrediction = function() {
			$location.path('/prediction-creation');
		};
		$scope.predictions = PredictionCollectionService.find();

		// panination
		$scope.currentPage = 1, $scope.numPerPage = 10,
				$scope.maxSize = 10;
}]);

wiseApp.controller('PredictionDetailCtrl', [ '$scope', '$routeParams',	'PredictionService', '$location',
		function($scope, $routeParams, PredictionService, $location) {

		// callback for ng-click 'updatePrediction':
		$scope.updatePrediction = function(prediction) {
			PredictionService.update({id:prediction.methodID}, prediction);
			$location.path('/prediction-list');
		};
		// callback for ng-click 'cancel':
		$scope.cancel = function() {
			window.history.back();
		};
		$scope.prediction = PredictionService.find({
			id : $routeParams.id
		});
}]);

wiseApp.controller('PredictionCreationCtrl', [ '$scope', 'NewPredictionService', '$location', 
         function($scope, NewPredictionService, $location) {
		
		// callback for ng-click 'createNewPrediction':
		$scope.createPrediction = function(prediction) {
			NewPredictionService.create(prediction);
			$location.path('/prediction-list');
		};
		$scope.cancel = function() {
			window.history.back();
		};
}]);

/**
 * Variables
 */
wiseApp.controller('VariableListCtrl',['$scope', 'VariableCollectionService', 'VariableService', '$location',
		function($scope, VariableCollectionService, VariableService, $location) {
	
		$scope.cancel = function() {
			$scope.search = "";
		}
		// callback for ng-click 'editVariable':
		$scope.editVariable = function(variableID) {
			$location.path('/variable-detail/' + variableID);
		};

		// callback for ng-click 'deleteVariable':
		$scope.deleteVariable = function(variableID) {
			VariableService.remove({id:variableID});
			$location.path('/variable-list');
		};
		// callback for ng-click 'createVariable':
		$scope.createVariable = function() {
			$location.path('/variable-creation');
		};
		$scope.variables = VariableCollectionService.find();

		// panination
		$scope.currentPage = 1, $scope.numPerPage = 10,
		$scope.maxSize = 10;
}]);

wiseApp.controller('VariableDetailCtrl', [ '$scope', '$routeParams', 'VariableService', 'UnitCollectionService', '$location',
		function($scope, $routeParams, VariableService, UnitCollectionService, $location) {

		// callback for ng-click 'updateVariable':
		$scope.updateVariable = function(variable) {
			VariableService.update({id:variable.variableID}, variable);
			$location.path('/variable-list');
		};
		// callback for ng-click 'cancel':
		$scope.cancel = function() {
			window.history.back();
		};
		$scope.variable = VariableService.find({
			id : $routeParams.id
		});
		$scope.items = UnitCollectionService.find();
}]);

wiseApp.controller('VariableCreationCtrl', [ '$scope', 'NewVariableService', 'UnitCollectionService','$location', 
        function($scope, NewVariableService, UnitCollectionService, $location) {
			
		$scope.units = UnitCollectionService.find();
			
		// callback for ng-click 'createNewFeature':
		$scope.createVariable = function(variable) {
			NewVariableService.create(variable);
			$location.path('/variable-list');
		};
		$scope.cancel = function() {
			window.history.back();
		};
}]);

/**
 * Units
 */
wiseApp.controller('UnitListCtrl',['$scope', 'UnitCollectionService', 'UnitService', '$location',
		function($scope, UnitCollectionService, UnitService, $location) {
	
		$scope.cancel = function() {
			$scope.search = "";
		}
		// callback for ng-click 'editUnit':
		$scope.editUnit = function(unitID) {
			$location.path('/unit-detail/' + unitID);
		};

		// callback for ng-click 'deleteUnit':
		$scope.deleteUnit = function(unit) {
			UnitService.remove({id:unit.unitID});
			$location.path('/unit-list');
		};
		// callback for ng-click 'createUnit':
		$scope.createUnit = function() {
			$location.path('/unit-creation');
		};
		$scope.units = UnitCollectionService.find();

		// panination
		$scope.currentPage = 1, $scope.numPerPage = 10,
		$scope.maxSize = 10;
}]);

wiseApp.controller('UnitDetailCtrl', [ '$scope', '$routeParams', 'UnitService', '$location',
		function($scope, $routeParams, UnitService, $location) {
			
		// callback for ng-click 'updateUnit':
		$scope.updateUnit = function(unit) {
			UnitService.update({id:unit.unitID}, unit);
			$location.path('/unit-list');
		};
		// callback for ng-click 'cancel':
		$scope.cancel = function() {
			window.history.back();
		};
		$scope.unit = UnitService.find({
			id : $routeParams.id
		});
}]);

wiseApp.controller('UnitCreationCtrl', [ '$scope', 'NewUnitService', '$location', 
        function($scope, NewUnitService, $location) {
		
		// callback for ng-click 'createNewUnit':
		$scope.createUnit = function(unit) {
			NewUnitService.create(unit);
			$location.path('/unit-list');
		};
		$scope.cancel = function() {
			window.history.back();
		};
}]);

/**
 * DataSets
 */
wiseApp.controller('DataSetListCtrl', ['$scope', 'DataSetCollectionService', 'DataSetService', '$location',
		function($scope, DataSetCollectionService, DataSetService, $location) {
					
		$scope.cancel = function() {
			$scope.search = "";
		}
		// callback for ng-click 'editDataSet':
		$scope.editDataSet = function(dataSetID) {
			$location.path('/dataSet-detail/' + dataSetID);
		};

		// callback for ng-click 'deleteDataSet':
		$scope.deleteDataSet = function(dataSet) {
			DataSetService.remove({id:dataSet.dataSetID});
			$location.path('/dataSet-list');
		};
		// callback for ng-click 'createDataSet':
		$scope.createDataSet = function() {
			$location.path('/dataSet-creation');
		};
		$scope.dataSets = DataSetCollectionService.find();

		// panination
		$scope.currentPage = 1, $scope.numPerPage = 10,
		$scope.maxSize = 10;
}]);

wiseApp.controller('DataSetDetailCtrl', [ '$scope', '$routeParams', 'DataSetService', 'SiteCollectionService', '$location',
		function($scope, $routeParams, DataSetService, SiteCollectionService, $location) {

		// callback for ng-click 'updateDataSet':
		$scope.updateDataSet = function(dataSet) {
			DataSetService.update({id:dataSet.dataSetID}, dataSet);
			$location.path('/dataSet-list');
		};
		// callback for ng-click 'cancel':
		$scope.cancel = function() {
			window.history.back();
		};
		$scope.dataSet = DataSetService.find({
			id : $routeParams.id
		});
		$scope.sites = SiteCollectionService.find();
}]);

wiseApp.controller('DataSetCreationCtrl', [ '$scope', 'NewDataSetService','SiteCollectionService', 'VariableCollectionService',
                                             'EntityCollectionService', 'MethodCollectionService', 'SourceCollectionService', '$location', 
         function($scope, NewDataSetService, SiteCollectionService, VariableCollectionService, EntityCollectionService, 
        		 MethodCollectionService, SourceCollectionService, $location) {
		
		// callback for ng-click 'createNewDataSet':
		$scope.createDataSet = function(dataSet) {
			NewDataSetService.create(dataSet);
			$location.path('/dataSet-list');
		};
		
		$scope.sites = SiteCollectionService.find();
		$scope.variables = VariableCollectionService.find();
		$scope.entities = EntityCollectionService.find();
		$scope.methods = MethodCollectionService.find();
		$scope.sources = SourceCollectionService.find();
		
		$scope.cancel = function() {
			window.history.back();
		};
}]);

/**
 * Map Controllers
 */
wiseApp.controller('SiteMapCtrl',['$scope', '$http',
		function($scope, $http) {

		var addressPointsToMarkers = function(points) {
			return points.map(function(ap) {
				
				return {
					layer : "site data",
					lat : ap.latitude,
					lng : ap.longitude
				};
			});
		};
	
		angular.extend($scope,{
			center : {
				lat : 37.517,
				lng : 126.894,
				zoom : 11
			},
			events : {
				map : {
					enable : ['moveend', 'popupopen' ],
					logic : 'emit'
				},
				marker : {
					enable : [],
					logic : 'emit'
				}
			},
			layers : {
				baselayers : {
					osm : {
						name : 'OpenStreetMap',
						type : 'xyz',
						url : 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png'
					},
					cycle : {
						name : 'OpenCycleMap',
						type : 'xyz',
						url : 'http://{s}.tile.opencyclemap.org/cycle/{z}/{x}/{y}.png'
	
					}
				},
				overlays : {
					siteData : {
						name : 'siteData',
						type : 'markercluster',
						visible : true
					}
				}
			}
		});
		
		$http.get("sites").success(function(data) {
				
			var points = data.map(function(d) {
				return {
					layer : "siteData",
					lat : parseFloat(d.latitude),
					lng : parseFloat(d.longitude),
					message : '<div class="panel panel-default"><div class="panel-heading"> <h4>'
							+ d.siteName
							+ '</h4></div><div class="panel-body">'
							+ '<a href="#/site-detail/'
							+ d.siteID
							+ '" class="thumbnail"> <img src="'
							+ d.imageLink
							+ '" class="img-rounded" style="width:260px;height:180px"> <br/> </a></div>'
				};
			});
	
			console.log(points.length);
	
			angular.extend($scope, {
				markers : points
			});
		});
}]);

/**
 * Models
 */
wiseApp.controller('ModelListCtrl', ['$scope', 'ModelCollectionService', 'ModelService', '$location',
		function($scope, ModelCollectionService, ModelService, $location) {
					
		$scope.cancel = function() {
			$scope.search = "";
		}
		// callback for ng-click 'editModel':
		$scope.editModel = function(modelID) {
			$location.path('/model-detail/' + modelID);
		};

		// callback for ng-click 'deleteModel':
		$scope.deleteModel = function(model) {
			ModelService.remove({id:model.methodID});
			$location.path('/model-list');
		};
		// callback for ng-click 'createPrediction':
		$scope.createModel = function() {
			$location.path('/model-creation');
		};
		$scope.models = ModelCollectionService.find();

		// panination
		$scope.currentPage = 1, $scope.numPerPage = 10,
				$scope.maxSize = 10;
}]);

wiseApp.controller('ModelDetailCtrl', [ '$scope', '$routeParams', 'ModelTrainingDataService', 'ModelScriptService', 'ModelService', 'fileUpload', '$location',
		function($scope, $routeParams, ModelCSVDownloadService, ModelScriptService, ModelService, fileUpload, $location) {

		// callback for ng-click 'updateModel':
		$scope.updateModel = function(model) {
			ModelService.update({id:model.methodID}, model);
			$location.path('/model-list');
		};
		
		// download training data csv file 
		$scope.downloadTrainingData = function(modelID) {
			ModelCSVDownloadService.download(modelID);
		};
		
		// download script as txt file
		$scope.downloadScript = function(modelID) {
			ModelScriptService.download(modelID);
		};
		
		// upload script as txt file
		$scope.uploadTrainingData = function(modelID) {
			
			var file = $scope.csvFile;
            
            console.log('file is ' );
            console.dir(file);
            
            fileUpload.uploadFileToUrl(file, modelID);
		};
		
		// callback for ng-click 'cancel':
		$scope.cancel = function() {
			window.history.back();
		};
		$scope.model = ModelService.find({
			id : $routeParams.id
		});
}]);

wiseApp.controller('ModelCreationCtrl', [ '$scope', 'NewModelService', '$location', 
         function($scope, NewModelService, $location) {
		
		// callback for ng-click 'createNewModel':
		$scope.createModel = function(model) {
			NewModelService.create(model);
			$location.path('/model-list');
		};
		$scope.cancel = function() {
			window.history.back();
		};
}]);

