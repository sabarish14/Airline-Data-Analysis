angular.module('FlightController', [])

        // inject the Todo service factory into our controller
	.controller('mainController', ['$scope','$http','$window','Flight', function($scope, $http,$window, Flight) {
		$scope.loading = true;
	  		
		// GET =====================================================================
		
		// when landing on the page, get all orig and show them
		// use the service/Flight.js to get all orig
		Flight.getOrig()
			.success(function(data) {
				$scope.orig = data;
				$scope.loading = false;
			});
		// when landing on the page, get all dest and show them
		// use the service/Flight.js to get all dest
		Flight.getDest()
			.success(function(data) {
				$scope.dest = data;
				$scope.loading = false;
			});
		// when landing on the page, get all orig and show them
		// use the service/Flight.js to get all carriers
		Flight.getCarrier()
			.success(function(data) {
				$scope.carrier = data;
				$scope.loading = false;
			});

		

		/*$scope.predict = function()
		 {

			// validate the formData to make sure that something is there
				$scope.loading = true;
				// call the create function from our service (returns a promise object)
				Flight.predict()
				// if successful creation, call our get function to get all the new todos
				.success(function(data) {
					$scope.loading = false;
					$scope.formData = {}; // clear the form so our user is ready to enter another
					//$scope.todos = data; // assign our new list of todos
					});
			
		};*/
		$scope.predict = function() {
			console.log($scope.formData.date);
			$http.post('api/predict',$scope.formData).
			success(function(data) {
			    console.log(data);
			if (data==null)
				$scope.prediction="Data not available";
			else if (data.prediction=="1")
				$scope.prediction="Flight will be delayed";
			else
				$scope.prediction="Flight will not be delayed"
			$window.alert($scope.prediction);
			}).error(function(data) {
			    console.error("error in posting");
        			})
    		}
	}]);
