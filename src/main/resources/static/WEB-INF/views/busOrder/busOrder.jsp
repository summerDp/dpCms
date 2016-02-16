<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css"
	href="${basePath}/css/function/enterpeise.css">


<!--[if IE 7]>
	<link rel="stylesheet" href="../../css/font-awesome-ie7.min.css">
<![endif]-->
<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      	<script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      	<script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->

<div ng-controller="busOrderCtrl">
	<div class="am-btn-group">
		<button type="button"
			class="am-btn am-btn-secondary am-round am-btn-xs"
			ng-click="tableParams.reload()">
			<i class="am-icon-refresh"></i> 刷新
		</button>
		<button type="button" class="am-btn am-btn-success am-round am-btn-xs"
			ng-click="toggleAddTableData()">
			<i class="am-icon-plus"></i>新增
		</button>
	</div>
	<div>在这里显示表格</div>
	<simple-table show-filter="true"
		data-url="${basePath}/busOrder/find.do"  >
		<col title="ID" name="id"  filter="{'id':'text'}" sortable="'id'" sort-indicator="'span'"  ></col>
		<col title="名字" name="name"  filter="{'name_like':'text'}" 
			sortable="'name'" sort-indicator="'span'" ></col>
	</simple-table>
	

	<!--  新增 -->
	<div ng-if="isItemShow(operData)" class="container">
		<bus-order-edit item="operData"
			saveurl="${basePath}/busOrder/save.do"></bus-order-edit>
	</div>

	<!-- 以下为列表代码 -->
	<table ng-table="tableParams" show-filter="true"
		class="ng-table-responsive am-table am-table-bordered am-table-striped"
		id="table">
		<keyvalue dataurl="${basePath}/busOrder/find.do"
			delurl="${basePath}/busOrder/delete.do"></keyvalue>

		<tbody>
			<tr id="tr{{item.id}}" ng-repeat-start="item in $data">
				<td width="30" style="text-align: left"
					header="'ng-table/headers/checkbox.html'">
					<input type="checkbox" ng-model="checkboxes.items[item.id]" />
				</td>
				<td data-title="'ID'" filter="{ 'id': 'text' }">
					{{item.id}}
				</td>
				<td data-title="'委托单编号'" filter="{ 'busNum_like': 'text' }"
					sortable="'busNum'" sort-indicator="'span'">
					{{item.busNum}}
				</td>
				<td data-title="'委托单名称'" filter="{ 'name_like': 'text' }"
					sortable="'name'" sort-indicator="'span'">
					{{item.name}}
				</td>
				<td data-title="'委托单备注'" filter="{ 'remark_like': 'text' }"
					sortable="'remark'" sort-indicator="'span'">
					{{item.remark}}
				</td>
				

				<td data-title="'修改'">
					<button type="button"
						class="am-btn am-btn-warning am-radius am-btn-xs"
						ng-click="toggleEditTableData(item)">
						<i class="am-icon-pencil"></i>
					</button>
				</td>
				<td data-title="'删除'">
					<button type="button"
						class="am-btn am-btn-danger am-radius am-btn-xs"
						ng-click="delTableData(item)">
						<i class="am-icon-trash"></i>
					</button>
				</td>
			</tr>
			<!--  编辑 -->
			<tr ng-repeat-end ng-if="isItemShow(item)">
				<td colspan="9">
					<bus-order-edit item="item"
						saveurl="${basePath}/busOrder/save.do"></bus-order-edit>
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
			<div class="am-modal-hd">
				华正大数据提示您
			</div>
			<div class="am-modal-bd">

			</div>
			<div class="am-modal-footer">
				<span class="am-modal-btn" ng-click="tableParams.reload()">确定</span>
			</div>
		</div>
	</div>


	
	<div class="am-modal am-modal-confirm" tabindex="-1" id="my-confirm">
		<div class="am-modal-dialog">
			<div class="am-modal-hd">
				Amaze UI
			</div>
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





