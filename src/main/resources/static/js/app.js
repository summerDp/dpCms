var app = angular.module('app', ['ngTable', 'ngResource', 'ngTableResizableColumns', 'ngTableExport', 'ui.select2','ngRoute']);



app.config([ '$routeProvider', function($routeProvider) {
	for ( var i in menus) {
		$routeProvider.when('/' + menus[i].id, {
			templateUrl : basePath + menus[i].templateUrl,
			controller :menus[i].controller
		})
	}
	$routeProvider.otherwise({
		redirectTo : '/1'
	});
} ]);




var YHUtil = {
	hasData: function(data) {
		var hasData = false;
		if (data == undefined || data == null) {
			hasData = false;
		} else if (data.length < 1) {
			hasData = false
		} else {
			hasData = true;
		}
		return hasData;
	},
	hasObj: function(data) {
		var hasData = false;
		if (data == undefined || data == null) {
			hasData = false;
		} else {
			hasData = true;
		}
		return hasData;
	}
}

