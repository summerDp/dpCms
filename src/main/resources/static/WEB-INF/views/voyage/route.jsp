<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="${basePath}/css/function/route.css">
		<style type="text/css">
			i { margin-right: 5px; }
			body { font-size: 12px; }
			
			.overdiv {
				width:290px;
				height:390px;
				overflow:hidden;
			}
			.overdiv ul{
				list-style:none;
				min-height:390px;
				display: flex;
			}
			.overdiv li{
				float:left;
				display:inline;
				min-width:60px;
				width: auto;
				height:350px;
				margin-top: 5px;
				margin-right:25px;
				margin-bottom:5px;
				text-align:center;
				line-height:80px;
				
				
			}
			
			.overdiv li.portBox {
				margin-left:75px;
				background-color: #ADDC4B;
				padding: 1px;
				border-radius: .7em;
				-webkit-box-sizing: border-box;
				-moz-box-sizing: border-box;
				-o-box-sizing: border-box;
				box-sizing: border-box;
				border: 1px solid #86AD5A;
				box-shadow: 0 0 5px rgba(0, 0, 0, 0.43);
			}
			
			div#overdiv li div.btnPlus {
				margin-top:160px;
				width:50px;
				height:auto;
				float:left;
				display:inline-block;
				margin-left:-75px;
			};
			
			div#overdiv label.myLabel {
				margin: 10px auto !important;
			}
			
			.overdiv li cite {
			    background: #28bf4c none repeat scroll 0 0;
			    display: inline-block;
			    height: 6px;
			    left: -200px;
			    position: absolute;
			    top: 31px;
			    width: 210px;
			}
			div.selectDiv {
				height: 480px;
				width:280px !important;
				overflow-y:auto;
			}
		</style>
		<!--[if IE 7]>
			<link rel="stylesheet" href="../../css/font-awesome-ie7.min.css">
			<![endif]-->
			<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
			<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
			<!--[if lt IE 9]>
	      	<script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
	      	<script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
	    <![endif]-->

		<div ng-controller="routeCtrl" class="am-center container">
			<div class="am-btn-group">
				<button type="button" class="am-btn am-btn-secondary am-round am-btn-xs" ng-click="tableParams.reload()">
					<i class="icon-refresh"></i>刷新
				</button>
				<button type="button" class="am-btn am-btn-success am-round am-btn-xs" ng-click="toggleAddTableData()">
					<i class="icon-plus"></i>新增
				</button>
			</div>
			<!--  新增 -->
			<div ng-show="isItemShow(operData)" class="container">
				<routeedit item="operData" saveurl="${basePath}/route/save.do"></routeedit>
			</div>
			<!-- 以下为列表代码 -->
			<table ng-table="tableParams" show-filter="true" class="ng-table-responsive am-table am-table-bordered am-table-striped" id="table">
				<!--<div ng-show="false" dataurl="/yhwlsys_base/weibo/find.do"></div>-->
				<keyvalue dataurl="${basePath}/route/find.do"  delurl="${basePath}/route/delete.do"></keyvalue>

				<tbody>
					<tr id="tr{{item.id}}" ng-repeat-start="item in $data">
						<td width="30" style="text-align: left" header="'ng-table/headers/checkbox.html'" >
						<!--  filter="{ 'id': 'id' }"  -->
							<input type="checkbox" ng-model="checkboxes.items[item.id]" />
						</td>
						<td data-title="'航线名称'"  filter="{ 'name': 'text' }" >
							{{item.name}}
						</td>
						<td data-title="'航线代号'"  filter="{ 'code': 'text' }" >
							{{item.code}}
						</td>
						<td data-title="'是否锁定'"  filter="{ 'lockStatus': 'text' }" >
							{{item.lockStatus? "是" : "否"}}
						</td>
						<td data-title="'备注'"  filter="{ 'remark': 'text' }" >
							{{item.remark}}
						</td>
						
						<td data-title="'修改'">
							<button type="button" class="am-btn am-btn-warning am-radius am-btn-xs" ng-click="toggleEditTableData(item)">
								<i class="icon-pencil"></i>修改
							</button>
						</td>
						<td data-title="'删除'">
							<button type="button" class="am-btn am-btn-danger am-radius am-btn-xs" ng-click="delTableData(item)">
								<i class="icon-trash"></i>删除
							</button>
						</td>
					</tr>
					<!--  编辑 -->
					<tr  ng-repeat-end ng-show="isItemShow(item)" >
						<td colspan="7">
							<routeedit item="item" saveurl="${basePath}/route/save.do"></routeedit>
						</td>
					</tr>
				</tbody>
			</table>
			<!-- 以上为列表代码 -->
			<script type="text/ng-template" id="ng-table/headers/checkbox.html">
				<input type="checkbox" ng-model="checkboxes.checked" id="select_all" name="filter-checkbox" value="" />
			</script>
			<div class="am-modal am-modal-alert" tabindex="-1" id="my-alert">
				<div class="am-modal-dialog">
					<div class="am-modal-hd">华正大数据提示您</div>
					<div class="am-modal-bd">

					</div>
					<div class="am-modal-footer">
						<span class="am-modal-btn" ng-click="tableParams.reload()">确定</span>
					</div>
				</div>
			</div>
			<div class="am-modal am-modal-confirm" tabindex="-1" id="my-confirm">
				<div class="am-modal-dialog">
					<div class="am-modal-hd">Amaze UI</div>
					<div class="am-modal-bd">
						你，确定要删除这条记录吗？
					</div>
					<div class="am-modal-footer">
						<span class="am-modal-btn" data-am-modal-cancel>取消</span>
						<span class="am-modal-btn" data-am-modal-confirm>确定</span>
					</div>
				</div>
			</div>
		</div>
       
