package com.scau.keshe.sharespace.database;

/**
 * 实现数据与实体的一个数据操作
 * @author ShouLun
 *
 */
public interface DataBaseImpl {
	
	public boolean insertInfo();
	
	public boolean updateInfo();
	
	public boolean deleteInfo();
	
	public Object selecte();
}
