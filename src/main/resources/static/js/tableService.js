app.provider('tableDataService', function() {
	var getPageCountAndSortingConf = function(tableFilter) { //翻页与排序配置
		if (tableFilter == undefined || tableFileter == null) {
			tableFilter = {};
		}

		return {
			page: 1, // show first page
			count: 100, // count per page
			filter: tableFilter,
			sorting: {}
		}
	};

	var getHandleDataConf = function(handleData) {
		return { //返回数据配置
			counts: [100, 200, 500],
			paginationMaxBlocks: 20,
			total: 0, // length of data
			getData: handleData //输入定义
		}
	};

	var testFun = function() {
		//console.info('testFun');
	};
	//保存处理器
		//$scope.save =
	var saveHandle = function($scope,$http,operData) {
		console.info('startToSave...saveUrl=', $scope.saveurl);
		$scope.operData = $.convertObject2Params(operData);
		//console.info('startToSave...$scope.operData=', $scope.operData);
		$http.get($scope.saveurl, {
			params: $scope.operData
		}).success(function(response) {
			$("#my-alert .am-modal-bd").html(response.message);
			var $modal = $('#my-alert').modal();
			if (YHUtil.hasObj(response.data)) {
				$scope.isAdd = !$scope.isAdd;
				$scope.operData = response.data;
				$scope.operData.isShowEdit=false;
			}
			//刷新表格
			//$scope.tableParams.reload();
			$scope.operData = response.data;
			$scope.showSaveBtn = false;
			//$scope.toggleAdd();
		}).error(function() {
			//console.info('error');
			$scope.saveMsg = '连接不成功.';
		});
	}

	function initSelectTable($scope, NgTableParams, $resource, $http, tableFilter, $filter) {
		//初始化选择框对象
		function initCheckboxes() {
				$scope.checkboxes = {
					'checked': false,
					items: {}
				}
			}
			//		$scope.$on('dataurl', function(event, dataurl) {
			//			console.info('dataurl=', dataurl);
			//			$scope.dataurl = dataurl;
			//		});
		
		function handleData($defer, params) { //刷新数据方法
			var dataUrl = $scope.dataurl;

			console.info('$scope.dataurl=', dataUrl);
			if (!YHUtil.hasObj(dataUrl)) {
				return undefined;
			}
			var Api = $resource(dataUrl);
			var paramInUrl = params.yhUrl();
			//paramInUrl.add("ctx=");
			//console.info(paramInUrl);
			return Api.get(paramInUrl).$promise.then(function(respData) {
				if (respData == null || respData == undefined) {
					return;
				}
				if (respData.data == null || respData.data == undefined) {
					return;
				}
				
				//$scope.data = respData.data.content;
				if($scope.handleDataInCtrl != undefined){
					$scope.data = $scope.handleDataInCtrl(respData.data.content);
				}else{
					$scope.data = respData.data.content;
				}
				
				

				params.total(respData.data.totalElements);
				initCheckboxes();
				return $scope.data;
			});

		}

		function initTableParams() { //表格形式配置方法
			$scope.tableParams = new NgTableParams(
				getPageCountAndSortingConf(tableFilter),
				getHandleDataConf(handleData)
			);
		}

		function initTableCheackboxs() {
			initCheckboxes();
			// watch for check all checkbox
			$scope.$watch('checkboxes.checked', function(value) {
				angular.forEach($scope.data, function(item) {

					if (angular.isDefined(item.id)) {
						$scope.checkboxes.items[item.id] = value;
					}
				});
			});
			// watch for data checkboxes
			$scope.$watch('checkboxes.items', function(values) {
				if (!$scope.data) {
					return;
				}
				var checked = 0,
					unchecked = 0,
					total = $scope.data.length;
				angular.forEach($scope.data, function(item) {
					checked += ($scope.checkboxes.items[item.id]) || 0;
					unchecked += (!$scope.checkboxes.items[item.id]) || 0;
				});
				if ((unchecked == 0) || (checked == 0)) {
					$scope.checkboxes.checked = (checked == total);
				}
				// grayed checkbox
				angular.element(document.getElementById("select_all")).prop("indeterminate", (checked != 0 && unchecked != 0));
			}, true);
			//checkboxes end
		}

		initTableParams(); //启动ng-table参数数据
		initTableCheackboxs();
	}

	function initTable($scope, NgTableParams, $resource, $http, tableFilter, vaildDataHandle, $filter) {

		initSelectTable($scope, NgTableParams, $resource, $http, tableFilter, $filter);

		//		$scope.$on('saveurl', function(event, saveurl) {
		//			console.info('saveurl=', saveurl);
		//			$scope.saveurl = saveurl;
		//		});
		//		$scope.$on('delurl', function(event, delurl) {
		//			console.info('delurl=', delurl);
		//			$scope.delurl = delurl;
		//		});


		//统一验证数据,在键入数据时进行验证
		$scope.keyUpData = function(operData) {
			//console.info('keyUpData...operData=',operData);
			if (operData == null) {
				$scope.showSaveBtn = false;
				return;
			} else {
				$scope.showSaveBtn = true;
			}

			if (vaildDataHandle(operData)) {
				$scope.showSaveBtn = true;
			} else {
				$scope.showSaveBtn = false;
				return;
			}

		}

		//删除处理器
		var delHandle = function(data) {
			$http.post($scope.delurl+"?ids="+data.id, {
				
			}).success(function(response) {
				if (!YHUtil.hasObj(response)) {
					return;
				}
				$("#my-alert .am-modal-bd").html(response.message);
				var $modal = $('#my-alert').modal();
				$scope.delMsg = response.message;
			}).error(function() {
				$scope.delMsg = '连接不成功.';
			});
		}


		$scope.delTableData = function(data) {
			getDelTableDataTemp = data;
			$('#my-confirm').modal({
				relatedTarget: this,
				onConfirm: function confirm() {
					delHandle(getDelTableDataTemp);
				},
				onCancel: function() {

				}
			});
		};



		$scope.toggleAddTableData = function() {

//			if(!$scope.operData){
//				$scope.operData = $scope.defaltOperData;
//			}
			$scope.operData.isShowEdit=!$scope.operData.isShowEdit;
			//$scope.operData.isShowEdit=true;
			//$scope.data.push($scope.operData);
			//$scope.isAdd = !$scope.isAdd;
		}


		$scope.toggleEditTableData = function(data) {
//			$scope.operData = data;
//			$scope.isAdd = true;
			data.isShowEdit=!data.isShowEdit;
		}
		$scope.isItemShow = function(data) {
			//console.info('isItemShow data=',data);
			return data.isShowEdit;
		}
	}

	var initScope = function($scope) {
		//此配置用于显示新增/编辑窗口,默认不显示
		$scope.isAdd = false;
		//单页操作对像初始化
		$scope.operData = {};
		//是否存在保存键
		$scope.showSaveBtn = false;
		//字典对象
		$scope.dictMap = dictMap;

		$scope.defaltOperData = {
		};
	}

	return {
		runTestFun: testFun,
		$get: function() {
			return {
				getDefaultPageCountAndSortingConf: function() {
					return pageCountAndSortingConf;
				},
				getDefaultHandleDataConf: getHandleDataConf,
				initTable: initTable,
				initSelectTable: initSelectTable,
				initScope: initScope,
				saveHandle : saveHandle
					//提供方法结束
			};
		}
	}
});