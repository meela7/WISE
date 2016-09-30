package org.cilab.s4rm.service.impl;

import java.util.List;
import java.util.Map;

import org.cilab.s4rm.dao.TagDAO;
import org.cilab.s4rm.model.Tag;
import org.cilab.s4rm.service.TagService;

public class TagServiceImpl implements TagService {

	/**
	 * Class Name:	TagServiceImpl.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.16
	 * @version 1.0
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	
	TagDAO tagDao;
	
	public void setTagDao(TagDAO tagDao){
		this.tagDao = tagDao;
	}
	
	@Override
	public boolean newInstance(Tag tag) {
		return this.tagDao.create(tag);
	}

	@Override
	public Tag readInstance(int tagID) {
		return this.tagDao.read(tagID);
	}

	@Override
	public boolean updateInstance(Tag tag) {
		return this.tagDao.update(tag);
	}

	@Override
	public boolean deleteInstance(int tagID) {
		return this.tagDao.delete(tagID);
	}

	@Override
	public List<Tag> readCollection() {
		return this.tagDao.list();
	}

	@Override
	public boolean isInstanceExist(String name) {
		if(this.tagDao.getByUniqueKey(name) != null)
			return true;
		else
			return false;
	}
	
	@Override
	public List<Tag> search(Map<String, String> map) {
		return this.tagDao.search(map);
	}

	@Override
	public List<Tag> listSearch(Map<String, List<String>> map) {
		return this.tagDao.listSearch(map);
	}

}
