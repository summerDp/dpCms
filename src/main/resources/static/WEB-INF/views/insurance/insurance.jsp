<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

		<style type="text/css">
			i {
				margin-right: 5px;
			}
			body {
				font-size: 12px;
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

		<div ng-controller="insuranceCtrl" class="am-center container">
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
				<insuranceedit item="operData" saveurl="${basePath}/insurance/save.do" ></insuranceedit>
			</div>
			<!-- 以下为列表代码 -->
			<table ng-table="tableParams" show-filter="true" class="ng-table-responsive am-table am-table-bordered am-table-striped" id="table">
				<!--<div ng-show="false" dataurl="/yhwlsys_base/weibo/find.do"></div>-->
				<keyvalue dataurl="${basePath}/insurance/find.do"  delurl="${basePath}/insurance/delete.do"></keyvalue>

				<tbody>
					<tr id="tr{{item.id}}" ng-repeat-start="item in $data">
						<td width="30" style="text-align: left" header="'ng-table/headers/checkbox.html'" >
						<!--  filter="{ 'id': 'id' }"  -->
							<input type="checkbox" ng-model="checkboxes.items[item.id]" />
						</td>
						<td data-title="''"  filter="{ 'id': 'text' }" >
							{{item.id}}
						</td>
						<td data-title="''"  filter="{ 'insuranceNo': 'text' }" >
							{{item.insuranceNo}}
						</td>
						<td data-title="''"  filter="{ 'enterpriseId': 'text' }" >
							{{item.enterpriseId}}
						</td>
						<td data-title="''"  filter="{ 'insuranceWorth': 'text' }" >
							{{item.insuranceWorth}}
						</td>
						<td data-title="''"  filter="{ 'insuranceMoney': 'text' }" >
							{{item.insuranceMoney}}
						</td>
						<td data-title="''"  filter="{ 'insuranceTypes': 'text' }" >
							{{item.insuranceTypes}}
						</td>
						<td data-title="''"  filter="{ 'insureTimes': 'text' }" >
							{{item.insureTimes}}
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
							<insuranceedit item="item" saveurl="${basePath}/insurance/save.do"></insuranceedit>
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
       
