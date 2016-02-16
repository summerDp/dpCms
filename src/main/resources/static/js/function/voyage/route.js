app.controller('routeCtrl', function($scope, $filter, NgTableParams, $resource, $q, $http, $sce, tableDataService) {
	//初始化请求资源
	tableDataService.initScope($scope);
	//在此对输入数据进行验证,验证数据通过后,验证通过则返回true值
	var vaildDataHandle = function(data) {
		var result = YHUtil.hasData(data.ctx) && YHUtil.hasData(data.showTime) && YHUtil.hasData(data.goodCnt);
		return true;
	}

	//用于定义表格的过滤器,目前无需要直接传null
	var tableFilter = null;
	//统一默认的调用方法,基本不需要修改
	tableDataService.initTable($scope, NgTableParams, $resource, $http, tableFilter, vaildDataHandle,$filter);
});

app.directive("routeedit", function() {
	return {
		restrict : 'E',
		scope : {
			item : '=',
			saveurl : '@'
		},
		replace : true,
		templateUrl : basePath + '/js/function/voyage/routeEdit.html',
		controller : function($scope, $http, tableDataService) {
			$scope.save = function(operData) {
				tableDataService.saveHandle($scope, $http, operData);
			};
			
			$scope.refreshRoutePort = function(item) {
				if(item && item.id > 0) {
					$http.get(basePath + "/routePort/findList.do", {
						params: {
							"route.id": item.id,
							"orderParams[0].field": "portIndex",
							"rjdParams[0]" : "port"
						}
					}).success(function(resp) {
						item.routePorts = resp.data;
					}).error(function() {
						$scope.saveMsg = '连接不成功.';
					});
				}
				else {
					alert("没有数据可刷新");
				}
			};
			
			$scope.deleteRoutePort = function(node, item) {
				$http.get(basePath + "/routePort/deleteRoutePort.do", {
					params: {
						id: node.id,
						routeId: item.id
					}
				}).success(function(resp) {
					console.log(resp);
					item.routePorts = resp.data;
				}).error(function() {
					$scope.saveMsg = '连接不成功.';
				});
			};
			
			$scope.addRoutePort = function(item, index){
				if(item.routePorts instanceof Array) {
					item.routePorts.add(index, {});
				}
				else { item.routePorts = [{}]; }
			};
			
			$scope.saveRoutePort = function(node, item, index) {
				var params = $.convertObject2Params(item.routePorts[index], function(key, value){
					console.log("key: " + key);
					return true;
				});
				params["route.id"] = item.id;
				params.myIndex = index;
				$http.get(basePath + "/routePort/saveRoutePort.do", {
					params: params
				}).success(function(resp) {
					item.routePorts = resp.data;
				}).error(function() {
					$scope.saveMsg = '连接不成功.';
				});
			};
		},
		link: function($scope, elem, attrs) { }
	};
});