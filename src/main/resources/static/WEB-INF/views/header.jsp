<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<style type="text/css">
.the_header {
	background-color: #E94E3A;
	color: white;
	height: 34px;
	line-height: 34px;
	padding: 0 10px;
	margin-bottom: 20px;
}

.the_header .companyName {
	display: inline-block;
	font-size: 20px;
	font-weight: bold;
	float: left;
}

.the_header .userName {
	display: inline-block;
	float: right;
	margin-right: 20px;
	font-weight: bold;
}

.the_header .userName i {
	margin-right: 5px;
}

.the_header a {
	color: white;
}

.the_header .glyphicon-home {
	font-size: 23px;
	margin-right: 10px;
	float: left;
	display: inline-block;
	margin-top: 5px;
}

.the_header .glyphicon-new-window {
	margin-right: 5px;
	float: right;
	display: inline-block;
	margin-top: 5px;
	cursor: pointer;
}
</style>
<div class="the_header">
	<i class="glyphicon glyphicon-home"></i> <span class="companyName">华正大数据</span>
	<a href="${basePath}/logout"> <i
		class="glyphicon glyphicon-new-window"></i>
	</a> <span class="userName"><i class="am-icon-user"></i>${loginName}</span>
</div>

