package com.jt.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@RequestMapping("/index")
	public String index(){
		return "index";
	}
	
	//@PathVariable 的作用:可以将{}中的数据传值给参数列表,需名称一致
	/*
	 * restful:
	 * 		1.restful结构参数用{}包裹
	 * 		2.参数之间用"/"分割
	 * 		3.接受参数与变量一致
	 * 		4.使用@PathVariable 注解
	 */
	@RequestMapping("/page/{module}")
	public String toItemAdd(@PathVariable String module){
		return module;
	}
	
}
