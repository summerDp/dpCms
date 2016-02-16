app.directive('tagselect', function() {
	return {
		restrict: 'E',
		scope: {
			url: '@',
			sltmodel: '=',
			showName: '@',
			mutl: '@',
			idName: '@'
		},
		templateUrl: '../tags/tagsSelect.html',
		link: function(scope, element, attrs) {
		},
		controller: DefaultSelectTableCtrl
	}
});
