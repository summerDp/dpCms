app.controller('busOrderCtrl', function($scope, $filter, NgTableParams,
		$resource, $q, $http, $sce, tableDataService) {

	tableDataService.initScope($scope);

	$scope.vaildDataHandle = function(data) {
		var result = YHUtil.hasData(data.ctx) && YHUtil.hasData(data.showTime)
				&& YHUtil.hasData(data.goodCnt);
		return true; 
	}
	
	var tableFilter = null;
	// 统一默认的调用方法,基本不需要修改
	tableDataService.initTable($scope, NgTableParams, $resource, $http,
			tableFilter, vaildDataHandle, $filter);
	
	

});

app.directive('busOrderEdit', function() {
	return {
		restrict : 'E',
		scope : {
			item : '=',
			saveurl : '@'
		},
		replace : true,
		templateUrl : basePath + '/js/function/busOrder/busOrderEdit.html',
		controller : function($scope, $http, tableDataService) {
			$scope.save = function(operData) {
				tableDataService.saveHandle($scope, $http, operData);
			};
		}
	};
});

app.directive('addClientAddr', function() {
	return {
		restrict : 'E',
		scope : {
			model : '=',
			showMsg: '@'
		},
		replace : true,
		templateUrl : basePath + '/js/function/busOrder/clientAddrEdit.html',
		controller : function($scope, $http, tableDataService) {
			$scope.save = function(operData) {//保存客户的联系地址
				//tableDataService.saveHandle($scope, $http, operData);
			};
			
			var tableParams = new NgTableParams({
				page: 1, // show first page
				count: 100, // count per page
				filter: {},
				sorting: {}
			}, {
				counts: [50, 100, 200],
				paginationMaxBlocks: 9,
				total: data.data.length, // length of data
				getData: handleData
			});
			
			$scope.tableParams = tableParams;
			
			
		}
	};
});