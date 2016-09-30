package org.cilab.m4.service.impl;

import java.util.List;
import java.util.Map;

import org.cilab.m4.dao.MethodDAO;
import org.cilab.m4.model.Method;
import org.cilab.m4.service.MethodService;

public class MethodServiceImpl implements MethodService {

	/**
	 * Class Name:	MethodServiceImpl.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.02.04
	 * @version 1.0
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	
	MethodDAO methodDao;
	
	public void setMethodDao(MethodDAO methodDao){
		this.methodDao = methodDao;
	}
	
	@Override
	public boolean newInstance(Method method) {
		
		return methodDao.create(method);			
	}

	@Override
	public Method readInstance(int methodID) {
		Method method = methodDao.read(methodID);
		return method;
	}

	@Override
	public boolean updateInstance(Method method) {
		return methodDao.update(method);
	}

	@Override
	public boolean deleteInstance(int methodID) {
		return methodDao.delete(methodID);
	}

	@Override
	public List<Method> readCollection() {
		return methodDao.list();
	}

	@Override
	public boolean isInstanceExist(String name) {
		if(methodDao.getByUniqueKey(name) != null)
			return true;
		else
			return false;
	}

	@Override
	public List<Method> search(Map<String, String> map) {
		return methodDao.search(map);
	}
	@Override
	public List<Method> listSearch(Map<String, List<String>> map) {
		return methodDao.listSearch(map);
	}

}
