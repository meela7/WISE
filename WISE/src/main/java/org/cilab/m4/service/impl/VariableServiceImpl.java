package org.cilab.m4.service.impl;

import java.util.List;
import java.util.Map;

import org.cilab.m4.dao.VariableDAO;
import org.cilab.m4.model.Variable;
import org.cilab.m4.service.VariableService;

public class VariableServiceImpl implements VariableService {

	/**
	 * Class Name:	VariableServiceImpl.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.16
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	
	VariableDAO variableDao;
	
	public void setVariableDao(VariableDAO variableDao){
		this.variableDao = variableDao;
	}
	
	@Override
	public boolean newInstance(Variable variable) {
		return this.variableDao.create(variable);
	}

	@Override
	public Variable readInstance(int variableID) {
		return this.variableDao.read(variableID);
	}

	@Override
	public boolean updateInstance(Variable variable) {
		return this.variableDao.update(variable);
	}

	@Override
	public boolean deleteInstance(int variableID) {
		return this.variableDao.delete(variableID);
	}

	@Override
	public List<Variable> readCollection() {
		return this.variableDao.list();
	}

	@Override
	public boolean isInstanceExist(String name, int unitID) {
		if(this.variableDao.getByUniqueKey(name, unitID) != null)
			return true;
		else
			return false;
	}

	@Override
	public List<Variable> search(Map<String, String> map) {
		return this.variableDao.search(map);
	}

	@Override
	public List<Variable> listSearch(Map<String, List<String>> map) {
		return this.variableDao.listSearch(map);
	}

}
