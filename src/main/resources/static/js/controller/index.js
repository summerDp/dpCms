dpCmsApp.controller('indexCtrl', function($scope,checkLoginStateService){
	$scope.checkLoginState = function() {
		var promise  = checkLoginStateService.checkLoginState();
		promise.then(function(result) { 
			if(result.data.data==false) location.href="door.html";
			$scope.userName = result.data.data.employee.name;
		});

	}
});


