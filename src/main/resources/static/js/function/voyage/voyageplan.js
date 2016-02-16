app.controller('voyagePlanCtrl', function($scope, $filter, NgTableParams, $resource, $q, $http, $sce, tableDataService) {
		//初始化请求资源
		tableDataService.initScope($scope);
		//已经可以从页面进行定义
		//$scope.dataUrl = '/yhwlsys_base/weibo/find.do';
		//$scope.saveUrl = '/yhwlsys_base/weibo/addWeibo.do';


		//在此对输入数据进行验证,验证数据通过后,验证通过则返回true值
		var vaildDataHandle = function(data) {
			var result =
				YHUtil.hasData(data.ctx) && YHUtil.hasData(data.showTime) && YHUtil.hasData(data.goodCnt);

			return true;
		}
				
		//用于定义表格的过滤器,目前无需要直接传null
		var tableFilter = null;
		//统一默认的调用方法,基本不需要修改
		tableDataService.initTable($scope, NgTableParams, $resource, $http, tableFilter, vaildDataHandle,$filter);

});

app.directive('voyageplanedit', function() {
	return {
		restrict : 'E',
		scope : {
			item : '=',
			saveurl : '@'
		},
		replace : true,
		templateUrl : basePath + '/js/function/voyage/voyageplanEdit.html',
		controller : function($scope, $http, tableDataService) {
			$scope.save = function(operData) {
				var params = $.convertObject2Params(operData, function(key, value){
					if(key == "vesselVoyages") { return false; }
					return true;
				});
				tableDataService.saveHandle($scope, $http, params);
			};
			
			$scope.saveVslVoyage = function(node, item, index) {
				var isYes = window.confirm("您确定保存该船期？");
				if(!isYes) { return; }
				var isAdd = node.id? false : true;
				var params = $.convertObject2Params(node);
				if(isAdd) {
					var minId = -1000, maxId = -1000;
					var size = item.vesselVoyages.length;
					if(size > 1) {
						var lastIndex = size - 1;
						if(index < lastIndex) {  // 说明插在第一个位置
							minId = item.vesselVoyages[index + 1].id;
							maxId = item.vesselVoyages[lastIndex].id;
						}
					}
					params.minVslVoyId = minId;
					params.maxVslVoyId = maxId;
					params.myOrderIndex = index + 1;
				}
				params["voyagePlan.id"] = item.id;
				
//				console.log("params", params);
//				if(true){ return; }
				$http.get(basePath + "/vesselVoyage/saveVslVoyage.do", {
					params: params
				}).success(function(resp) {
					if(resp.errorcode == 1) {
						if(isAdd) {
							for(var i = index, s = item.vesselVoyages.length; i < s; ++i) {
								item.vesselVoyages[i].orderIndex = i + 1;
							}
							item.vesselVoyages[index].id = resp.data.id;
						}
					}
					else {
						alert("错误代码: %s, 错误信息: %s".jFormat(resp.errorcode, resp.message));
					}
				}).error(function() {
					$scope.saveMsg = '连接不成功.';
				});
			};
			
			$scope.selectRoute = function(item) {
				$http.get(basePath + "/routePort/findList.do", {
					params: {
						"route.id": item.id,
						"orderParams[0].field": "portIndex",
						"rjdParams[0]": "port"
					}
				}).success(function(resp) {
					if(resp.errorcode == 1) {
						var data = resp.data;
						for(var i = 0, size = data.length, routePort; i < size; ++i) {
							routePort = data[i];
							delete routePort.id;  // 把id删除
							routePort.portByPortId =  routePort.port;
							delete routePort.port;
						}
						$scope.item.vesselVoyages = data;
					}
					else {
						alert("错误代码:%s, 错误信息: %s".jFormat(resp.errorcode, resp.message));
					}
				}).error(function() {
					$scope.saveMsg = '连接不成功.';
				});
			};
			
			$scope.refreshVslVoy = function(item) {
				if(item && item.id > 0) {
					$http.get(basePath + "/vesselVoyage/findList.do", {
						params: {
							"voyagePlan.id": item.id,
							"orderParams[0].field": "orderIndex",
							"rjdParams[0]": "portByNextPortId",
							"rjdParams[1]": "portByPortId"
						}
					}).success(function(resp) {
						if(resp.errorcode == 1) {
							item.vesselVoyages = resp.data;
						}
						else {
							alert("错误代码:%s, 错误信息: %s".jFormat(resp.errorcode, resp.message));
						}
					}).error(function() {
						$scope.saveMsg = '连接不成功.';
					});
				}
				else {
					alert("没有数据可刷新");
				}
			};
			
			$scope.addVslVoyage = function(item, index) {
				if(item.vesselVoyages instanceof Array) {
					item.vesselVoyages.add(index, {});
				}
				else { item.vesselVoyages = [{}]; }
			};
			
			$scope.deleteVslVoyage = function(node, item, index) {
				var yesNo = window.confirm("您确定要删除该船期吗");
				if(!yesNo) { return; }
				var nodes = item.vesselVoyages;
				if(node.id) {
					var prevId = -1000;
					var nextId = -1000;
					if(nodes.length > 1) { // 只有一个，不用执行update，仅需delete
						if(index < nodes.length - 1) {
							prevId = nodes[index + 1].id,
							nextId = nodes[nodes.length - 1].id
						}
					}
					$http.get(basePath + "/vesselVoyage/deleteVslVoy.do", {
						params: {
							delVslVoyId: node.id,
							minVslVoyId: prevId,
							maxVslVoyId: nextId,
							voyPlanId: item.id
						}
					}).success(function(resp) {
						if(resp.errorcode == 1) {
							for(var i = index + 1, size = nodes.length; i < size; ++i) {
								nodes[i].orderIndex -= 1;
							}
							nodes.removeByIndex(index);
						}
						else {
							alert("错误代码:%s, 错误信息: %s".jFormat(resp.errorcode, resp.message));
						}
					}).error(function() {
						$scope.saveMsg = '连接不成功.';
					});
				}
				else {
					alert("该船期还没有保存，请点击刷新按钮或者删除按钮");
					if(item.vesselVoyages instanceof Array && item.vesselVoyages.length > 0) {
						item.vesselVoyages.removeByIndex(index);
					}
				}
			}
		}
	};
});
