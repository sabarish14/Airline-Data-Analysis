angular.module('flightService', [])

	// super simple service
	// each function returns a promise object 
	.factory('Flight', ['$http',function($http) {
		return {
			getOrig : function()
			{
				return $http.get('/api/orig');
			},
			getDest : function() {
				return $http.get('/api/dest');
			},
			getCarrier : function() {
				return $http.get('/api/carrier');
			},

			/*predict : function(flightData) {
				return $http.post('/api/predict', flightData);
			},*/
		
		}
	}]);
