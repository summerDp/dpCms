app
		.directive(
				"yhselect",
				function($http, $q) {
					return {
						restrict : 'E',
						require : 'ngModel',
						scope : {
							dataurl : '@',
							ngModel : '=ngModel',
							multiple : '@',
							selsectconfig : '@',
							optioncontent : '@',
							optionvalue : '@',
							dataPlaceholder : '@'
						},
						replace : true,
						template : function(tElement, tAttrs) {
							var optioncontent = tAttrs.optioncontent;
							var optionvalue = tAttrs.optionvalue;
							var multiple = tAttrs.multiple;

							if (multiple != undefined) {
								// console.log($scope);
							}

							var _html = '<select ui-select2 class="populate placeholder" >'
									+ '<option>{{dataPlaceholder}}</option>'
									+ '<option ng-repeat="option in optionArray" value="{{option.'
									+ optionvalue
									+ '}}"'
									+ 'ng-selected="ngModel == option.'
									+ optionvalue
									+ '">'
									+ '{{option.'
									+ optioncontent
									+ '}}'
									+ '</option>'
									+ '</select>';

							return _html;
						},
						link : function(scope, iele, iattr) {
							$http
									.get(scope.dataurl)
									.then(
											function resolved(res) {
												scope.optionArray = res.data.data.content;
											});
						},
					}
				});

app.directive('keyvalue', function() {
	// console.info('startKeyValueDirective...');
	return {
		restrict : 'E',
		scope : false,
		link : function(scope, element, attrs) {
			//console.info('keyvalue link scope=',scope);
			for ( var i in attrs) {
				//console.info('keyvalue link attrs[i]=',attrs[i]);
				scope[i] = attrs[i];
			}
		}
	}
});

// 选择框控件器(开始):自编写的选择框中的列表需要用到这个控件器,此控制器管理表格中的数据,实现是否选中与数据输出
var DefaultSelectTableCtrl = function($scope, NgTableParams, $resource, $http,
		tableDataService) {
	// 从数组中通过id删除指定值 开始
	Array.prototype.indexOfById = function(val) {
		for (var i = 0; i < this.length; i++) {
			if (this[i].id == val.id)
				return i;
		}
		return -1;
	};
	Array.prototype.removeById = function(val) {
		var index = this.indexOfById(val);
		if (index > -1) {
			this.splice(index, 1);
		}
	};
	// 从数组中删除指定值 结束
	tableDataService.initScope($scope, NgTableParams, $resource, $http);
	var tableFilter = null;
	tableDataService.initSelectTable($scope, NgTableParams, $resource, $http,
			tableFilter);
			
	$scope.handleDataInCtrl = function(data){
		if(($scope.handleOptionData) && typeof($scope.handleOptionData)=='function'){
			data = $scope.handleOptionData(data);
		}
			var newdata = [];
			
			if(data == null || data == undefined){
				return newdata;
			}
			
			var nullItem = {
				id:-1
			};
			
			nullItem[$scope.showName]= "<空>";
//			console.info("nullItem=", nullItem);
			
			newdata.push(nullItem);
			
			for(var i in data){
				if(typeof(data[i])!='function'){
					newdata.push(data[i]);
				}
			}
			return newdata;
	}
	
	
	
	$scope.SelectDivDispalyFlag= false;//设置选择框是默认不出现的
	
	$scope.toggleSelectDivDispaly = function() {
		var tempFlag = Boolean($scope.SelectDivDispalyFlag);
		$scope.SelectDivDispalyFlag = (!tempFlag);
	}
	$scope.hiedecloseselectdiv = function() {
		$scope.SelectDivDispalyFlag = false;
	}
	

	$scope.selcectCurrentTr = function(data) {
		if (data == null && data == undefine) {
			return;
		}
		if ($scope.mutl == 'false') {
			$scope.hiedecloseselectdiv();//单选选择后选择框消失
			if(data.id == -1){
				$scope.sltmodel = null;
				return;
			}
			$scope.sltmodel = data;
		} else {
			if (YHUtil.hasObj($scope.sltmodel)) {
			} else {
				$scope.sltmodel = [];
			}
			var isHasObj = false;
			for ( var i in $scope.sltmodel) {
				var value = $scope.sltmodel[i];
				if (value[$scope.idName] == data[$scope.idName]) {
					isHasObj = true;
					break;
				}
			}
			//console.log("开始增删前的数组是：", $scope.sltmodel);
			if (!isHasObj) {
				$scope.sltmodel.push(data);
			} else {
				//console.log("被删除的对象是：", data);
				$scope.sltmodel.removeById(data);
			}
			//console.log("完成增删后的数组是：", $scope.sltmodel);
		}
	};

	$scope.isSelected = function(item) {
		if ($scope.sltmodel == undefined || $scope.sltmodel == null) {
			return false;
		}
		if ($scope.mutl != 'true') {
			return $scope.sltmodel.id== item.id;
		} else {
			var isHasObj = false;
			for ( var i in $scope.sltmodel) {
				var value = $scope.sltmodel[i];
				
				if(value == null || value == undefined){
					continue;
				}
				//console.info('value=',value);
				if (value[$scope.idName] == item[$scope.idName]) {
					isHasObj = true;
					break;
				}
			}
			return isHasObj;
		}
	}
	$scope.delSelected =function(item){
		
	}

};
// 选择框控件器(结束):自编写的选择框中的列表需要用到这个控件器,此控制器管理表格中的数据,实现是否选中与数据输出
/*
 * 时间选择器指令(开始)
 */
app.directive('datePicker', function() {
	return {
		restrict : 'A',
		require : 'ngModel',
		scope : {
			minDate : '@',
			dateFmt : '@',
			minDate : '@',
			maxDate : '@'
		},
		link : function(scope, element, attr, ngModel) {
			element.val(ngModel.$viewValue);

			function onpicking(dp) {
				var date = dp.cal.getNewDateStr();
				scope.$apply(function() {
					ngModel.$setViewValue(date);
				});
			}
			element.bind('click', function() {
				WdatePicker({
					onpicking : onpicking,
					dateFmt : scope.dateFmt,
					minDate : scope.minDate,
					maxDate : scope.maxDate
				})
			});
		}
	};
});
/*
 * 时间选择器指令(结束)
 */


app.directive(
		'yhselectTable',
		function() {
			return {
				restrict : 'E',

				scope : {
					url : '@',
					sltmodel : '=',
					dataurl :'@',
					mutl : '@',
					idName : '@',
					showName : '@',
					filterField:'=',
					showTitle:'@',
					required:'@',
					afterSelect: '=',
					paraUrlHandel:'&'
				},
				replace:true,
				templateUrl : basePath + '/js/function/common/yhselectTable.html',
				link : function(scope, element, attrs) {
				},
				controller :  function($scope, NgTableParams, $resource, $http,
						tableDataService) {
					//console.info('yhselectTable start... $scope', $scope);
					// 从数组中通过id删除指定值 开始
					Array.prototype.indexOfById = function(val) {
						for (var i = 0; i < this.length; i++) {
							if (this[i].id == val.id)
								return i;
						}
						return -1;
					};
					Array.prototype.removeById = function(val) {
						var index = this.indexOfById(val);
						if (index > -1) {
							this.splice(index, 1);
						}
					};
					// 从数组中删除指定值 结束
					tableDataService.initScope($scope, NgTableParams, $resource, $http);
					var tableFilter = null;
					tableDataService.initSelectTable($scope, NgTableParams, $resource, $http,
							tableFilter);
							
					
					$scope.handleDataInCtrl = function(data){
						if(($scope.handleOptionData) && typeof($scope.handleOptionData)=='function'){
							data = $scope.handleOptionData(data);
						}
						var newdata = [];
						
						if(data == null || data == undefined){
							return newdata;
						}
						
						var nullItem = {
							id:-1
						};
						nullItem[$scope.showName]= "<空>";

						newdata.push(nullItem);
						
						for(var i in data){
							if(typeof(data[i])!='function'){
								newdata.push(data[i]);
							}
						}
						return newdata;
					}
					
					$scope.SelectDivDispalyFlag= false;//设置选择框是默认出现的
					
					$scope.toggleSelectDivDispaly = function() {
						var tempFlag = Boolean($scope.SelectDivDispalyFlag);
						$scope.SelectDivDispalyFlag = (!tempFlag);
					}
					$scope.hiedecloseselectdiv = function() {
						$scope.SelectDivDispalyFlag = false;
					}
					

					$scope.selcectCurrentTr = function(data) {
//						console.info('selcectCurrentTr=',data);
						if (data == null || data == undefined ) {
							return;
						}
						
						if ($scope.mutl != 'true') {
							if(data.id == -1){
								$scope.sltmodel = null;
								return;
							}
							$scope.sltmodel = data;
						} else {
							if (YHUtil.hasObj($scope.sltmodel)) {
							} else {
								$scope.sltmodel = [];
							}
							var isHasObj = false;
							for ( var i in $scope.sltmodel) {
								var value = $scope.sltmodel[i];
								if (value[$scope.idName] == data[$scope.idName]) {
									isHasObj = true;
									break;
								}
							}
							//console.log("开始增删前的数组是：", $scope.sltmodel);
							if (!isHasObj) {
								$scope.sltmodel.push(data);
							} else {
								//console.log("被删除的对象是：", data);
								$scope.sltmodel.removeById(data);
							}
							//console.log("完成增删后的数组是：", $scope.sltmodel);
						}
						if($scope.afterSelect) { $scope.afterSelect(data); }
					};

					$scope.isSelected = function(item) {
						if ($scope.sltmodel == undefined || $scope.sltmodel == null) {
							return false;
						}
						if ($scope.mutl != 'true') {
							return $scope.sltmodel.id == item.id;
						} else {
							var isHasObj = false;
							for ( var i in $scope.sltmodel) {
								var value = $scope.sltmodel[i];
								//console.info('vale=',value);
								//console.info('$scope.idName=',$scope.idName);
								if(value == null || value == undefined){
									continue;
								}
								if (value[$scope.idName] == item[$scope.idName]) {
									isHasObj = true;
									break;
								}
							}
							return isHasObj;
						}
					}
					$scope.delSelected =function(item){
						if (item == undefined || item == null){
							return
						}
						if($scope.mutl != 'true'){
							//单选
							 $scope.sltmodel = {};
						}else{
							//多选
							$scope.sltmodel.removeById(item);
						}
					}

				}
			}
});

