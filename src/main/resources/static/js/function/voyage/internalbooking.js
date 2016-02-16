app.controller('internalBookingCtrl', function($scope, $filter, NgTableParams, $resource, $q, $http, $sce, tableDataService) {
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

		//以下可以重新自动定义各项操作方法,具体的操作代码可以从页面的click事件上找出方法名字,
		//	当然可以自已新写操作,总体样例,如下
		//		$scope.delTableData = function{
		//			//自定定删除代码,保存修改同理
		//		}

		$scope.doSth = function() {
			$scope.operData;

		}
	});

app.directive('internalbookingedit', function() {
	return {
		restrict : 'E',
		scope : {
			item : '=',
			saveurl : '@'
		},
		replace : true,
		templateUrl : basePath + '/js/function/voyage/internalbookingEdit.html',
		controller : function($scope, $http, tableDataService) {
			$scope.save = function(operData) {
				tableDataService.saveHandle($scope, $http, operData);
			};
			
			$scope.addVirtVslVoy = function(item, index){
				if(item.virtualVslVoyages instanceof Array) {
					item.virtualVslVoyages.add(index, {});
				}
				else { item.virtualVslVoyages = [{}]; }
			};
			
			$scope.refreshVirtVslVoy = function(item) {
				if(item && item.id > 0) {
					$http.get(basePath + "/virtualVslVoyage/findList.do", {
						params: {
							"internalBooking.id": item.id,
							"orderParams[0].field": "orderIndex",
							"rjdParams[0]": "portByDepPortId"
						}
					}).success(function(resp) {
						if(resp.errorcode == 1) {
							item.virtualVslVoyages = resp.data;
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
			
			$scope.saveVirtVslVoy = function(node, item, index) {
				console.log("saveVirtVslVoy");
			};
			
			$scope.deleteVirtVslVoy = function(node, item, index) {
				console.log("saveVirtVslVoy");
			}
		}
	};
});
