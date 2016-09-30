package org.cilab.m4.service;

import java.util.List;
import java.util.Map;

import org.cilab.m4.model.Entity;

public interface EntityService {
	
	/**
	 * Class Name:	EntityService.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.16
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	public boolean newInstance(Entity entity);
	public Entity readInstance(int entityID);
	public boolean updateInstance(Entity entity);
	public boolean deleteInstance(int entityID);	
	public List<Entity> readCollection();
	
	public boolean isInstanceExist(String name);
	public List<Entity> search(Map<String, String> map);
	public List<Entity> listSearch(Map<String, List<String>> map);

}
