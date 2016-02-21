var app = angular.module('dpCms', []);  

app.controller('loginCtrl', function($scope, $http , $httpParamSerializer) {
	$scope.formData = {};
	$scope.imgCodeSrc = "imgcode";
	$scope.msg = "";
	$scope.processForm = function() {
		console.log($scope.formData);
		console.log($httpParamSerializer($scope.formData));
		$http({
			method  : 'POST',
			url     : 'login',
			// pass in data as strings
			data    : $httpParamSerializer($scope.formData),
			// set the headers so angular passing info as form data (not request payload)
			headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  
		})
		.success(function(data) {
			console.log(data);
			if (data.stateCode == 1) {
				var uri = data.data;
				location.href = uri;
			}else{
				$scope.msg = data.message;
				$scope.changeImgCode();
			}
		});
	};

	$scope.changeImgCode = function() {
		$scope.imgCodeSrc = $scope.imgCodeSrc +"?"+getRandomSpan();
	}

	function getRandomSpan(){
		return Math.floor((Math.random()*60000)+1);
	};
});
