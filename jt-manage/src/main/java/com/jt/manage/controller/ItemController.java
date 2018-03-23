package com.jt.manage.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.EasyUIResult;
import com.jt.common.vo.SysResult;
import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemDesc;
import com.jt.manage.service.ItemService;

@Controller
@RequestMapping("/item/")
public class ItemController {
	
	private static final Logger logger = Logger.getLogger(ItemController.class);
	
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("findAll")
	@ResponseBody
	public List<Item> findItemAll (){
		return itemService.findItemAll();
	}
	
	@RequestMapping("query")
	@ResponseBody
	public EasyUIResult findItemByPage(Integer page,Integer rows){
		return itemService.findItemByPage(page,rows);
	}
	
	/*
	 * @RsponseBody
	 * 1.若返回值是对象,解析编码格式为"UTF-8"
	 * 2.若返回值是String类型,解析编码格式为"ISO-8859-1" 
	 */
	@RequestMapping(value="cat/queryItemCatName",produces="text/html;charset=utf-8")
	@ResponseBody
	public String findItemCatNameById(Long itemCatId){
		return itemService.findItemCatName(itemCatId);
	}
	
	@RequestMapping("save")
	@ResponseBody
	public SysResult saveItem(Item item,String desc){
		try {
			itemService.saveItem(item,desc);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "新建商品失败");
		}
	}
	
	@RequestMapping("update")
	@ResponseBody
	public SysResult updateItem(Item item,String desc){
		try {
			itemService.updateItem(item,desc);
			logger.info("{更新操作成功}");
			return SysResult.build(200, "更新成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("{更新操作失败}");
			return SysResult.build(201, "更新失败");
		}
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public SysResult deleteItem(Long[] ids){
		try {
			itemService.deleteItems(ids);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "删除失败");
		}
	}
	
	@RequestMapping("instock")
	@ResponseBody
	public SysResult itemInstock(Long[] ids){
		try {
			int status = 2;
			itemService.updateStatus(status,ids);
			return SysResult.build(200, "下架成功");
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "下架失败");
		}
	}
	
	
	@RequestMapping("reshelf")
	@ResponseBody
	public SysResult itemReshelf(Long[] ids){
		try {
			int status = 1;
			itemService.updateStatus(status,ids);
			return SysResult.build(200, "上架成功");
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "上架失败");
		}
	}
	
	@RequestMapping("query/item/desc/{itemId}")
	@ResponseBody
	public SysResult findItemDescById(@PathVariable Long itemId){
		try {
			ItemDesc itemDesc = itemService.findItemDescById(itemId);
			return SysResult.oK(itemDesc);
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "商品描述信息查询失败");
		}		
	}
	
}
