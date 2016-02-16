app.directive('userselect', function() {
	return {
		restrict: 'E',
		scope: {
			url: '@',
			sltmodel: '=',
			showName: '@',
			mutl: '@',
			idName: '@'
		},
		templateUrl: '../user/usersSelect.html',
		link: function(scope, element, attrs) {

		},
		controller: DefaultSelectTableCtrl
	}
});