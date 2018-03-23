package com.jt.manage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jt.common.mapper.SysMapper;
import com.jt.manage.pojo.Item;

public interface ItemMapper extends SysMapper<Item>{
	List<Item> findItemAll();

	int findItemCount();

	List<Item> findItemByPage(@Param("begin") int begin,@Param("rows") Integer rows);

	String findItemCatName(Long itemCatId);
	
	void updateStatus(@Param("status")int status, @Param("ids")Long[] ids);
}
