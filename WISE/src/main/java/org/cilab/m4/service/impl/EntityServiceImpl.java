package org.cilab.m4.service.impl;

import java.util.List;
import java.util.Map;

import org.cilab.m4.dao.EntityDAO;
import org.cilab.m4.model.Entity;
import org.cilab.m4.service.EntityService;

public class EntityServiceImpl implements EntityService {
	/**
	 * Class Name:	EntityServiceImpl.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.16
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	
	EntityDAO entityDao;
	
	public void setEntityDao(EntityDAO entityDao){
		this.entityDao = entityDao;
	}
	
	@Override
	public boolean newInstance(Entity entity) {
		return this.entityDao.create(entity);
	}

	@Override
	public Entity readInstance(int entityID) {
		return this.entityDao.read(entityID);
	}

	@Override
	public boolean updateInstance(Entity entity) {
		return this.entityDao.update(entity);
	}

	@Override
	public boolean deleteInstance(int entityID) {
		return this.entityDao.delete(entityID);
	}

	@Override
	public List<Entity> readCollection() {
		return this.entityDao.list();
	}

	@Override
	public boolean isInstanceExist(String name) {
		if(this.entityDao.getByUniqueKey(name) != null)
			return true;
		else
			return false;
	}

	@Override
	public List<Entity> search(Map<String, String> map) {
		return this.entityDao.search(map);
	}

	@Override
	public List<Entity> listSearch(Map<String, List<String>> map) {
		return this.entityDao.listSearch(map);
	}

}
