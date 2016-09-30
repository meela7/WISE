package org.cilab.m4.service.impl;

import java.util.List;
import java.util.Map;

import org.cilab.m4.dao.UnitDAO;
import org.cilab.m4.model.Unit;
import org.cilab.m4.service.UnitService;

public class UnitServiceImpl implements UnitService {

	/**
	 * Class Name:	UnitServiceImpl.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.16
	 * @version 1.0
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	
	UnitDAO unitDao;
	
	public void setUnitDao(UnitDAO unitDao){
		this.unitDao = unitDao;
	}
	
	@Override
	public boolean newInstance(Unit unit) {
		return this.unitDao.create(unit);
	}

	@Override
	public Unit readInstance(int unitID) {
		return this.unitDao.read(unitID);
	}

	@Override
	public boolean updateInstance(Unit unit) {
		return this.unitDao.update(unit);
	}

	@Override
	public boolean deleteInstance(int unitID) {
		return this.unitDao.delete(unitID);
	}

	@Override
	public List<Unit> readCollection() {
		return this.unitDao.list();
	}

	@Override
	public boolean isInstanceExist(String name) {
		if(this.unitDao.getByUniqueKey(name) != null)
			return true;
		else
			return false;
	}
	public Unit getInstanceByUniqueKey(String name) {
		return this.unitDao.getByUniqueKey(name);
	}

	@Override
	public List<Unit> search(Map<String, String> map) {
		return this.unitDao.search(map);
	}

	@Override
	public List<Unit> listSearch(Map<String, List<String>> map) {
		return this.unitDao.listSearch(map);
	}

}
