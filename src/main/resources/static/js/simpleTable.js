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
	
	
	var getDefaultDataHandle = function(){
		var tabInfo = this;
		var handleData = function($defer, params) { //刷新数据方法
			console.info('dfDataHandel tabInfo=',tabInfo);
			
			var dataUrl = tabInfo.dataUrl;
			var resource = tabInfo.$resource;
			
			console.info('dfDataHandel dataUrl=',dataUrl);
			
			if (!YHUtil.hasObj(dataUrl)) {
				return undefined;
			}
			var Api = tabInfo.resource(dataUrl);
			
			var paramInUrl = params.yhUrl();
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
				if(tabInfo.parseData){
					tabInfo.data = tabInfo.parseData(respData.data.content);
				}else{
					tabInfo.data = respData.data.content;
				}
				
				params.total(respData.data.totalElements);
				tabInfo.initCheckboxes();
				return tabInfo.data;
			});

		};
		return handleData;
	}
	
	var initCheckboxes = function(){
		this.scope[this.checkboxesName] = {
				'checked': false,
				items: {}
		}
	}
	function initTableCheackboxs() {
		this.initCheckboxes();
		// watch for check all checkbox
		console.info('initTableCheackboxs ',this.checkboxesName+'.checked');
		this.scope.$watch(this.checkboxesName+'.checked', function(value) {
			angular.forEach(this.data, function(item) {
				if (angular.isDefined(item.id)) {
					this.scope[this.checkboxesName].items[item.id] = value;
				}
			});
		});
		// watch for data checkboxes
		this.scope.$watch(this.checkboxesName+'.items', function(values) {
			if (!this.data) {
				return;
			}
			var checked = 0,
				unchecked = 0,
				total = this.data.length;
			angular.forEach(this.data, function(item) {
				checked += (this.scope[checkboxesName].items[item.id]) || 0;
				unchecked += (!this.scope[checkboxesName].items[item.id]) || 0;
			});
			if ((unchecked == 0) || (checked == 0)) {
				this.scope[checkboxesName].checked = (checked == total);
			}
			// grayed checkbox
			angular.element(document.getElementById(this.checkboxesName + "_select_all")).prop("indeterminate", (checked != 0 && unchecked != 0));
		}, true);
		//checkboxes end
	}
	
	var getTableInfo = function($scope,NgTableParam,checkboxesName,dataUrl,$resource){
		var tabInfo = {
			"scope":$scope,
			"dataUrl":	dataUrl,
			"resource": $resource,
			"checkboxesName":checkboxesName,
			"initCheckboxes":initCheckboxes,
			"initTableCheackboxs",initTableCheackboxs
			"data":null,
			"getDefaultDataHandle":getDefaultDataHandle,
			"ngTableParam": null,
			"initNgTableParam": function(){
				this.ngTableParam = 
					new NgTableParam(getDfTableParamConf(),getTableDataConf(this.getDefaultDataHandle()));
			},
			
			"init":function(){
				this.initNgTableParam();
				this.initTableCheackboxs();
			}
			
			//parseData 取数据后管理
			//paramUrlHandle 请求数据前参数处理
		};
		
		return tabInfo;
	}
	
	return {
		$get : function(){
			return {
				getTableInfo :getTableInfo
			}
		}
	}
}

app.provider('simpleTableService', simpleTableServiceProvder);