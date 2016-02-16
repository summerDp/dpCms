/**
 * 
 */
var simpleTableServiceProvder = function() {
	
	var getDfTableParamConf = function(){
		return {
			page: 1, // show first page
			count: 100, // count per page
			filter: {},
			sorting: {}
		}
	}
	var getTableDataConf = function(handleData){
		return {
				counts: [50, 100, 200],
				paginationMaxBlocks: 9,
				total: 0, // length of data
				getData: handleData
			}
	}
	
	var getDefaultNgTableParam = function(handleData){
		var tableParams = new NgTableParams(getDfTableParamConf(),getTableDataConf(handleData));
		return tableParams;
	}
	
	
	var getDefaultDataHandle(tabInfo){
		var handleData = function($defer, params) { //刷新数据方法
			var dataUrl = tabInfo.dataUrl;
			var resource = tabInfo.$resource;
			
			if (!YHUtil.hasObj(dataUrl)) {
				return undefined;
			}
			var Api = resource(dataUrl);
			
			var paramInUrl = params.yhUrl();
			//paramInUrl.add("ctx=");
			//console.info(paramInUrl);
			if(tabInfo.paramUrlHandle){
				paramInUrl = tabInfo.paramUrlHandle(paramInUrl);
			}
			
			return Api.get(paramInUrl).$promise.then(function(respData) {
				if (respData == null || respData == undefined) {
					return;
				}
				if (respData.data == null || respData.data == undefined) {
					return;
				}
				
				//$scope.data = respData.data.content;
				if(tabInfo.handleDataInCtrl){
					tabInfo.data = tabInfo.handleDataInCtrl(respData.data.content);
				}else{
					tabInfo.data = respData.data.content;
				}
				
				params.total(respData.data.totalElements);
				tableInfo.initCheckboxes();
				return tabInfo.data;
			});

		};
		return handleData;
	}
	
	var initCheckboxes = function(){
		
	}
	
	var getTableInfo = function($scope,dataUrl,$resource,checkboxesName){
		var tabInfo = {
			"$scope", $scope,
			"dataUrl", 	dataUrl,
			"$resource", $resource,
			"checkboxes",checkboxesName,
			"initCheckboxes":function(){
				
			}
			//
		};
		
		return tabInfo;
	}
	
	return {
		$get : function(){
			return {
				getDfNgTableParam :　getDefaultNgTableParam,
				getDfTableParamConf : getDfTableParamConf,
				getTableDataConf :getTableDataConf，
				getDefaultDataHandle ：getDefaultDataHandle,
				getDataInfo :getDataInfo
			}
		}
	}
}

app.provider('simpleTableService', simpleTableServiceProvder);