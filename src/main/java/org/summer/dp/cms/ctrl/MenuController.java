package org.summer.dp.cms.ctrl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.summer.dp.cms.entity.base.Menu;
import org.summer.dp.cms.support.Response;

@RestController
public class MenuController {
	private static Logger log = LoggerFactory.getLogger(MenuController.class);

	@RequestMapping(value = "/menu")
	@ResponseBody
	public Response menu(Response response) {
		List<Menu> menus = new ArrayList<Menu>();
		Menu menu = new Menu();
		menu.setName("URP管理");
		menu.setUri("/userManager");
		menu.setLevel((short) 1);
		menus.add(menu);
		
		Menu menu1 = new Menu();
		menu1.setName("用户管理");
		menu1.setUri("/userManager");
		menu1.setLevel((short) 2);
		menus.add(menu1);
		
		Menu menu2 = new Menu();
		menu2.setName("权限管理");
		menu2.setUri("/permissionManager");
		menu2.setLevel((short) 2);
		menus.add(menu2);


		response.setData(menus);
		return response;
	}

	@RequestMapping(value = "/myMenu")
	@ResponseBody
	public Response myMenu(Response response) {
		List<Menu> menus = new ArrayList<Menu>();
		Menu menu = new Menu();
		menu.setName("URP管理");
		menu.setUri("/userManager");
		menu.setLevel((short) 1);
		menus.add(menu);
		
		Menu menu1 = new Menu();
		menu1.setName("用户管理");
		menu1.setUri("/userManager");
		menu1.setLevel((short) 2);
		menus.add(menu1);
		
		Menu menu2 = new Menu();
		menu2.setName("权限管理");
		menu2.setUri("/permissionManager");
		menu2.setLevel((short) 2);
		menus.add(menu2);

		response.setData(menus);
		return response;
	}
}
