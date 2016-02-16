package org.summer.dp.cms.ctrl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexCtrl extends BaseController{
	
	
	@RequestMapping("/")
	public String index(){
		return "你好我是INDEX";
	}
}
