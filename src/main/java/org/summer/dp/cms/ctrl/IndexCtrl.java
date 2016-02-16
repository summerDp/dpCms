package org.summer.dp.cms.ctrl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class IndexCtrl extends BaseController{
	
	@RequestMapping("/")
	public ModelAndView index(){
		return new ModelAndView("forward:/door.html");
	}
}
