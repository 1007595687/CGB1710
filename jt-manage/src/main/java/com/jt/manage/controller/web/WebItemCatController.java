package com.jt.manage.controller.web;


//import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.vo.ItemCatResult;
import com.jt.manage.service.ItemCatService;

@Controller
public class WebItemCatController {
	
	@Autowired
	private ItemCatService itemCatService;
	
//	private static ObjectMapper objectMapper =new ObjectMapper();

	/*@RequestMapping("/web/itemcat/all")
	public void findItemCatAll(String callback,HttpServletResponse response){
		try {
			ItemCatResult catResult = itemCatService.findItemCatAll();
			String jsonData = objectMapper.writeValueAsString(catResult);
			String result = callback+"("+jsonData+")";
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	@RequestMapping("/web/itemcat/all")
	@ResponseBody
	public Object findItemCatAll(String callback){
		ItemCatResult catResult = itemCatService.findCacheItemCatAll();
		MappingJacksonValue jacksonValue = new MappingJacksonValue(catResult);
		jacksonValue.setJsonpFunction(callback);
		return jacksonValue;
	}
}
