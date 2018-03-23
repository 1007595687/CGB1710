package com.jt.manage.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.common.vo.EasyUIResult;
import com.jt.manage.mapper.ItemDescMapper;
import com.jt.manage.mapper.ItemMapper;
import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemDesc;

import redis.clients.jedis.JedisCluster;

@Service
public class ItemServiceimpl implements ItemService {

	@Autowired
	private ItemMapper itemMapper;
	
	@Autowired
	private ItemDescMapper itemDescMapper;
	
	@Autowired
	private JedisCluster jedisCluster;
	
	@Override
	public List<Item> findItemAll() {
		return itemMapper.findItemAll();
	}

	@Override
	public EasyUIResult findItemByPage(Integer page, Integer rows) {
		int total = itemMapper.findItemCount();		
		int begin = (page-1)*rows;
		List<Item> itemList = itemMapper.findItemByPage(begin,rows);
		return new EasyUIResult(total, itemList);
	}

	@Override
	public String findItemCatName(Long itemCatId) {
		return itemMapper.findItemCatName(itemCatId);
	}

	@Override
	public void saveItem(Item item,String desc) {
		item.setStatus(1);
		item.setCreated(new Date());
		item.setUpdated(item.getCreated());
		itemMapper.insert(item);
		
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemDesc(desc);
		itemDesc.setItemId(item.getId());
		itemDesc.setCreated(item.getCreated());
		itemDesc.setUpdated(item.getCreated());
		itemDescMapper.insert(itemDesc);
	}

	@Override
	public void updateItem(Item item,String desc) {
		item.setUpdated(new Date());
		itemMapper.updateByPrimaryKeySelective(item);
		
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setUpdated(item.getUpdated());
		itemDescMapper.updateByPrimaryKeySelective(itemDesc);
		jedisCluster.del("ITEM_"+item.getId());
	}

	/**
	 * 一般先删从表
	 */
	@Override
	public void deleteItems(Long[] ids) {
		itemDescMapper.deleteByIDS(ids);
		itemMapper.deleteByIDS(ids);
		for (Long id : ids) {
			jedisCluster.del("ITEM_"+id);
		}
	}

	@Override
	public void updateStatus(int status, Long[] ids) {
		/*for (Long id : ids) {
			Item item = new Item();
			item.setId(id);
			item.setStatus(status);
			item.setUpdated(new Date());
			itemMapper.updateByPrimaryKeySelective(item);
		}*/
		itemMapper.updateStatus(status,ids);
		
	}

	@Override
	public ItemDesc findItemDescById(Long itemId) {
		return itemDescMapper.selectByPrimaryKey(itemId);
	}

	@Override
	public Item findItemById(Long itemId) {
		
		return itemMapper.selectByPrimaryKey(itemId);
	}

}
