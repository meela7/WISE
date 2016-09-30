package org.cilab.s4rm.service.impl;

import java.util.List;
import java.util.Map;

import org.cilab.s4rm.dao.MetaDAO;
import org.cilab.s4rm.model.Metadata;
import org.cilab.s4rm.service.MetaService;

public class MetaServiceImpl implements MetaService {

	/**
	 * Class Name:	MetaServiceImpl.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.16
	 * @version 1.0
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	
	MetaDAO metaDao;
	
	public void setMetaDao(MetaDAO metaDao){
		this.metaDao = metaDao;
	}
	
	@Override
	public boolean newInstance(Metadata meta) {
		return this.metaDao.create(meta);
	}

	@Override
	public Metadata readInstance(int metaID) {
		return this.metaDao.read(metaID);
	}

	@Override
	public boolean updateInstance(Metadata meta) {
		return this.metaDao.update(meta);
	}

	@Override
	public boolean deleteInstance(int metaID) {
		return this.metaDao.delete(metaID);
	}

	@Override
	public List<Metadata> readCollection() {
		return this.metaDao.list();
	}

	@Override
	public boolean isInstanceExist(String key, String value) {
		if(this.metaDao.getByUniqueKey(key, value) != null)
			return true;
		else
			return false;
	}

	@Override
	public List<Metadata> search(Map<String, String> map) {
		return this.metaDao.search(map);
	}

	@Override
	public List<Metadata> listSearch(Map<String, List<String>> map) {
		return this.metaDao.listSearch(map);
	}

}
