<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page
	import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />


<html lang="en" class="no-js">

<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta charset="utf-8">
<title>华正大数据平台登录</title>
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-control" content="no-cache">
<meta http-equiv="Cache" content="no-cache">
<!-- CSS -->
<link rel="stylesheet" type="text/css" href="css/function/login.css" />

<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
            <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->

</head>

<body>

	<div class="page-container">
		<h1>登录</h1>
		<form>
			<input type="text" name="username" class="username"
				placeholder="请输入您的用户名！"> <input type="password"
				name="password" class="password" placeholder="请输入您的用户密码！"> <input
				type="Captcha" class="Captcha" name="loginCode"
				placeholder="请输入验证码！"> <img src="captcha.do" id="captchaImg" />
			<div class="errorresult"></div>
			<button type="button" class="submit_button">登录</button>
			<label> <a href="">注册</a>
			</label>

		</form>
	</div>

	<!-- Javascript -->
	<script src="js/lib/jQuery/jquery.min.js" type="text/javascript"
		charset="utf-8"></script>
	<script src="js/function/login/login.min.js" type="text/javascript"
		charset="utf-8"></script>
	<script src="js/function/login/login-init.js" type="text/javascript"
		charset="utf-8"></script>
	<script type="text/javascript">
		$(".submit_button").click(function() {
			submitForm();
		});
		$(document).keyup(function(event) {
			if (event.keyCode == 13) {
				submitForm();
			}
		});
		$(".page-container img").on("click", function() {
			$(this).prop("src", "captcha.do?" + Math.random());
		});

		function submitForm() {
			//alert($.trim($(".username").val()).length);
			if ($.trim($(".username").val()).length < 1) {
				$(".errorresult").html("请输入用户名！");
				$(".username").focus();
				return false;
			}
			if ($.trim($(".password").val()).length < 1) {
				$(".errorresult").html("请输入密码！");
				$(".password").focus();
				return false;
			}
			if ($.trim($(".Captcha").val()).length < 1) {
				$(".errorresult").html("请输入验证码！");
				$(".Captcha").focus();
				return false;
			}
			$.ajax({
				url : "login.do?",
				type : "post",
				data : $("form").serialize(),
				success : function(data) {
					var resultNum = parseInt(data.errorcode);
					if (resultNum == 1) {
						var uri = data.data;
						location.href = uri;
					} else {
						$(".errorresult").html(data.message);
						$(".Captcha").focus();
						$(".page-container img").prop("src",
								"captcha.do?" + Math.random());
					}
				},
				cache : false,
				timeout : 5000,
				error : function() {
					$(".errorresult").html("登录时出现通讯错误,请联系信息中心！");
					$(".page-container img").prop("src",
							"captcha.do?" + Math.random());
				}
			});
		}
	</script>
</body>

</html>