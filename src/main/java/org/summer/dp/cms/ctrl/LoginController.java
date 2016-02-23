package org.summer.dp.cms.ctrl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.summer.dp.cms.entity.base.Account;
import org.summer.dp.cms.entity.base.Employee;
import org.summer.dp.cms.entity.base.Post;
import org.summer.dp.cms.helper.exception.AccountNoActiceException;
import org.summer.dp.cms.helper.hash.MD5;
import org.summer.dp.cms.helper.servlet.KaptchaProducerAgency;
import org.summer.dp.cms.service.AccountService;
import org.summer.dp.cms.service.CurrentUserInfoService;
import org.summer.dp.cms.support.Response;
import org.summer.dp.cms.support.ResponseFactory;
import org.summer.dp.cms.support.StateCode;
import org.summer.dp.cms.vo.CurrentInfo;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;

/**
 * 登录控制器
 * @author 赵宝东
 *
 */
@RestController
public class LoginController {
	private static Logger log = LoggerFactory.getLogger(LoginController.class);
	@Autowired private AccountService accountService;
	@Autowired private CurrentUserInfoService currentUserInfoService;
	@Autowired private StringRedisTemplate redisTemplate;//只有STRING 序列化成JSON用着先,等出稳定版 



	/**
	 * 
	 * @Title: loginDo 
	 * @Description: 全平台登录
	 * @param @param username
	 * @param @param password
	 * @return Response 返回类型,如果成功返回跳转的URL
	 * @throws
	 */
	@RequestMapping(value = "/login")
	@ResponseBody
	public Response login(String username, String password, String logincode , Response response ) {
		String msg = "";
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		String codeSession = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
		if (StringUtils.isBlank(logincode) || StringUtils.isBlank(codeSession)
				|| !logincode.equals(codeSession)) {
			response.setStateCode(StateCode.LOGIN_FAIL);
			msg = "验证码不正确,朋友！";
		} else {
			AuthenticationToken token = new UsernamePasswordToken(username,password);
			Account account = accountService.findByLoginName(username);
			try {
				currentUser.login(token);
				account.setPassword("");
				// 获取当前登录用户的岗位信息。
				CurrentInfo currentInfo = currentUserInfoService.findCurrentUserInfo(account);
				Employee emplpyee = currentInfo.getEmployee();
				if (emplpyee == null || emplpyee.getDefaultPostId() == null) {
					throw new AccountNoActiceException();
				}
				long defaultPostId = emplpyee.getDefaultPostId();

				// 遍历岗位信息,如果有一个岗位不需要过滤权限,那么这个人不需要过滤权限
				Set<Post> postSet = currentInfo.getPostList();
				Iterator<Post> it = postSet.iterator();
				if (it.hasNext()) {
					Post post = it.next();
					if (post.getNeedFilter() == null) {// 当前登录的员工不需要过滤任何权限
						currentInfo.setNeedFilter(false);
					}
					if (post.getId() == defaultPostId) {// 该人的默认岗位
						currentInfo.setDefaultPostId(defaultPostId);// 保存到SESSION里，快速获取
						currentInfo.setIndexPage(post.getIndexPage());// 保存到SESSION里，快速获取
						currentUser.getSession().setAttribute("currentInfo", currentInfo);
						response.setStateCode(StateCode.OK);
						response.setData("index.html");// 把该人应该跳转的页面返回到客户端
					}
				}

				msg = "登录成功";
			} catch (UnknownAccountException uae) {
				response.setStateCode(StateCode.LOGIN_FAIL);
				msg = "用户不存在！";
			} catch (IncorrectCredentialsException ice) {
				response.setStateCode(StateCode.LOGIN_FAIL);
				msg = "用户名或密码错误！";
			} catch (LockedAccountException lae) {
				response.setStateCode(StateCode.LOGIN_FAIL);
				msg = "用户为锁定状态！";
			} catch (AuthenticationException ae) {
				response.setStateCode(StateCode.LOGIN_FAIL);
				ae.printStackTrace();
				msg = "登录失败！";
			} catch (AccountNoActiceException ana) {
				response.setStateCode(StateCode.LOGIN_FAIL);
				msg = "该帐号未激活！";
			} catch (Exception e) {
				response.setStateCode(StateCode.LOGIN_FAIL);
				e.printStackTrace();
				msg = "平台繁忙！";
			}
		}
		response.setMessage(msg);
		currentUser.getSession().removeAttribute(Constants.KAPTCHA_SESSION_KEY);
		return response;
	}


	/**
	 * 获取登录的图片验证码
	 */
	@RequestMapping(value = "/imgcode", method = RequestMethod.GET)
	public void captcha(HttpServletRequest request, HttpServletResponse response )
			throws ServletException, IOException {
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		Producer captchaProducer = KaptchaProducerAgency.getKaptchaProducerExample();
		response.setDateHeader("Expires", 0);
		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control",
				"no-store, no-cache, must-revalidate");
		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");
		// return a jpeg
		response.setContentType("image/jpeg");
		// create the text for the image
		String capText = captchaProducer.createText();
		log.debug("******************验证码是: " + capText + "******************");
		// store the text in the session
		session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText	);
		// create the image with the text
		BufferedImage bi = captchaProducer.createImage(capText);
		ServletOutputStream out = response.getOutputStream();
		// write the data out
		ImageIO.write(bi, "jpg", out);
		try {
			out.flush();
		} finally {
			out.close();
		}
	}

	@RequestMapping(value = "/logout.do")
	@ResponseBody
	public Response logout() {
		Response response = ResponseFactory.getResponse();
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.logout();
		return response;
	}

	@RequestMapping(value = "/login/timeOut")
	@ResponseBody
	public Response timeOut(short StatusCode, HttpSession session) {
		Response response = ResponseFactory.getResponse();
		response.setStateCode(StateCode.LOGIN_TIMEOUT);
		response.setMessage("会话已经超时,请重新登录");
		return response;
	}

	@RequestMapping(value = "nono")
	@ResponseBody
	public Response nono(short StatusCode, HttpSession session) {
		Response response = ResponseFactory.getResponse();
		response.setMessage("未授权使用资源");
		return response;
	}

	@RequestMapping(value = "/login/success.do")
	@ResponseBody
	public Response success(HttpSession session) {
		Response response = ResponseFactory.getResponse();
		response.setMessage("登录成功");
		return response;
	}

	@RequestMapping(value = "/getMenuAndUserName")
	@ResponseBody
	public Response isLogin(HttpSession session,Response response) {
		Subject currentUser = SecurityUtils.getSubject();
		if(currentUser.isAuthenticated()){
			CurrentInfo currentInfo = (CurrentInfo) currentUser.getSession().getAttribute("currentInfo");
			response.setFieldPaths(new String[]{"menus","permissions","employee"});
			response.setData(currentInfo);
		}else{response.setData(false);}
		return response;
	}

}
