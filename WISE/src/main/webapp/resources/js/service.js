/**
 * @ S4RM System
 * @File Name: service.js
 * @Description: Angular JS Services
 * 
 * @author Meilan Jiang
 * @since 2016.06.17
 * @version 1.0
 * 
 * Copyright(c) 2016 by CILAB All right reserved.
 */

var contextRoot = '/wise';

/**
 * Tag
 */
wiseApp.factory('TagCollectionService', function($resource) {
	return $resource(contextRoot + '/tags', {}, {
		find : {
			method : 'GET',
			isArray : true
		}
	})
});
wiseApp.factory('NewTagService', function($resource) {
	return $resource(contextRoot + '/tags/new', {}, {
		create : {
			method : 'POST'
		}
	})
});
wiseApp.factory('TagService', function($resource) {
	return $resource(contextRoot + '/tags/:id', {id: '@id'}, {
		find : {
			method : 'GET',
			params : {
				id : '@id'
			}
		},
		update : {
			method : 'PUT'			
		},
		remove : {
			method : 'DELETE'
		}
	})
});

/**
 * Meatadata
 */
wiseApp.factory('MetadataCollectionService', function($resource) {
	return $resource(contextRoot + '/metas', {}, {
		find : {
			method : 'GET',
			isArray : true
		}
	})
});
wiseApp.factory('NewMetadataService', function($resource) {
	return $resource(contextRoot + '/metas/new', {}, {
		create : {
			method : 'POST'
		}
	})
});
wiseApp.factory('MetadataService', function($resource) {
	return $resource(contextRoot + '/metas/:id', {}, {

		find : {
			method : 'GET',
			params : {
				id : '@id'
			}
		},
		update : {
			method : 'PUT'
		},
		remove : {
			method : 'DELETE'
		}
	})
});

/**
 * Sensors
 */
wiseApp.factory('SensorCollectionService', function($resource) {
	return $resource(contextRoot + '/sensors', {}, {
		find : {
			method : 'GET',
			isArray : true
		},
		search : {
			method : 'POST',
			isArray : true
		}
	})
});
wiseApp.factory('NewSensorService', function($resource) {
	return $resource(contextRoot + '/sensors/new', {}, {
		create : {
			method : 'POST'
		}
	})
});
wiseApp.factory('SensorService', function($resource) {
	return $resource(contextRoot + '/sensors/:id', {id : '@id'}, {
		find : {
			method : 'GET',
			params : {
				id : '@id'
			}
		},
		update : {
			method : 'PUT'
		},
		remove : {
			method : 'DELETE'
		}
	})
});


/**
 * Streams
 */
wiseApp.factory('StreamCollectionService', function($resource) {
	return $resource(contextRoot + '/streams', {}, {
		find : {
			method : 'GET',
			isArray : true
		}
	})
});
wiseApp.factory('NewStreamService', function($resource) {
	return $resource(contextRoot + '/streams/new', {}, {
		create : {
			method : 'POST'
		}
	})
});
wiseApp.factory('StreamService', function($resource) {
	return $resource(contextRoot + '/streams/:id', {id: '@id'}, {
		find : {
			method : 'GET',
			params : {
				id : '@id'
			}
		},
		update : {
			method : 'PUT'
		},
		remove : {
			method : 'DELETE'
		}
	})
});


/**
 * Log
 */
wiseApp.factory('LogCollectionService', function($resource) {
	return $resource(contextRoot + '/logs', {}, {
		find : {
			method : 'GET',
			isArray : true
		}
	})
});
wiseApp.factory('NewLogService', function($resource) {
	return $resource(contextRoot + '/logs/new', {}, {
		create : {
			method : 'POST'
		}
	})
});
wiseApp.factory('LogService', function($resource) {
	return $resource(contextRoot + '/logs/:id', {id: '@id'}, {
		find : {
			method : 'GET',
			params : {
				id : '@id'
			}
		},
		update : {
			method : 'PUT'
		},
		remove : {
			method : 'DELETE'
		}
	})
});

/**
 * Users
 */
wiseApp.factory('UserCollectionService', function($resource) {
	return $resource(contextRoot + '/users', {}, {
		find : {
			method : 'GET',
			isArray : true
		},
		search : {
			method : 'POST',
			isArray : true
		}
	})
});
wiseApp.factory('UserSensorService', function($resource) {
	return $resource(contextRoot + '/users/new', {}, {
		create : {
			method : 'POST'
		}
	})
});
wiseApp.factory('UserService', function($resource) {
	return $resource(contextRoot + '/users/:id', {id : '@id'}, {
		find : {
			method : 'GET',
			params : {
				id : '@id'
			}
		},
		update : {
			method : 'PUT'
		},
		remove : {
			method : 'DELETE'
		}
	})
});

/*
 * M4 Metadata Services
*/

wiseApp.factory('EntityCollectionService', function($resource) {
	return $resource(contextRoot + '/entities', {}, {
		find : {
			method : 'GET',
			isArray : true
		}
	})
});
wiseApp.factory('NewEntityService', function($resource) {
	return $resource(contextRoot + '/entities/new', {}, {
		create : {
			method : 'POST'
		}
	})
});
wiseApp.factory('EntityService', function($resource) {
	return $resource(contextRoot + '/entities/:id', {id: '@id'}, {
		find : {
			method : 'GET',
			params : {
				id : '@id'
			}
		},
		update : {
			method : 'PUT'			
		},
		remove : {
			method : 'DELETE'
		}
	})
});

/**
 * Sources
 */
wiseApp.factory('SourceCollectionService', function($resource) {
	return $resource(contextRoot + '/sources', {}, {
		find : {
			method : 'GET',
			isArray : true
		}
	})
});
wiseApp.factory('NewSourceService', function($resource) {
	return $resource(contextRoot + '/sources/new', {}, {
		create : {
			method : 'POST'
		}
	})
});
wiseApp.factory('SourceService', function($resource) {
	return $resource(contextRoot + '/sources/:id', {}, {

		find : {
			method : 'GET',
			params : {
				id : '@id'
			}
		},
		update : {
			method : 'PUT',
			params : {
				id : '@id'
			}
		},
		remove : {
			method : 'DELETE',
			params : {
				id : '@id'
			}
		}
	})
});

/**
 * Sites
 */
wiseApp.factory('SiteCollectionService', function($resource) {
	return $resource(contextRoot + '/sites', {}, {
		find : {
			method : 'GET',
			isArray : true
		},
		search : {
			method : 'POST',
			isArray : true
		}
	})
});
wiseApp.factory('NewSiteService', function($resource) {
	return $resource(contextRoot + '/sites/new', {}, {
		create : {
			method : 'POST'
		}
	})
});
wiseApp.factory('SiteService', function($resource) {
	return $resource(contextRoot + '/sites/:id', {id : '@id'}, {
		find : {
			method : 'GET',
			params : {
				id : '@id'
			}
		},
		update : {
			method : 'PUT'
		},
		remove : {
			method : 'DELETE'
		}
	})
});

/**
 * Rivers
 */
wiseApp.factory('RiverCollectionService', function($resource) {
	return $resource(contextRoot + '/rivers', {}, {
		find : {
			method : 'GET',
			isArray : true
		}
	})
});
wiseApp.factory('NewRiverService', function($resource) {
	return $resource(contextRoot + '/rivers/new', {}, {
		create : {
			method : 'POST'
		}
	})
});
wiseApp.factory('RiverService', function($resource) {
	return $resource(contextRoot + '/rivers/:id', {id : '@id'}, {
		find : {
			method : 'GET',
			params : {
				id : '@id'
			}
		},
		update : {
			method : 'PUT'
		},
		remove : {
			method : 'DELETE'
		}
	})
});

/**
 * Fishes
 */
wiseApp.factory('FishCollectionService', function($resource) {
	return $resource(contextRoot + '/fishes', {}, {
		find : {
			method : 'GET',
			cache : true,
			isArray : true
		}
	})
});
wiseApp.factory('NewFishService', function($resource) {
	return $resource(contextRoot + '/fishes/new', {}, {
		create : {
			method : 'POST'
		}
	})
});
wiseApp.factory('FishService', function($resource) {
	return $resource(contextRoot + '/fishes/:id', {id : '@id'}, {
		find : {
			method : 'GET',
			params : {
				id : '@id'
			}
		},
		update : {
			method : 'PUT'
		},
		remove : {
			method : 'DELETE'
		}
	})
});

/**
 * Methods
 */
wiseApp.factory('MethodCollectionService', function($resource) {
	return $resource(contextRoot + '/methods', {}, {
		find : {
			method : 'GET',
			isArray : true
		}
	})
});
wiseApp.factory('NewMethodService', function($resource) {
	return $resource(contextRoot + '/methods/new', {}, {
		create : {
			method : 'POST'
		}
	})
});
wiseApp.factory('MethodService', function($resource) {
	return $resource(contextRoot + '/methods/:id', {id: '@id'}, {
		find : {
			method : 'GET',
			params : {
				id : '@id'
			}
		},
		update : {
			method : 'PUT'
		},
		remove : {
			method : 'DELETE'
		}
	})
});

/**
 * Instruments
 */
wiseApp.factory('InstrumentCollectionService', function($resource) {
	return $resource(contextRoot + '/instruments', {}, {
		find : {
			method : 'GET',
			isArray : true
		}
	})
});
wiseApp.factory('NewInstrumentService', function($resource) {
	return $resource(contextRoot + '/instruments/new', {}, {
		create : {
			method : 'POST'
		}
	})
});
wiseApp.factory('InstrumentService', function($resource) {
	return $resource(contextRoot + '/instruments/:id', {id: '@id'}, {
		find : {
			method : 'GET',
			params : {
				id : '@id'
			}
		},
		update : {
			method : 'PUT'			
		},
		remove : {
			method : 'DELETE'
		}
	})
});

/**
 * Predictions
 */
wiseApp.factory('PredictionCollectionService', function($resource) {
	return $resource(contextRoot + '/predictions', {}, {
		find : {
			method : 'GET',
			isArray : true
		}
	})
});
wiseApp.factory('NewPredictionService', function($resource) {
	return $resource(contextRoot + '/predictions/new', {}, {
		create : {
			method : 'POST'
		}
	})
});
wiseApp.factory('PredictionService', function($resource) {
	return $resource(contextRoot + '/predictions/:id', {id: '@id'}, {
		find : {
			method : 'GET',
			params : {
				id : '@id'
			}
		},
		update : {
			method : 'PUT'			
		},
		remove : {
			method : 'DELETE'
		}
	})
});

/**
 * Variables
 */
wiseApp.factory('VariableCollectionService', function($resource) {
	return $resource(contextRoot + '/variables', {}, {
		find : {
			method : 'GET',
			isArray : true
		}
	})
});
wiseApp.factory('NewVariableService', function($resource) {
	return $resource(contextRoot + '/variables/new', {}, {
		create : {
			method : 'POST'
		}
	})
});
wiseApp.factory('VariableService', function($resource) {
	return $resource(contextRoot + '/variables/:id', {id: '@id'}, {
		find : {
			method : 'GET',
			params : {
				id : '@id'
			}
		},
		update : {
			method : 'PUT'
		},
		remove : {
			method : 'DELETE'
		}
	})
});

/**
 * Units
 */
wiseApp.factory('UnitCollectionService', function($resource) {
	return $resource(contextRoot + '/units', {}, {
		find : {
			method : 'GET',
			isArray : true
		}
	})
});
wiseApp.factory('NewUnitService', function($resource) {
	return $resource(contextRoot + '/units/new', {}, {
		create : {
			method : 'POST'
		}
	})
});
wiseApp.factory('UnitService', function($resource) {
	return $resource(contextRoot + '/units/:id', {id: '@id'}, {
		find : {
			method : 'GET',
			params : {
				id : '@id'
			}
		},
		update : {
			method : 'PUT'			
		},
		remove : {
			method : 'DELETE'
		}
	})
});

/**
 * DataSets
 */
wiseApp.factory('DataSetCollectionService', function($resource) {
	return $resource(contextRoot + '/dataSets', {}, {
		find : {
			method : 'GET',
			isArray : true
		}
	})
});
wiseApp.factory('NewDataSetService', function($resource) {
	return $resource(contextRoot + '/dataSets/new', {}, {
		create : {
			method : 'POST'
		}
	})
});
wiseApp.factory('DataSetService', function($resource) {
	return $resource(contextRoot + '/dataSets/:id', {id: '@id'}, {
		find : {
			method : 'GET',
			params : {
				id : '@id'
			}
		},
		update : {
			method : 'PUT'
		},
		remove : {
			method : 'DELETE'
		}
	})
});

/**
 * Models
 */
wiseApp.factory('ModelCollectionService', function($resource) {
	return $resource(contextRoot + '/models', {}, {
		find : {
			method : 'GET',
			isArray : true
		}
	})
});
wiseApp.factory('NewModelService', function($resource) {
	return $resource(contextRoot + '/models/new', {}, {
		create : {
			method : 'POST'
		}
	})
});
wiseApp.factory('ModelService', function($resource) {
	return $resource(contextRoot + '/models/:id', {id: '@id'}, {
		find : {
			method : 'GET'
//				,
//			params : {
//				id : '@id'
//			}
		},
		update : {
			method : 'PUT'			
		},
		remove : {
			method : 'DELETE'
		},
		download : {
			method : 'GET'
		}
	})
});
wiseApp.factory('ModelTrainingDataService', ['$q', '$timeout', '$window',
                  function($q, $timeout, $window) {
		return {
			download: function(id) {

				var defer = $q.defer();
				$timeout(function() {
					$window.location = contextRoot + '/models/' + id + '/trainingData';
				}, 1000)
				.then(function() {
					defer.resolve('success');
					}, function() {
						defer.reject('error');
					});
				return defer.promise;
			}
		};
}]);

wiseApp.service('fileUpload', ['$http', function ($http) {
    this.uploadFileToUrl = function(file, id){
       var fd = new FormData();
       fd.append('file', file);
    
       $http({
    	  method: 'POST',
    	  url: contextRoot + '/models/' + id + '/trainingData',
    	  data: fd,
    	  transformRequest: function(data, headersGetterFunction) {
              return data;
          },
          headers: {'Content-Type': undefined}
       })
       .success(function(){
       })
    
       .error(function(){
       });
    }
 }]);
wiseApp.factory('ModelScriptService', ['$q', '$timeout', '$window',
                  function($q, $timeout, $window) {
		return {
			download: function(id) {

				var defer = $q.defer();
				$timeout(function() {
					$window.location = contextRoot + '/models/' + id + '/script';
				}, 1000)
				.then(function() {
					defer.resolve('success');
					}, function() {
						defer.reject('error');
					});
				return defer.promise;
			}
		};
}]);
