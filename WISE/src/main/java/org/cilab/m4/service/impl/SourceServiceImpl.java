package org.cilab.m4.service.impl;

import java.util.List;
import java.util.Map;

import org.cilab.m4.dao.SourceDAO;
import org.cilab.m4.model.Source;
import org.cilab.m4.service.SourceService;

public class SourceServiceImpl implements SourceService {

	/**
	 * Class Name:	SourceServiceImpl.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.14
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	
	SourceDAO sourceDao;
	public void setSourceDao(SourceDAO sourceDao){
		this.sourceDao = sourceDao;
	}
	
	@Override
	public boolean newInstance(Source source) {
		return this.sourceDao.create(source);
	}

	@Override
	public Source readInstance(int sourceID) {
		return this.sourceDao.read(sourceID);
	}

	@Override
	public boolean updateInstance(Source source) {
		return this.sourceDao.update(source);
	}

	@Override
	public boolean deleteInstance(int sourceID) {
		return this.sourceDao.delete(sourceID);
	}

	@Override
	public List<Source> readCollection() {
		return this.sourceDao.list();
	}

	@Override
	public boolean isInstanceExist(String institution, String name) {
		if(this.sourceDao.getByUniqueKey(institution,name) != null)
			return true;
		return false;
	}

	@Override
	public List<Source> search(Map<String, String> map) {
		return this.sourceDao.search(map);
	}

	@Override
	public List<Source> listSearch(Map<String, List<String>> map) {
		return this.sourceDao.listSearch(map);
	}

}
