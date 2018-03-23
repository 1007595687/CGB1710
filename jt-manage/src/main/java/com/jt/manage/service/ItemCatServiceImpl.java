package com.jt.manage.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.vo.ItemCatData;
import com.jt.common.vo.ItemCatResult;
//import com.jt.common.service.RedisService;
//import com.jt.common.service.RedisServiceDemo;
import com.jt.manage.mapper.ItemCatMapper;
import com.jt.manage.pojo.ItemCat;

import redis.clients.jedis.JedisCluster;

@Service
public class ItemCatServiceImpl implements ItemCatService {

	private static ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private ItemCatMapper itemCatMapper;

	/*
	 * @Autowired private Jedis jedis;
	 */
	/*
	 * @Autowired private RedisService redisService;
	 */
	@Autowired
	private JedisCluster jedisCluster;

	@Override
	public List<ItemCat> findItemCat() {

		return itemCatMapper.select(null);
	}

	@Override
	public List<ItemCat> findItemCatByParentId(Long parentId) {
		/*
		 * 迭代,实现redis缓存
		 */
		List<ItemCat> itemCatList = new ArrayList<ItemCat>();
		String key = "ITEM_CAT_" + parentId;
		String jsonData = jedisCluster.get(key);
		try {
			if (StringUtils.isEmpty(jsonData)) {
				/*
				 * 业务实现
				 */
				ItemCat itemCat = new ItemCat();
				itemCat.setParentId(parentId);
				itemCat.setStatus(1);
				itemCatList = itemCatMapper.select(itemCat);
				// ---------
				String resultJSON = objectMapper.writeValueAsString(itemCatList);
				jedisCluster.set(key, resultJSON);
			} else {
				ItemCat[] itemCats = objectMapper.readValue(jsonData, ItemCat[].class);
				itemCatList = Arrays.asList(itemCats);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemCatList;
	}

	/*
	 * @Override public ItemCatResult findItemCatAll() { String key =
	 * "ITEM_CAT_ALL"; String jsonData = jedisCluster.get(key); List<ItemCat>
	 * itemCatLists=null; try { if(StringUtils.isEmpty(jsonData)){
	 * //System.out.println("From DB"); ItemCat tempItemCat = new ItemCat();
	 * tempItemCat.setStatus(1); itemCatLists =
	 * itemCatMapper.select(tempItemCat); String
	 * resultJson=objectMapper.writeValueAsString(itemCatLists);
	 * jedisCluster.set(key, resultJson); }else{ //System.out.println(
	 * "From redis"); ItemCat[] itemCats = objectMapper.readValue(jsonData,
	 * ItemCat[].class); itemCatLists = Arrays.asList(itemCats); } } catch
	 * (IOException e) { e.printStackTrace(); throw new
	 * RuntimeException("数据获取失败"); } Map<Long, List<ItemCat>> itemCatMap = new
	 * HashMap<Long,List<ItemCat>>(); for (ItemCat itemCat : itemCatLists) {
	 * if(itemCatMap.containsKey(itemCat.getParentId())){
	 * itemCatMap.get(itemCat.getParentId()).add(itemCat); }else{ List<ItemCat>
	 * itemCatList = new ArrayList<ItemCat>(); itemCatList.add(itemCat);
	 * itemCatMap.put(itemCat.getParentId(), itemCatList); } } ItemCatResult
	 * catResult = new ItemCatResult(); List<ItemCatData> itemCatDataList1 = new
	 * ArrayList<ItemCatData>(); for (ItemCat itemCat1 : itemCatMap.get(0L)) {
	 * ItemCatData itemCatData1 = new ItemCatData();
	 * itemCatData1.setUrl("/products/"+itemCat1.getId()+".html");
	 * itemCatData1.setName("<a href='"
	 * +itemCatData1.getUrl()+"'>"+itemCat1.getName()+"</a>"); List<ItemCatData>
	 * itemCatDataList2 = new ArrayList<ItemCatData>(); for(ItemCat itemCat2 :
	 * itemCatMap.get(itemCat1.getId())){ ItemCatData itemCatData2 = new
	 * ItemCatData(); itemCatData2.setUrl("/products/"+itemCat2.getId());
	 * itemCatData2.setName(itemCat2.getName()); List<String> itemCatDataList3=
	 * new ArrayList<String>(); for (ItemCat itemCat3 :
	 * itemCatMap.get(itemCat2.getId())) {
	 * itemCatDataList3.add("/product/"+itemCat3.getId()+"|"+itemCat3.getName())
	 * ; } itemCatData2.setItems(itemCatDataList3);
	 * itemCatDataList2.add(itemCatData2); }
	 * itemCatData1.setItems(itemCatDataList2);
	 * itemCatDataList1.add(itemCatData1); if(itemCatDataList1.size()>13) break;
	 * } catResult.setItemCats(itemCatDataList1); return catResult; }
	 */

	@Override
	public ItemCatResult findItemCatAll() {
		List<ItemCat> itemCatLists = null;
		ItemCat tempItemCat = new ItemCat();
		tempItemCat.setStatus(1);
		itemCatLists = itemCatMapper.select(tempItemCat);
		Map<Long, List<ItemCat>> itemCatMap = new HashMap<Long, List<ItemCat>>();
		for (ItemCat itemCat : itemCatLists) {
			if (itemCatMap.containsKey(itemCat.getParentId())) {
				itemCatMap.get(itemCat.getParentId()).add(itemCat);
			} else {
				List<ItemCat> itemCatList = new ArrayList<ItemCat>();
				itemCatList.add(itemCat);
				itemCatMap.put(itemCat.getParentId(), itemCatList);
			}
		}
		ItemCatResult catResult = new ItemCatResult();
		List<ItemCatData> itemCatDataList1 = new ArrayList<ItemCatData>();
		for (ItemCat itemCat1 : itemCatMap.get(0L)) {
			ItemCatData itemCatData1 = new ItemCatData();
			itemCatData1.setUrl("/products/" + itemCat1.getId() + ".html");
			itemCatData1.setName("<a href='" + itemCatData1.getUrl() + "'>" + itemCat1.getName() + "</a>");
			List<ItemCatData> itemCatDataList2 = new ArrayList<ItemCatData>();
			for (ItemCat itemCat2 : itemCatMap.get(itemCat1.getId())) {
				ItemCatData itemCatData2 = new ItemCatData();
				itemCatData2.setUrl("/products/" + itemCat2.getId());
				itemCatData2.setName(itemCat2.getName());
				List<String> itemCatDataList3 = new ArrayList<String>();
				for (ItemCat itemCat3 : itemCatMap.get(itemCat2.getId())) {
					itemCatDataList3.add("/product/" + itemCat3.getId() + "|" + itemCat3.getName());
				}
				itemCatData2.setItems(itemCatDataList3);
				itemCatDataList2.add(itemCatData2);
			}
			itemCatData1.setItems(itemCatDataList2);
			itemCatDataList1.add(itemCatData1);
			if (itemCatDataList1.size() > 13)
				break;
		}
		catResult.setItemCats(itemCatDataList1);
		return catResult;
	}

	@Override
	public ItemCatResult findCacheItemCatAll() {
		String key = "ITEM_CAT_ALL";

		String jsonData = jedisCluster.get(key);

		try {
			// 判断数据是否为空
			if (StringUtils.isEmpty(jsonData)) {
				ItemCatResult itemCatResult = findItemCatAll();
				// 将对象转化为JSON串
				String restJSON = objectMapper.writeValueAsString(itemCatResult);
				// 将数据存入redis中
				jedisCluster.set(key, restJSON);
				return itemCatResult;

			} else {
				ItemCatResult itemCatResult = objectMapper.readValue(jsonData, ItemCatResult.class);
				return itemCatResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}
