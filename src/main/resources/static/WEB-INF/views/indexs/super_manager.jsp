<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html class="no-js" ng-app="app">

	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="description" content="">
		<meta name="keywords" content="">
		<meta name="viewport"
			content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		<title>超级管理员管理页面</title>
		
		<!-- Set render engine for 360 browser -->
		<meta name="renderer" content="webkit">

		<!-- No Baidu Siteapp-->
		<meta http-equiv="Cache-Control" content="no-siteapp" />

		<link rel="icon" type="image/png" href="${basePath}/img/favicon.png">

		<!-- Add to homescreen for Chrome on Android -->
		<meta name="mobile-web-app-capable" content="yes">
		<link rel="icon" sizes="192x192"
			href="${basePath}/app-icon72x72@2x.png">

		<!-- Add to homescreen for Safari on iOS -->
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="apple-mobile-web-app-status-bar-style" content="black">
		<meta name="apple-mobile-web-app-title" content="Amaze UI" />
		<link rel="apple-touch-icon-precomposed"
			href="pages/assets/i/app-icon72x72@2x.png">

		<!-- Tile icon for Win8 (144x144 + tile color) -->
		<meta name="msapplication-TileImage"
			content="${basePath}/app-icon72x72@2x.png">
		<meta name="msapplication-TileColor" content="#0e90d2">

		<link rel="stylesheet" href="${basePath}/css/amazeui.css">
		<link rel="stylesheet" type="text/css"
			href="${basePath}/css/bootstrap.css">
			<link rel="stylesheet" type="text/css"
			href="${basePath}/css/common/common.css">
			
		<style type="text/css">
.am-offcanvas .am-offcanvas-bar {
	width: 170px;
}

.am-offcanvas-content {
	padding: 15px 0;
}

.am-offcanvas-content ul {
	margin: 0;
	padding: 0;
	display: block;
	clear: both;
}

.am-offcanvas-content ul li {
	margin: 0;
	padding: 0;
	text-align: center;
	display: block;
	clear: both;
	height: 30px;
	line-height: 30px;
}

.ng-view {
	position: relative;
	margin-left: 100px;
}

.toggleMenu {
	position: absolute;
	left: 0;
	top: 0;
	z-index: 1;
}

.ngview {
	clear: both;
	overflow: hidden;
	width: 100%;
}

body {
	font-size: 12px;
}

.am-btn-group i {
	margin-right: 5px;
}

.header {
	background-color: #F9F9F9
}
</style>
	</head>

	<body>

		<!-- 侧边栏内容 -->
		<div id="my-offcanvas" class="am-offcanvas">
			<div class="am-offcanvas-bar">
				<div class="am-offcanvas-content">
					<ul>
						<c:forEach items="${menu}" var="item">
							<li>
								<a href="#/${item.id}"> ${item.name}</a>
							</li>
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>
		<!-- 按钮触发器， 需要指定 target -->
		<button class="am-btn am-btn-danger toggleMenu"
			data-am-offcanvas="{target: '#my-offcanvas', effect: 'push'}">
			<i class="am-icon-align-left"></i>
		</button>
		<div class="container">
	
			<jsp:include page="../header.jsp" />
			<div class="ngview" ng-view></div>
		</div>




		<!--[if (gte IE 9)|!(IE)]><!-->
		<script src="${basePath}/js/lib/jQuery/jquery.min.js">
</script>
		<!--<![endif]-->
		<!--[if lte IE 8 ]>
			<script src="http://libs.baidu.com/jquery/1.11.3/jquery.min.js"></script>
			<script src="http://cdn.staticfile.org/modernizr/2.8.3/modernizr.js"></script>
			<script src="assets/js/amazeui.ie8polyfill.min.js"></script>
		<![endif]-->

</script>
		<script src="${basePath}/js/utils/jsMethod.js" type="text/javascript"
			charset="utf-8">
</script>
		<script src="${basePath}/js/utils/jQueryMethod.js"
			type="text/javascript" charset="utf-8">
</script>
		<script src="${basePath}/js/lib/angular/angular.yh.js"
			type="text/javascript" charset="utf-8">
</script>
		<script src="${basePath}/js/lib/plugin/angular-route.js"
			type="text/javascript" charset="utf-8">
</script>


		<script src="${basePath}/js/lib/plugin/My97DatePicker/WdatePicker.js"
			type="text/javascript" charset="utf-8">
</script>
		<script src="${basePath}/js/lib/plugin/select2/select2.js"
			type="text/javascript" charset="utf-8">
</script>
		<script
			src="${basePath}/js/lib/plugin/angular-ui-select2/src/select2.js">
</script>
		<script src="${basePath}/js/lib/plugin/ng-table.min.js"
			type="text/javascript" charset="utf-8">
</script>
		<script src="${basePath}/js/lib/plugin/ng-table-export.src.js"
			type="text/javascript" charset="utf-8">
</script>
		<script src="${basePath}/js/lib/plugin/ng-table-resizable-columns.js"
			type="text/javascript" charset="utf-8">
</script>
		<script src="${basePath}/js/lib/plugin/angular-resource.min.js"
			type="text/javascript" charset="utf-8">
</script>


		<script type="text/javascript" charset="utf-8">
		basePath = "${basePath}";
		menus = [];
		<c:forEach items="${menu}" var="item" >
			menus.push({
						templateUrl: '${item.url}',
						id:${item.id}, 
						controller:'${item.ctrlName}' 
						});
		</c:forEach>
		dictMap = {};
		<c:forEach items="${dictMap}" var="dict">
			<c:if test="${!empty dict.id}">
				dictMap[${dict.id}]='${dict.name}';
			</c:if>
		</c:forEach>
		console.info('dictMap=',dictMap);
	</script>
		<script src="${basePath}/js/app.js" type="text/javascript"
			charset="utf-8">
		</script>
		<script src="${basePath}/js/directives.js" type="text/javascript"
			charset="utf-8">
		</script>
		<script src="${basePath}/js/tableService.js" type="text/javascript"
			charset="utf-8">
		</script>
		<script src="${basePath}/js/directives_simpleTable.js" type="text/javascript"
			charset="utf-8">
		</script>
		<script src="${basePath}/js/simpleTable.js" type="text/javascript"
			charset="utf-8">
		</script>
		
		<c:forEach items="${menu}" var="item">
			<c:if test="${!empty item.jsUrl}">
			<script src="${basePath}/${item.jsUrl}" type="text/javascript" charset="utf-8"></script>
			</c:if>
		</c:forEach>

		<script src="${basePath}/js/lib/amazeui/amazeui.min.js"
			type="text/javascript" ></script>
		<script type="text/javascript">

$(function() {
	var id = '#my-offcanvas';
	var $myOc = $(id);
	$('.doc-oc-js').on('click', function() {
		$myOc.offCanvas($(this).data('rel'));
	});
	$(".am-offcanvas-content ul li").on('click', function() {
		$('#my-offcanvas').offCanvas('close')
	});
});
</script>
	</body>

</html>