package com.jt.manage.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jt.common.po.BasePojo;

/**
 * 1.@Table 定义对应的表 
 * 2.@Id 定义数据表的主键
 * 3.@GeneratedValue(strategy = GenerationType.IDENTITY) 表示主键自增 
 * 4.@Column(name="") 可定义对应表字段
 * 
 * @author Tarena
 *
 */
@Table(name = "tb_item_cat")
@JsonIgnoreProperties(ignoreUnknown=true)
public class ItemCat extends BasePojo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long parentId;
	private String name;
	private Integer status;
	private Integer sortOrder;
	private Boolean isParent;

	/**
	 * 为满足Tree返回值结构,添加getText和getState方法
	 */
	
	public String getText(){
		return name;
	}
	
	public String getState(){
		return isParent?"closed":"open";
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}
	

}
